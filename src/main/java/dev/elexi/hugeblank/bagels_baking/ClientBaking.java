package dev.elexi.hugeblank.bagels_baking;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

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
    }
}
