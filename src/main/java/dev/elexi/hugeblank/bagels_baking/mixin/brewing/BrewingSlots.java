package dev.elexi.hugeblank.bagels_baking.mixin.brewing;

import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingSlots {


    @Inject(at = @At(value = "HEAD"), method = "isValid(ILnet/minecraft/item/ItemStack;)Z", cancellable = true)
    private void checkValid(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (slot != 3 && slot != 4) {
            List<Ingredient> types = BrewingRecipeRegistryInvoker.getPOTION_TYPES();
            for (Ingredient ingredient : types) {
                if (ingredient.test(stack)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }

}
