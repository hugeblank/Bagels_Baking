package dev.elexi.hugeblank.bagels_baking;

import dev.elexi.hugeblank.bagels_baking.client.IceBoxScreen;
import dev.elexi.hugeblank.bagels_baking.client.MillScreen;
import dev.elexi.hugeblank.bagels_baking.network.BakingPackets;
import dev.elexi.hugeblank.bagels_baking.network.ClientPacketHandler;
import dev.elexi.hugeblank.bagels_baking.sprite.SpriteRegistry;
import dev.elexi.hugeblank.bagels_baking.util.WoodBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

public class ClientBaking implements ClientModInitializer {

    public static final SpriteIdentifier ICE_BOX_NORMAL = new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, new Identifier(Baking.ID,"entity/ice_box"));
    public static final SpriteIdentifier ICE_BOX_LEFT = new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, new Identifier(Baking.ID,"entity/ice_box_left"));
    public static final SpriteIdentifier ICE_BOX_RIGHT = new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, new Identifier(Baking.ID,"entity/ice_box_right"));

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
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.CINNAMON_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.CINNAMON_TREE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.SMALL_CINNAMON_TREE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.GRAPE_STEM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.GRAPE_VINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.LEMON_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.CHERRY_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.JUNIPER_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.POTTED_LEMON_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.POTTED_CHERRY_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.POTTED_JUNIPER_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.LEMON_WOOD_TYPE.getBlock(WoodBlock.TRAPDOOR), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.CHERRY_WOOD_TYPE.getBlock(WoodBlock.TRAPDOOR), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.JUNIPER_WOOD_TYPE.getBlock(WoodBlock.TRAPDOOR), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.LEMON_WOOD_TYPE.getBlock(WoodBlock.DOOR), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.CHERRY_WOOD_TYPE.getBlock(WoodBlock.DOOR), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Baking.JUNIPER_WOOD_TYPE.getBlock(WoodBlock.DOOR), RenderLayer.getCutout());

        SpriteRegistry.register(ICE_BOX_NORMAL);
        SpriteRegistry.register(ICE_BOX_LEFT);
        SpriteRegistry.register(ICE_BOX_RIGHT);

        BlockEntityRendererRegistry.INSTANCE.register(Baking.ICE_BOX_ENTITY_TYPE, ChestBlockEntityRenderer::new);

        ScreenRegistry.register(Baking.MILL_SCREEN, MillScreen::new);
        ScreenRegistry.register(Baking.ICE_BOX_9X3, IceBoxScreen::new);
        ScreenRegistry.register(Baking.ICE_BOX_9X6, IceBoxScreen::new);

        EntityRendererRegistry.INSTANCE.register(Baking.TOMATO_THROWABLE, FlyingItemEntityRenderer::new);

        ClientPacketHandler.register(BakingPackets.TOMATO_PACKET);
        ClientPacketHandler.register(BakingPackets.BOAT_PACKET);
    }
}
