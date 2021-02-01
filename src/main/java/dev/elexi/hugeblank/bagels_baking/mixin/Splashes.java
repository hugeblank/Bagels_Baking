package dev.elexi.hugeblank.bagels_baking.mixin;

import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public class Splashes {

    @Final
    @Shadow
    private List<String> splashTexts;

    @Final
    @Shadow
    private Session field_18934;

    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I" ), method = "get()Ljava/lang/String;", cancellable = true)
    private void addSplashes(CallbackInfoReturnable<String> cir) {
        splashTexts.add("Corn Breaks Spacetime!");
        splashTexts.add("0 Rotten Tomatoes!");
        splashTexts.add("Bricks and Halite!");
        splashTexts.add("Better than Overwatch?");
        if (field_18934 != null) {
            if (field_18934.getUsername().equals("rwr")) {
                cir.setReturnValue("Subscribe to twitch.tv/hugeblank");
            } else if (field_18934.getUsername().equals("hugeblank")) {
                cir.setReturnValue("Subscribe to twitch.tv/rrricohu");
            }
        }
    }
}
