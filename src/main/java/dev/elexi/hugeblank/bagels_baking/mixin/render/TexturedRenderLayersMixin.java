package dev.elexi.hugeblank.bagels_baking.mixin.render;

import dev.elexi.hugeblank.bagels_baking.block.sign.SignTypeRegistry;
import dev.elexi.hugeblank.bagels_baking.sprite.SpriteRegistry;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.SignType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(TexturedRenderLayers.class)
public class TexturedRenderLayersMixin {
    @Inject(at = @At("TAIL"), method = "addDefaultTextures(Ljava/util/function/Consumer;)V")
    private static void injectDefaultTextures(Consumer<SpriteIdentifier> adder, CallbackInfo ci) {
        for (SpriteIdentifier sprite : SpriteRegistry.getRegistered()) {
            adder.accept(sprite);
        }
    }

    @Inject(at = @At("HEAD"), method = "getSignTextureId(Lnet/minecraft/util/SignType;)Lnet/minecraft/client/util/SpriteIdentifier;", cancellable = true)
    private static void signTextureOverride(SignType signType, CallbackInfoReturnable<SpriteIdentifier> cir) {
        if (signType instanceof SignTypeRegistry) {
            cir.setReturnValue(SignTypeRegistry.getTexture(signType.getName()));
        }
    }
}
