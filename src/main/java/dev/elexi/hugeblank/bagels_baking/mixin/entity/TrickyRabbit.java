package dev.elexi.hugeblank.bagels_baking.mixin.entity;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RabbitEntity.class)
public class TrickyRabbit extends AnimalEntity {

    @Final
    @Shadow
    private static TrackedData<Integer> RABBIT_TYPE;

    int cerealDropTime;
    private static final Item[] cereals = {Baking.CORN_CEREAL, Baking.RICE_CEREAL, Baking.WHEAT_CEREAL};

    protected TrickyRabbit(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        cerealDropTime = this.random.nextInt(12000) + 12000;
    }

    @Inject(at = @At("TAIL"), method = "tickMovement()V")
    private void trickMovement(CallbackInfo ci) {
        if (!this.world.isClient && this.isAlive() && !this.isBaby() && --cerealDropTime <= 0 && this.hasCustomName() && "Tricks".equals(this.getCustomName().asString())) {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(cereals[this.random.nextInt(cereals.length)]);
            cerealDropTime = this.random.nextInt(12000) + 12000;
        }
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V")
    public void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("CerealDropTime")) {
            cerealDropTime = tag.getInt("CerealDropTime");
        }

    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V")
    public void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {
        tag.putInt("CerealDropTime", cerealDropTime);
    }


    // Everything below this is just so I can access "this"... :I
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        RabbitEntity rabbitEntity = EntityType.RABBIT.create(serverWorld);
        int i = this.chooseType(serverWorld);
        if (this.random.nextInt(20) != 0) {
            if (passiveEntity instanceof RabbitEntity && this.random.nextBoolean()) {
                i = ((RabbitEntity)passiveEntity).getRabbitType();
            } else {
                i = this.getRabbitType();
            }
        }

        rabbitEntity.setRabbitType(i);
        return rabbitEntity;
    }

    private int chooseType(WorldAccess world) {
        Biome biome = world.getBiome(this.getBlockPos());
        int i = this.random.nextInt(100);
        if (biome.getPrecipitation() == Biome.Precipitation.SNOW) {
            return i < 80 ? 1 : 3;
        } else if (biome.getCategory() == Biome.Category.DESERT) {
            return 4;
        } else {
            return i < 50 ? 0 : (i < 90 ? 5 : 2);
        }
    }

    public int getRabbitType() {
        return this.dataTracker.get(RABBIT_TYPE);
    }
}
