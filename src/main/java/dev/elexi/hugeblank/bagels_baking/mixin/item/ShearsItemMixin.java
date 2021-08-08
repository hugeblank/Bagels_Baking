package dev.elexi.hugeblank.bagels_baking.mixin.item;

import dev.elexi.hugeblank.bagels_baking.block.BasicVineComponentBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {
    @Inject(at = @At("HEAD"), method = "getMiningSpeedMultiplier(Lnet/minecraft/item/ItemStack;Lnet/minecraft/block/BlockState;)F", cancellable = true)
    private void grapeVineSpeedMultiplier(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        if (state.getBlock() instanceof BasicVineComponentBlock) {
            cir.setReturnValue(2.0f);
        }
    }
}
