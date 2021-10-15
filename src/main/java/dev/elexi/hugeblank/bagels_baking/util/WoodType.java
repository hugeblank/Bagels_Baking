package dev.elexi.hugeblank.bagels_baking.util;

import dev.elexi.hugeblank.bagels_baking.block.*;
import dev.elexi.hugeblank.bagels_baking.block.type.SignTypeRegistry;
import dev.elexi.hugeblank.bagels_baking.entity.boat.BasicBoatRegistry;
import dev.elexi.hugeblank.bagels_baking.item.BakingCompostableItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SignItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// A single type of wood (ex: lemon vs. cherry). Not to be confused with the WoodBlock enum.
public class WoodType {
    private static final AbstractBlock.Settings settings = FabricBlockSettings.copy(Blocks.OAK_PLANKS);
    private final Map<WoodBlock, Block> blocks = new HashMap<>();
    private final String variant;

    public WoodType(String variant, Item fruit) {
        this.variant = variant;
        Block strippedLog = new BasicLogBlock();
        Block log = new BasicLogBlock(() -> strippedLog);
        Block strippedWood = new BasicLogBlock();
        Block wood = new BasicLogBlock(() -> strippedWood);

        SignType type = SignTypeRegistry.register(variant);

        blocks.put(WoodBlock.LOG, log);
        blocks.put(WoodBlock.WOOD, wood);
        blocks.put(WoodBlock.STRIPPED_LOG, strippedLog);
        blocks.put(WoodBlock.STRIPPED_WOOD, strippedWood);
        blocks.put(WoodBlock.LEAVES, new BasicLeavesBlock(fruit));
        blocks.put(WoodBlock.PLANKS, new Block(settings));
        blocks.put(WoodBlock.PRESSURE_PLATE, new BasicPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copy(Blocks.OAK_PRESSURE_PLATE)));
        blocks.put(WoodBlock.BUTTON, new BasicWoodenButtonBlock(settings));
        blocks.put(WoodBlock.DOOR, new BasicDoorBlock(settings));
        blocks.put(WoodBlock.FENCE_GATE, new FenceGateBlock(settings));
        blocks.put(WoodBlock.FENCE, new FenceBlock(settings));
        blocks.put(WoodBlock.SLAB, new SlabBlock(settings));
        blocks.put(WoodBlock.STAIRS, new StairBlock(log.getDefaultState(), settings));
        blocks.put(WoodBlock.TRAPDOOR, new BasicTrapdoorBlock(settings));
        blocks.put(WoodBlock.SIGN, new SignBlock(FabricBlockSettings.copy(Blocks.OAK_SIGN), type));
        blocks.put(WoodBlock.WALL_SIGN, new WallSignBlock(FabricBlockSettings.copy(Blocks.OAK_SIGN), type));
        blocks.put(WoodBlock.TRELLIS, new TrellisBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE)));

        BakingCompostableItems.registerCompostableItem(0.3f, getBlock(WoodBlock.LEAVES));
        BakingCompostableItems.registerCompostableItem(0.65f, fruit);
    }

    public Block getBlock(WoodBlock type) {
        return blocks.get(type);
    }

    public void init() {

        // Registering of all wood related things.
        Set<Map.Entry<WoodBlock, Block>> entries = blocks.entrySet();
        for (Map.Entry<WoodBlock, Block> entry : entries) {
            WoodBlock type = entry.getKey();
            Block block = entry.getValue();

            // Block registering & flammability is handled by WoodType
            Identifier id = type.registerBlock(variant, block);

            Item item;
            switch (type) {
                case LOG, WOOD, STRIPPED_LOG, STRIPPED_WOOD, PLANKS, SLAB, STAIRS ->
                        item = new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
                case BUTTON, PRESSURE_PLATE ->
                        item = new BlockItem(block, new Item.Settings().group(ItemGroup.REDSTONE));
                case LEAVES, DOOR, TRAPDOOR, FENCE, FENCE_GATE, TRELLIS ->
                        item = new BlockItem(block, new Item.Settings().group(ItemGroup.DECORATIONS));
                case SIGN ->
                        item = new SignItem(new FabricItemSettings().group(ItemGroup.DECORATIONS).maxCount(16), blocks.get(WoodBlock.SIGN), blocks.get(WoodBlock.WALL_SIGN));
                default ->
                        item = null;
            }
            if (item != null) Registry.register(Registry.ITEM, id, item);
        }

        // Boat stuff. It's cray.
        BasicBoatRegistry.register(variant, blocks.get(WoodBlock.PLANKS).asItem());

    }
}