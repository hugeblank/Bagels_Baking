package dev.elexi.hugeblank.bagels_baking.recipe;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.mixin.recipe.CuttingRecipeSerializerAccessor;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class MillingRecipe extends CuttingRecipe {
    public static final RecipeSerializer<MillingRecipe> SERIALIZER = (RecipeSerializer<MillingRecipe>) CuttingRecipeSerializerAccessor.newSerializer(MillingRecipe::new);
    public static final Identifier ID = new Identifier(Baking.ID, "milling");
    public static final RecipeType<MillingRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ID, new RecipeType<MillingRecipe>() {
        public String toString() {
            return ID.toString();
        }
    });

    public MillingRecipe(Identifier id, String group, Ingredient input, ItemStack output) {
        super(TYPE, SERIALIZER, id, group, input, output);
    }

    public ItemStack createIcon() {
        return new ItemStack(Baking.MILL_ITEM);
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        return this.input.test(inv.getStack(0));
    }
}