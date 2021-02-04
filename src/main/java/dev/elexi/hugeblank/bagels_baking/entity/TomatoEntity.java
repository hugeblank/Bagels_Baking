package dev.elexi.hugeblank.bagels_baking.entity;


import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.network.TomatoSpawnPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class TomatoEntity extends ThrownItemEntity {

    public TomatoEntity(EntityType<? extends TomatoEntity> entityType, World world) {
        super(entityType, world);
    }

    public TomatoEntity(World world, LivingEntity owner) {
        super(Baking.TOMATO_THROWABLE, owner, world);
    }

    public TomatoEntity(World world, double x, double y, double z) {
        super(Baking.TOMATO_THROWABLE, x, y, z, world);
    }

    @Override
    public Packet createSpawnPacket() {
        return TomatoSpawnPacket.create(this, Baking.TOMATO_PACKET);
    }

    @Override
    protected Item getDefaultItem() {
        return Baking.TOMATO;
    }

    @Environment(EnvType.CLIENT)
    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = new ItemStack(this.getDefaultItem());
        return new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack);
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), 0.25f);
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, (byte)3);
            this.remove();
        } else {
            ((ClientWorld)this.world).playSound(this.getBlockPos(), SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.NEUTRAL, 1.0f, 1.0f, true);
        }

    }
}