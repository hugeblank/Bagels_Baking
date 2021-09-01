package dev.elexi.hugeblank.bagels_baking.mixin.render;

import dev.elexi.hugeblank.bagels_baking.ClientBaking;
import dev.elexi.hugeblank.bagels_baking.block.entity.IceBoxBlockEntity;
import dev.elexi.hugeblank.bagels_baking.block.type.SignTypeRegistry;
import dev.elexi.hugeblank.bagels_baking.sprite.SpriteRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
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

    @Inject(at = @At("HEAD"), method = "getChestTexture(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/block/enums/ChestType;Z)Lnet/minecraft/client/util/SpriteIdentifier;", cancellable = true)
    private static void addIceBoxTexture(BlockEntity blockEntity, ChestType type, boolean christmas, CallbackInfoReturnable<SpriteIdentifier> cir) {
        if (blockEntity instanceof IceBoxBlockEntity) {
            switch(type) {
                case LEFT:
                    cir.setReturnValue(ClientBaking.ICE_BOX_LEFT);
                case RIGHT:
                    cir.setReturnValue(ClientBaking.ICE_BOX_RIGHT);
                case SINGLE:
                default:
                    cir.setReturnValue(ClientBaking.ICE_BOX_NORMAL);
            }
        }
    }
}
