package dev.elexi.hugeblank.bagels_baking.mixin.entity.passive;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(RabbitEntity.class)
public class TrickyRabbit {

    int cerealDropTime;
    private static final Item[] cereals = {Baking.CORN_CEREAL, Baking.RICE_CEREAL, Baking.WHEAT_CEREAL};

    @Inject(at = @At("TAIL"), method = "tickMovement()V")
    private void trickMovement(CallbackInfo ci) {
        RabbitEntity rabbit = ((RabbitEntity)(Object)this);

        if (!rabbit.world.isClient && rabbit.isAlive() && !rabbit.isBaby() && --cerealDropTime <= 0 && rabbit.hasCustomName() && "Tricks".equals(rabbit.getCustomName().asString())) {
            Random random = rabbit.world.getRandom();
            rabbit.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
            rabbit.dropItem(cereals[random.nextInt(cereals.length)]);
            cerealDropTime = random.nextInt(12000) + 12000;
        }
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V")
    public void readCustomDataFromTag(NbtCompound tag, CallbackInfo ci) {
        if (tag.contains("CerealDropTime")) {
            cerealDropTime = tag.getInt("CerealDropTime");
        }

    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V")
    public void writeCustomDataToTag(NbtCompound tag, CallbackInfo ci) {
        tag.putInt("CerealDropTime", cerealDropTime);
    }

}
