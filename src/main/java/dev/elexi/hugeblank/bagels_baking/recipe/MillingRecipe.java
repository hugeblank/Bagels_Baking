package dev.elexi.hugeblank.bagels_baking.recipe;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class MillingRecipe extends CuttingRecipe {

    public MillingRecipe(Identifier id, String group, Ingredient input, ItemStack output) {
        super(Type.INSTANCE, Baking.MILLING_SERIALIZER, id, group, input, output);
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(Baking.MILL_ITEM);
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        return this.input.test(inv.getStack(0));
    }

    public static class Type implements RecipeType<MillingRecipe> {
        private Type() {}

        public static final Type INSTANCE = new Type();
    }
}