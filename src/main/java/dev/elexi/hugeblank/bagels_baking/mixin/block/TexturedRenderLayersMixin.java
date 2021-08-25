package dev.elexi.hugeblank.bagels_baking.mixin.block;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.block.entity.IceBoxBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TexturedRenderLayers.class)
public class TexturedRenderLayersMixin {

    private static final SpriteIdentifier ICE_BOX_NORMAL = new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, new Identifier(Baking.ID,"entity/ice_box"));
    private static final SpriteIdentifier ICE_BOX_LEFT = new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, new Identifier(Baking.ID,"entity/ice_box_left"));
    private static final SpriteIdentifier ICE_BOX_RIGHT = new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, new Identifier(Baking.ID,"entity/ice_box_right"));

    @Inject(at = @At("HEAD"), method = "getChestTexture(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/block/enums/ChestType;Z)Lnet/minecraft/client/util/SpriteIdentifier;", cancellable = true)
    private static void addIceBoxTexture(BlockEntity blockEntity, ChestType type, boolean christmas, CallbackInfoReturnable<SpriteIdentifier> cir) {
        if (blockEntity instanceof IceBoxBlockEntity) {
            switch(type) {
                case LEFT:
                    cir.setReturnValue(ICE_BOX_LEFT);
                case RIGHT:
                    cir.setReturnValue(ICE_BOX_RIGHT);
                case SINGLE:
                default:
                    cir.setReturnValue(ICE_BOX_NORMAL);
            }
        }
    }
}
