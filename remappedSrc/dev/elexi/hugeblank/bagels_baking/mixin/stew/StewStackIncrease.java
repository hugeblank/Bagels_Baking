package dev.elexi.hugeblank.bagels_baking.mixin.stew;

import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Items.class)
public class StewStackIncrease {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void adjustStackSize(CallbackInfo ci) {
        ((IStewStackIncrease) Items.MUSHROOM_STEW).setMaxCount(16);
        ((IStewStackIncrease) Items.BEETROOT_SOUP).setMaxCount(16);
        ((IStewStackIncrease) Items.RABBIT_STEW).setMaxCount(16);
        ((IStewStackIncrease) Items.SUSPICIOUS_STEW).setMaxCount(16);
    }
}
