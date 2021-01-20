package dev.elexi.hugeblank.bagels_baking.mixin.brewing;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.item.Items;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipes {

    @Inject(at = @At(value = "TAIL"), method = "registerDefaults()V")
    private static void addRecipes(CallbackInfo ci) {
        BrewingRecipeRegistryInvoker.registerItemRecipe(Baking.RAW_EGG_WHITES, Items.SUGAR, Baking.MERINGUE);
        BrewingRecipeRegistryInvoker.invokeRegisterPotionType(Baking.RAW_EGG_WHITES);
        BrewingRecipeRegistryInvoker.invokeRegisterPotionType(Baking.MERINGUE);
    }

}
