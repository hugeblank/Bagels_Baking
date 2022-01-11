package dev.elexi.hugeblank.bagels_baking.mixin.entity.passive;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimalEntity.class)
public class AnimalBreedItems {

    private static final Ingredient breedingItems = Ingredient.ofItems(Items.WHEAT, Baking.CORN);

    @Inject(at = @At("HEAD"), method = "isBreedingItem(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
    public void isBreedingItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(breedingItems.test(stack));
    }
}
