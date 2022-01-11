package dev.elexi.hugeblank.bagels_baking.mixin.recipe;

import net.minecraft.recipe.CuttingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CuttingRecipe.Serializer.class)
public interface CuttingRecipeSerializerAccessor {
    @Invoker("<init>")
    static CuttingRecipe.Serializer newSerializer(CuttingRecipe.Serializer.RecipeFactory recipeFactory) {
        throw new AssertionError();
    }
}
