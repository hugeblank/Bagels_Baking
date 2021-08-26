package dev.elexi.hugeblank.bagels_baking.block.entity;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.recipe.FermentingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class FermenterBlockEntity extends BlockEntity implements Inventory {
    private Item content;
    private int amount;

    public FermenterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        content = Items.AIR;
        amount = 0;
    }

    public FermenterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(Baking.FERMENTER_ENTITY_TYPE, blockPos, blockState);
    }

    public Item getContent() {
        return content;
    }

    public boolean canFill(ItemStack stack) {
        return stack.getItem() == content || content == Items.AIR && amount < 16;
    }

    public ItemStack fillFermenter(ServerWorld world, ItemStack stack) {
        FermentingRecipe recipe = world.getRecipeManager().getFirstMatch(FermentingRecipe.TYPE, this, world).orElse(null);
        if (recipe != null ) {
            boolean isInput = false;
            for (Ingredient input : recipe.getIngredients()) {
                if (input.test(stack)) {
                    isInput = true;
                };
            }
            if (isInput && !canFill(stack)) {
                if (stack.getItem() != content) content = stack.getItem();
                amount++;
                stack.decrement(1);
            }
        }
        return new ItemStack(stack.getItem().getRecipeRemainder());
    }

    public ItemStack drainFermenter(ServerWorld world, ItemStack stack) {
        FermentingRecipe recipe = world.getRecipeManager().getFirstMatch(FermentingRecipe.TYPE, this, world).orElse(null);
        if(recipe != null && recipe.getCollector().getItem() == content) {
            // play sound!
            stack.decrement(1);
            amount--;
            if (amount == 0) {
                content = Items.AIR;
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
