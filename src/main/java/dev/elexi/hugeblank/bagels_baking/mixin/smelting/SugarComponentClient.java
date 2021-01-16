package dev.elexi.hugeblank.bagels_baking.mixin.smelting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.FurnaceFuelSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceFuelSlot.class)
public class SugarComponentClient {
    @Inject(at = @At("HEAD"), method = "isBucket(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
    private static void isSugar(ItemStack item, CallbackInfoReturnable<Boolean> cir) {
        if (item.getItem() == Items.SUGAR) { cir.setReturnValue(true); }
    }

    @Inject(at = @At("HEAD"), method = "getMaxItemCount(Lnet/minecraft/item/ItemStack;)I", cancellable = true)
    void sugarMax(ItemStack item, CallbackInfoReturnable<Integer> cir) {
        if (item.getItem() == Items.SUGAR) { cir.setReturnValue(64); }
    }
}
