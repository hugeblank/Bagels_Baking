package dev.elexi.hugeblank.bagels_baking.block.entity;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.block.FermenterBlock;
import dev.elexi.hugeblank.bagels_baking.item.BottledItem;
import dev.elexi.hugeblank.bagels_baking.recipe.FermentingRecipe;
import dev.elexi.hugeblank.bagels_baking.util.BakingProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class FermenterBlockEntity extends BlockEntity implements Inventory {
    private Item content;
    private int amount;

    public FermenterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        content = Items.AIR;
        amount = 0;
    }

    public FermenterBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(Baking.FERMENTER_ENTITY_TYPE, blockPos, blockState);
    }

    public Item getContent() {
        return content;
    }

    public boolean canFill(ItemStack stack) {
        return (stack.getItem() == content || content == Items.AIR) && amount < 16 && !this.getCachedState().get(BakingProperties.FERMENTED);
    }

    public ItemStack fillFermenter(ServerWorld world, BlockPos pos, ItemStack stack) {
        if (canFill(stack)) {
            List<FermentingRecipe> recipes = world.getRecipeManager().listAllOfType(FermentingRecipe.TYPE);
            for (FermentingRecipe r : recipes) {
                if (r.getInput().test(stack)) {
                    Item stackItem = stack.getItem();
                    if (stackItem != content) content = stackItem;
                    if (stackItem instanceof HoneyBottleItem || stackItem instanceof BottledItem) {
                        world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    } else {
                        world.playSound(null, pos, SoundEvents.BLOCK_AZALEA_HIT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    }
                    stack.decrement(1);
                    amount++;
                    return new ItemStack(stackItem.getRecipeRemainder());
                }
            }
        }
        return stack;
    }

    public ItemStack drainFermenter(ServerWorld world, BlockPos pos, ItemStack stack) {
        FermentingRecipe recipe = world.getRecipeManager().getFirstMatch(FermentingRecipe.TYPE, this, world).orElse(null);
        if(recipe != null && this.getCachedState().get(FermenterBlock.FERMENTED) && stack.getItem() == recipe.getCollector().getItem()) {
            Item soundItem = recipe.getOutput().getItem();
            if (soundItem instanceof HoneyBottleItem || soundItem instanceof BottledItem) {
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_AZALEA_HIT, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            stack.decrement(1);
            amount--;
            if (amount == 0) {
                content = Items.AIR;
                world.setBlockState(this.getPos(), this.getCachedState().with(FermenterBlock.FERMENTED, false));
            }
            return recipe.craft(this);
        }
        return stack;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        amount = nbt.contains("amount") ? nbt.getInt("amount") : 0;
        content = nbt.contains("item") ? Registry.ITEM.getOrEmpty(new Identifier(nbt.getString("item"))).orElse(Items.AIR) : Items.AIR;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt = super.writeNbt(nbt);
        nbt.putInt("amount", amount);
        nbt.putString("item", content.toString());
        return nbt;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return content == Items.AIR;
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot != 0) return null;
        return new ItemStack(content, amount);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (slot != 0) return null;
        this.amount = 0;
        this.content = Items.AIR;
        return null;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return removeStack(slot, getStack(slot).getCount());
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot == 0) {
            content = stack.getItem();
            amount = stack.getCount();
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {
        content = Items.AIR;
        amount = 0;
    }
}
