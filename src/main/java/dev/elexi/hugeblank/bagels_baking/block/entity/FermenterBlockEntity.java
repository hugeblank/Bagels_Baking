package dev.elexi.hugeblank.bagels_baking.block.entity;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.item.BottledItem;
import dev.elexi.hugeblank.bagels_baking.recipe.FermentingRecipe;
import dev.elexi.hugeblank.bagels_baking.util.BakingProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class FermenterBlockEntity extends BlockEntity implements Inventory{
    private ItemStack stack;

    public FermenterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        stack = ItemStack.EMPTY;
    }

    public FermenterBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(Baking.FERMENTER_ENTITY_TYPE, blockPos, blockState);
    }

    public boolean canFill(ItemStack stack) {
        // if content matches or the content is air (empty) AND the fermenter isn't full and is active OR the fermenter is empty and inactive
        return (stack.getItem() == this.stack.getItem() || this.stack == ItemStack.EMPTY) && ((this.stack.getCount() < this.getMaxCountPerStack() && this.getCachedState().get(BakingProperties.ACTIVE)) || (this.stack.getCount() == 0 && !this.getCachedState().get(BakingProperties.ACTIVE)));
    }

    public ItemStack fillFermenter(ServerWorld world, BlockPos pos, ItemStack stack, boolean dump) {
        if (canFill(stack)) {
            int count = dump ? Math.min(this.getMaxCountPerStack()-this.stack.getCount(), stack.getCount()) : 1;
            if (isValid(0, stack)) {
                this.setStack(0, stack.split(count));
                Item stackItem = stack.getItem();
                if (stackItem instanceof HoneyBottleItem || stackItem instanceof BottledItem) {
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                } else {
                    world.playSound(null, pos, SoundEvents.BLOCK_AZALEA_HIT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
                return new ItemStack(stackItem.getRecipeRemainder(), count);
            }
        }
        return stack;
    }

    public ItemStack drainFermenter(ServerWorld world, BlockPos pos, ItemStack stack, boolean dump) {
        FermentingRecipe recipe = world.getRecipeManager().getFirstMatch(FermentingRecipe.TYPE, this, world).orElse(null);
        if(recipe != null && !this.getCachedState().get(BakingProperties.ACTIVE) && stack.getItem() == recipe.getCollector().getItem()) {
            int count = dump ? Math.min(this.stack.getCount(), stack.getCount()) : 1;
            if (recipe.getCollector().getItem() instanceof GlassBottleItem) {
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_AZALEA_HIT, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            stack.decrement(count);
            this.stack.decrement(count);
            if (this.stack.getCount() == 0) this.stack = ItemStack.EMPTY;
            ItemStack result = recipe.craft(this);
            result.setCount(count);
            return result;
        }
        return stack;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.stack = nbt.contains("Item") ? ItemStack.fromNbt((NbtCompound) nbt.get("Item")) : ItemStack.EMPTY;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt = super.writeNbt(nbt);
        NbtCompound snbt = new NbtCompound();
        this.stack.writeNbt(snbt);
        nbt.put("Item", snbt);
        return nbt;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot != 0) return ItemStack.EMPTY;
        return this.stack;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        // Can't remove anything without glass bottle from player :(
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return removeStack(slot, getStack(slot).getCount());
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        int count = Math.min(this.getMaxCountPerStack()-this.stack.getCount(), stack.getCount());
        if (isValid(slot, stack)) {
            Item stackItem = stack.getItem();
            if (this.stack.isEmpty()) {
                this.stack = new ItemStack(stackItem, count);
            } else {
                this.stack.increment(count);
            }
            World world = this.getWorld();
            if(world != null && !world.isClient()) {
                BlockPos pos = this.getPos();
                BlockState state = this.getCachedState();
                world.setBlockState(pos, state.with(BakingProperties.ACTIVE, true));
            }
        }
    }

    @Override
    public int getMaxCountPerStack() {
        return 16;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        World world = this.getWorld();
        if (world != null && !world.isClient() && slot == 0 && canFill(stack)) {
            List<FermentingRecipe> recipes = world.getRecipeManager().listAllOfType(FermentingRecipe.TYPE);
            for (FermentingRecipe r : recipes) {
                if (r.getIngredients().get(0).test(stack)) return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        this.stack = ItemStack.EMPTY;
    }
}
