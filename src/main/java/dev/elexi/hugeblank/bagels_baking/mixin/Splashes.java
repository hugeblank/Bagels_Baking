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
        splashTexts.add("Bricks and Halite!"); // Honoring rrricohu's Nieces
        splashTexts.add("Better than Overwatch?"); // Stating a truth
        splashTexts.add("i take a bite :)"); // Honoring doordash drivers
        splashTexts.add("Cups^2!"); // Honoring the cup duplication glitch
        splashTexts.add("Based on real pizza!"); // Honoring ChocolateFrog using a real pizza as a texture reference
        splashTexts.add("Mill does work!"); // Honoring v0.3 on the server
        splashTexts.add("kenibs!"); // Honoring that one time I misspelled lemon
        splashTexts.add("We do not condone the eating of kiwi skin"); // Honoring Sammi's bad habits
        splashTexts.add("Kroi can't tyep sommtines but that' sokya"); // Honoring Kori's typing prowess
        splashTexts.add("My width is   "); // https://cdn.discordapp.com/attachments/676495817967665192/878052107222126662/alice0135a03.png
        splashTexts.add("Yea that thing"); // Kori not knowing what splashes are
        splashTexts.add("When Life gives you Lemons, cry."); // Don't fukcing ask
        splashTexts.add("Cellar? I barely know her!"); // hahaaa i'm hilarious
        splashTexts.add("I get paid to type these, you know."); // A lie
        splashTexts.add("OwO"); // you can't stop me
        splashTexts.add("Pray for Malcolm. He's not hurt or anything, he just worries me sometimes."); // Shoutout to Connor's brother
        splashTexts.add("That's not an entity, that's my wife!"); // I despise myself- Kori
        splashTexts.add("Cameo"); // ;)
        splashTexts.add("Personally, I think we need more pickle recipes."); // pikcle
        splashTexts.add("I bet lemon trees taste like splinters."); // Prove me wrong
        splashTexts.add("Hot Single Pockets in your area!"); // i'm so lonely bro
        splashTexts.add("This one's for you, Left Shark!"); // Left Shark I love you
        splashTexts.add("Kori doesn't know STEM. Why is Kori making stems??"); // Kori is sad and confused and also afraid
        splashTexts.add("Can't have schist in 1.17 :("); // well we don't, do we?
        splashTexts.add("Never put Minecraft on a resume"); // I put Bagel's Baking on my resume KEKW
        splashTexts.add("Semi-ethically sourced!"); // idk lol
        splashTexts.add("MORTY I TURNED MYSELF INTO A SPLASHTEXT"); // funniest shit i ever seen
        splashTexts.add("Too much bees"); // Turn down the bees. Too much.
        splashTexts.add("roger roger"); // roger roger
        splashTexts.add("pspsppspspspspspspsp @hugeblank"); // The now-infamous summoning call
        splashTexts.add("JAPPA my beloved"); // o7
        splashTexts.add("Icebox is actually just one word, usually."); // trivia!
        splashTexts.add("Phantoms just need a hug"); // propaganda
        splashTexts.add("WE HAVE STICKERS"); // he has stickers
        splashTexts.add("https://ibb.co/DMqwf0y if you dare"); // Togg said no Word Documents
        splashTexts.add("oniones"); // Todd made a find/replace resourcepack script that converted the word potato to onion. When it came across "potatoes"...
        // Miscellaneous
        splashTexts.add("Now Catering!");
        splashTexts.add("Respect your food delivery drivers!");
        splashTexts.add("A la minecarte!");
        splashTexts.add("Going to BlanketCon!");
        splashTexts.add("Standing with Ukraine");
        splashTexts.add("Craft Singles?");
        if (session != null && RANDOM.nextFloat() < 0.1) {
            switch (session.getUsername()) { // Surprises for people that inspired this mod
                case "hugeblank" -> cir.setReturnValue("Subscribe to twitch.tv/hugeblank");
                case "roger109z" -> cir.setReturnValue("poger! :)");
                case "Kori_A" -> cir.setReturnValue("Korea? No, Kori_A.");
                case "He_Is_Man" -> cir.setReturnValue("balright.");
                case "NotEnoughStar" -> cir.setReturnValue("Obamaphant moment");
                case "Pedrospeeder" -> cir.setReturnValue("Next year is Mazdas year, swear.");
                case "ChocolateFrog18" -> cir.setReturnValue("Any British frogs?");
                case "ZagXC" -> cir.setReturnValue("Connor help I'm trapped in the menu screen please get me out of here");
            }
        }
    }
}
