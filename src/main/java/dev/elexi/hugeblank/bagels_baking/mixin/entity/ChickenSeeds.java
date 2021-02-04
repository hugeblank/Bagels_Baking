package dev.elexi.hugeblank.bagels_baking.mixin.entity;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChickenEntity.class)
public class ChickenSeeds {

    @Mutable
    @Final
    @Shadow
    private static Ingredient BREEDING_INGREDIENT;

    @Inject(at = @At("HEAD"), method = "<clinit>", cancellable = true)
    private static void validSeeds(CallbackInfo ci) {
        BREEDING_INGREDIENT = Ingredient.ofItems(
                Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Baking.CORN_SEEDS,
                Baking.RICE, Baking.TEA_SEEDS, Baking.CORN_MEAL
        );
        ci.cancel();
    }
}
