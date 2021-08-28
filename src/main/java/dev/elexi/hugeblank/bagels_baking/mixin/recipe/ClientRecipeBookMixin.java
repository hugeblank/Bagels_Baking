package dev.elexi.hugeblank.bagels_baking.mixin.recipe;

import dev.elexi.hugeblank.bagels_baking.recipe.FermentingRecipe;
import dev.elexi.hugeblank.bagels_baking.recipe.FreezingRecipe;
import dev.elexi.hugeblank.bagels_baking.recipe.MillingRecipe;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {

    @Inject(at = @At("HEAD"), method = "getGroupForRecipe(Lnet/minecraft/recipe/Recipe;)Lnet/minecraft/client/recipebook/RecipeBookGroup;", cancellable = true)
    private static void changeMillingRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        if (recipe.getType() == MillingRecipe.TYPE) {
            cir.setReturnValue(RecipeBookGroup.STONECUTTER);
        }
        if (recipe.getType() == FreezingRecipe.TYPE) {
            cir.setReturnValue(RecipeBookGroup.STONECUTTER);
        }
        if (recipe.getType() == FermentingRecipe.TYPE) {
            cir.setReturnValue(RecipeBookGroup.STONECUTTER);
        }
    }
}
