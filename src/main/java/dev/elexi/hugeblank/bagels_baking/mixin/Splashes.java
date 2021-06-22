package dev.elexi.hugeblank.bagels_baking.mixin;

import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(SplashTextResourceSupplier.class)
public class Splashes {

    @Final
    @Shadow
    private List<String> splashTexts;

    @Final
    @Shadow
    private static Random RANDOM;

    @Final
    @Shadow
    private Session session;

    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I" ), method = "get()Ljava/lang/String;", cancellable = true)
    private void addSplashes(CallbackInfoReturnable<String> cir) {
        splashTexts.add("Corn Breaks Spacetime!"); // Honoring Kori's texture dilemma
        splashTexts.add("0 Rotten Tomatoes!"); // Honoring tomatoes
        splashTexts.add("Bricks and Halite!"); // Honoring Hunter's Nieces
        splashTexts.add("Better than Overwatch?"); // Stating a truth
        splashTexts.add("i take a bite :)"); // Honoring doordash drivers
        splashTexts.add("Cups^2!"); // Honoring the cup duplication glitch
        splashTexts.add("Based on real pizza!"); // Honoring ChocolateFrog using a real pizza as a texture reference
        splashTexts.add("Mill does work!"); // Honoring v0.3 on the server
        splashTexts.add("kenibs!"); // Honoring that one time I misspelled lemon
        // Miscellaneous
        splashTexts.add("Now Catering!");
        splashTexts.add("We do not condone the eating of kiwi skin");
        splashTexts.add("Kroi can't tyep sommtines but that' sokya");
        splashTexts.add("Respect your food delivery drivers!");
        splashTexts.add("A la minecarte!");
        if (session != null && RANDOM.nextFloat() < 0.1) {
            switch (session.getUsername()) { // Surprises for my friends :)
                case "rwr":
                    cir.setReturnValue("Betreucia killed Broseph!");
                    return;
                case "hugeblank":
                    cir.setReturnValue("Subscribe to twitch.tv/hugeblank");
                    return;
                case "roger109z":
                    cir.setReturnValue("poger! :)");
                    return;
                case "Kori_A":
                    cir.setReturnValue("Korea? No, KoriA.");
                    return;
                case "He_Is_Man":
                    cir.setReturnValue("balright.");
                    return;
                case "NotEnoughStar":
                    cir.setReturnValue("Obamaphant moment");
                    return;
                case "Bu_dum_chhh":
                    cir.setReturnValue("Literally the best smash player, no cap.");
                    return;
                case "Pedrospeeder":
                    cir.setReturnValue("Next year is Mazdas year, swear.");
                    return;
                case "ChocolateFrog18":
                    cir.setReturnValue("Any British frogs?");
            }
        }
    }
}
