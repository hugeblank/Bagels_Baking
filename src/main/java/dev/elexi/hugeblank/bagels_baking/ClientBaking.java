package dev.elexi.hugeblank.bagels_baking;

import dev.elexi.hugeblank.bagels_baking.client.IceBoxScreen;
import dev.elexi.hugeblank.bagels_baking.client.MillScreen;
import dev.elexi.hugeblank.bagels_baking.network.BakingPackets;
import dev.elexi.hugeblank.bagels_baking.network.ClientPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

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
        BlockEntityRendererRegistry.INSTANCE.register(Baking.ICE_BOX_ENTITY_TYPE, ChestBlockEntityRenderer::new);



        ScreenRegistry.register(Baking.MILL_SCREEN, MillScreen::new);
        ScreenRegistry.register(Baking.ICE_BOX_9X3, IceBoxScreen::new);
        ScreenRegistry.register(Baking.ICE_BOX_9X6, IceBoxScreen::new);

        EntityRendererRegistry.INSTANCE.register(Baking.TOMATO_THROWABLE, FlyingItemEntityRenderer::new);

        ClientPacketHandler.register(BakingPackets.TOMATO_PACKET);
        ClientPacketHandler.register(BakingPackets.BOAT_PACKET);
    }
}
