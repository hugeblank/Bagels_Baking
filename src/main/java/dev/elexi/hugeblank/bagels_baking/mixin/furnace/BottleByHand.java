package dev.elexi.hugeblank.bagels_baking.mixin.furnace;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.FurnaceFuelSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceFuelSlot.class)
public class BottleByHand {
    @Inject(at = @At("HEAD"), method = "canInsert(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
    public void canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() == Items.GLASS_BOTTLE) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "getMaxItemCount(Lnet/minecraft/item/ItemStack;)I", cancellable = true)
    public void getMaxItemInfo(ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        if (itemStack.getItem() == Items.GLASS_BOTTLE) {
            cir.setReturnValue(1);
        }
    }
}
