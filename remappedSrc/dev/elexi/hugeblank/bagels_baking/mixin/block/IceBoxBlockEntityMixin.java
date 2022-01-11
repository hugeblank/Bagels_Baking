package dev.elexi.hugeblank.bagels_baking.mixin.block;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChestBlockEntity.class)
public class IceBoxBlockEntityMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"), method = "playSound(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/sound/SoundEvent;)V", locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private static void makeSound(World world, BlockPos pos, BlockState state, SoundEvent soundEvent, CallbackInfo ci, ChestType chestType, double d, double e, double f) {
        if (state.getBlock() == Baking.ICE_BOX) {
            soundEvent = (soundEvent == SoundEvents.BLOCK_CHEST_OPEN) ? Baking.ICE_BOX_OPEN : Baking.ICE_BOX_CLOSE;
            world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
            ci.cancel();
        }
    }
}
