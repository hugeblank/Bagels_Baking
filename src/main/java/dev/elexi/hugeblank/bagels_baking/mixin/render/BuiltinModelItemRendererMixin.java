package dev.elexi.hugeblank.bagels_baking.mixin.render;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.block.entity.IceBoxBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

    @Final
    @Shadow
    private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    private final IceBoxBlockEntity iceBoxBlockEntity = new IceBoxBlockEntity(BlockPos.ORIGIN, Baking.ICE_BOX.getDefaultState());

    @Inject(at = @At(value = "RETURN", ordinal = 1), method = "render(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V")
    private void renderIceBoxItem(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (stack.isOf(Baking.ICE_BOX_ITEM)) {
            blockEntityRenderDispatcher.renderEntity(iceBoxBlockEntity, matrices, vertexConsumers, light, overlay);
        }
    }
}
