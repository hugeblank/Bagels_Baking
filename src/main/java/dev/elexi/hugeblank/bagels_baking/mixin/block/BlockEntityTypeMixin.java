package dev.elexi.hugeblank.bagels_baking.mixin.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
    @Inject(at = @At("HEAD"), method = "supports(Lnet/minecraft/block/BlockState;)Z", cancellable = true)
    private void forceSupport(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        Block sign = state.getBlock();
        if (BlockEntityType.SIGN.equals(this) && sign instanceof SignBlock || sign instanceof WallSignBlock) {
            cir.setReturnValue(true);
        }
    }
}
