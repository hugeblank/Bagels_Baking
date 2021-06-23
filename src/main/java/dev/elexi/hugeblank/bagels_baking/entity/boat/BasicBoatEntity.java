package dev.elexi.hugeblank.bagels_baking.entity.boat;

import dev.elexi.hugeblank.bagels_baking.network.BakingPackets;
import dev.elexi.hugeblank.bagels_baking.network.ServerPacketHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

// A majority of this was plucked from Terraform API (https://github.com/TerraformersMC/Terraform/)
// I love those people, check them out <3
public class BasicBoatEntity extends BoatEntity {
    private final BasicBoat boat;

    public BasicBoatEntity(EntityType<? extends BoatEntity> entityType, World world, BasicBoat boat) {
        super(entityType, world);
        this.boat = boat;
    }

    public Identifier getSkin() {
        return boat.getSkin();
    }

    public Item asItem() {
        return boat.asItem();
    }

    public Item asPlanks() {
        return boat.asPlanks();
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void fall(double distance, boolean onGround, BlockState state, BlockPos pos) {

        float savedFallDistance = this.fallDistance;

        // Run other logic, including setting the private field fallVelocity
        super.fall(distance, false, state, pos);

        if (!this.hasVehicle() && onGround) {
            this.fallDistance = savedFallDistance;

            if (this.fallDistance > 3.0F) {
                if (!isOnLand()) {
                    this.fallDistance = 0.0F;
                    return;
                }

                this.handleFallDamage(this.getSafeFallDistance(), 1.0F, DamageSource.FALL);
                if (!this.world.isClient && !this.isRemoved()) {
                    this.kill();
                    if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                        for (int i = 0; i < 3; i++) {
                            this.dropItem(this.asPlanks());
                        }

                        for (int i = 0; i < 2; i++) {
                            this.dropItem(Items.STICK);
                        }
                    }
                }
            }

            this.fallDistance = 0.0F;
        }
    }

    private boolean isOnLand() {
        return true;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return ServerPacketHandler.create(this, BakingPackets.BOAT_PACKET);
    }

    @Override
    public void setBoatType(BoatEntity.Type type) {
        throw new UnsupportedOperationException("Tried to set the boat type of a Bagels Baking boat");
    }

    @Override
    public BoatEntity.Type getBoatType() {
        return boat.getType();
    }

}
