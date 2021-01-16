package dev.elexi.hugeblank.bagels_baking.mixin.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ScreenHandler.class)
public abstract class UpdateSliceDamage {

    @Final
    @Shadow
    private DefaultedList<ItemStack> trackedStacks;
    List<ScreenHandlerListener> listeners;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;areEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"), method = "sendContentUpdates()V", locals = LocalCapture.CAPTURE_FAILHARD)
    public void sendContentUpdates(CallbackInfo ci, int j, ItemStack itemStack, ItemStack itemStack2) {
        if (itemStack.getItem() instanceof SwordItem) {
            ItemStack itemStack3 = itemStack.copy();
            this.trackedStacks.set(j, itemStack3);

            for (ScreenHandlerListener screenHandlerListener : this.listeners) {
                screenHandlerListener.onSlotUpdate((ScreenHandler) (Object) this, j, itemStack3);
            }
        }
    }

}
