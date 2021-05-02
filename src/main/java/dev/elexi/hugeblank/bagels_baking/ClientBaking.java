package dev.elexi.hugeblank.bagels_baking;

import dev.elexi.hugeblank.bagels_baking.entity.TomatoEntity;
import dev.elexi.hugeblank.bagels_baking.network.TomatoSpawnPacket;
import dev.elexi.hugeblank.bagels_baking.screen.MillScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class ClientBaking implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.HALITE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.HALITE_STAIR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.HALITE_SLAB, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.HALITE_WALL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.POLISHED_HALITE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.POLISHED_HALITE_STAIR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.POLISHED_HALITE_SLAB, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.POLISHED_HALITE_WALL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.COFFEE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.TEA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.TOMATO_PLANT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.CORN_STALK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.RICE_PLANT, RenderLayer.getCutout());

        ScreenRegistry.register(Baking.MILL_SCREEN, MillScreen::new);

        EntityRendererRegistry.INSTANCE.register(Baking.TOMATO_THROWABLE, (dispatcher, context) ->
                new FlyingItemEntityRenderer(dispatcher, context.getItemRenderer()));
        receiveEntityPacket();
    }

    public void receiveEntityPacket() {
        ClientSidePacketRegistry.INSTANCE.register(Baking.TOMATO_PACKET, (ctx, byteBuf) -> {
            UUID uuid = byteBuf.readUuid();
            int entityId = byteBuf.readVarInt();
            Vec3d pos = TomatoSpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = TomatoSpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = TomatoSpawnPacket.PacketBufUtil.readAngle(byteBuf);
            ctx.getTaskQueue().execute(() -> {
                if (MinecraftClient.getInstance().world == null)
                    throw new IllegalStateException("Tried to spawn entity in a null world!");
                Entity e = new TomatoEntity(MinecraftClient.getInstance().world, pos.x, pos.y, pos.z);
                e.updateTrackedPosition(pos);
                e.setPos(pos.x, pos.y, pos.z);
                e.pitch = pitch;
                e.yaw = yaw;
                e.setEntityId(entityId);
                e.setUuid(uuid);
                MinecraftClient.getInstance().world.addEntity(entityId, e);
            });
        });
    }
}
