package dev.elexi.hugeblank.bagels_baking.mixin.render;

import dev.elexi.hugeblank.bagels_baking.entity.boat.BasicBoatEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoatEntityRenderer.class)
public class BoatEntityRendererMixin {
    @Inject(at = @At("HEAD"), method = "getTexture(Lnet/minecraft/entity/vehicle/BoatEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void getCustomTexture(BoatEntity boat, CallbackInfoReturnable<Identifier> cir) {
        if (boat instanceof BasicBoatEntity) {
            cir.setReturnValue(((BasicBoatEntity) boat).getSkin());
        }
    }

    // Pain.
    @ModifyVariable(at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/util/Pair;getSecond()Ljava/lang/Object;", ordinal = 0), method = "render(Lnet/minecraft/entity/vehicle/BoatEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    private Identifier applyCustomTexture(Identifier identifier, BoatEntity boatEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (boatEntity instanceof BasicBoatEntity) {
            return ((BasicBoatEntity) boatEntity).getSkin();
        }
        return identifier;
    }
}
