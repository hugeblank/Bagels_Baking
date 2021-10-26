package dev.elexi.hugeblank.bagels_baking.mixin.client;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.advancement.Advancement;
import net.minecraft.client.toast.AdvancementToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AdvancementToast.class)
public class AdvancementToastMixin {
    // Arbitrary integer used for id
    private static final Toast.Visibility GOLDEN_BAGEL_VISIBILITY = VisibilityAccessor.createVisibility("golden_bagel", Baking.GOLDEN_BAGEL.hashCode(), Baking.GOLDEN_BAGEL_ADVANCEMENT);

    @Shadow
    @Final
    private Advancement advancement;

    @Inject(at = @At(value = "RETURN", ordinal = 0), method = "draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/toast/ToastManager;J)Lnet/minecraft/client/toast/Toast$Visibility;", cancellable = true)
    private void redirectSound(MatrixStack matrices, ToastManager manager, long startTime, CallbackInfoReturnable<Toast.Visibility> cir) {
        if (advancement.getId().equals(new Identifier(Baking.ID, "bagels_baking/joshemve_meal")) && startTime < 5000L) {
            cir.setReturnValue(GOLDEN_BAGEL_VISIBILITY);
        }
    }
}
