package dev.elexi.hugeblank.bagels_baking.mixin.brewing;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(targets = "net.minecraft.screen.BrewingStandScreenHandler$PotionSlot")
public class BrewingSlotsClient {
    @Inject(at = @At(value = "HEAD"), method = "matches(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
    private static void isValid(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        List<Ingredient> types = BrewingRecipeRegistryInvoker.getPOTION_TYPES();
        for (Ingredient ingredient : types) {
            if (ingredient.test(stack)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }

}
