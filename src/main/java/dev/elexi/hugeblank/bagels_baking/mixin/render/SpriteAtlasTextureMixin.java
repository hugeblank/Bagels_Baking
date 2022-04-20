package dev.elexi.hugeblank.bagels_baking.mixin.render;

import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

@Mixin(SpriteAtlasTexture.class)
public class SpriteAtlasTextureMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 1), method = "method_18160", cancellable = true)
    private void cancelError(Identifier identifier, ResourceManager resourceManager, Queue queue, CallbackInfo ci) {
        // This is NOT A GOOD SOLUTION and I DON'T LIKE IT.
        if (identifier.getPath().contains("sign")) {
            ci.cancel();
        }
    }
}
