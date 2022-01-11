package dev.elexi.hugeblank.bagels_baking.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

public class ServerPacketHandler {
    public static Packet<?> create(Entity e, Identifier packetID) {
        final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeVarInt(e.getId());
        buf.writeUuid(e.getUuid());
        buf.writeVarInt(Registry.ENTITY_TYPE.getRawId(e.getType()));
        buf.writeDouble(e.getX());
        buf.writeDouble(e.getY());
        buf.writeDouble(e.getZ());
        buf.writeByte(MathHelper.floor(e.getPitch() * 256.0F / 360.0F));
        buf.writeByte(MathHelper.floor(e.getYaw() * 256.0F / 360.0F));

        return ServerPlayNetworking.createS2CPacket(packetID, buf);
    }
}
