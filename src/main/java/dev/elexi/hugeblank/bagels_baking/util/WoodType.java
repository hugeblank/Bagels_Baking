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
    private final Map<WoodBlock, Identifier> blocks = new HashMap<>();
    private final String variant;
    private final Item fruit;

    public WoodType(String variant, Item fruit) {
        this.variant = variant;
        this.fruit = fruit;
    }

    public Block getBlock(WoodBlock type) {
        return Registry.BLOCK.get(blocks.get(type));
    }

    public void init() {
        Block strippedLog = new BasicLogBlock();
        Block log = new BasicLogBlock(() -> strippedLog);
        Block strippedWood = new BasicLogBlock();
        Block wood = new BasicLogBlock(() -> strippedWood);

        SignType signType = SignTypeRegistry.register(variant);

        register(WoodBlock.LOG, log);
        register(WoodBlock.WOOD, wood);
        register(WoodBlock.STRIPPED_LOG, strippedLog);
        register(WoodBlock.STRIPPED_WOOD, strippedWood);
        register(WoodBlock.LEAVES, new BasicLeavesBlock(fruit));
        register(WoodBlock.PLANKS, new Block(FabricBlockSettings.copy(Blocks.OAK_PLANKS)));
        register(WoodBlock.PRESSURE_PLATE, new BasicPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copy(Blocks.OAK_PRESSURE_PLATE)));
        register(WoodBlock.BUTTON, new BasicWoodenButtonBlock(FabricBlockSettings.copy(Blocks.OAK_BUTTON)));
        register(WoodBlock.DOOR, new BasicDoorBlock(FabricBlockSettings.copy(Blocks.OAK_DOOR)));
        register(WoodBlock.FENCE_GATE, new FenceGateBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE_GATE)));
        register(WoodBlock.FENCE, new FenceBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE)));
        register(WoodBlock.SLAB, new SlabBlock(FabricBlockSettings.copy(Blocks.OAK_SLAB)));
        register(WoodBlock.STAIRS, new StairBlock(getBlock(WoodBlock.PLANKS).getDefaultState(), FabricBlockSettings.copy(Blocks.OAK_STAIRS)));
        register(WoodBlock.TRAPDOOR, new BasicTrapdoorBlock(FabricBlockSettings.copy(Blocks.OAK_TRAPDOOR)));
        register(WoodBlock.SIGN, new SignBlock(FabricBlockSettings.copy(Blocks.OAK_SIGN), signType));
        register(WoodBlock.WALL_SIGN, new WallSignBlock(FabricBlockSettings.copy(Blocks.OAK_WALL_SIGN).dropsLike(getBlock(WoodBlock.SIGN)), signType));
        register(WoodBlock.TRELLIS, new TrellisBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE).nonOpaque()));

        // Registering of all wood related things.
        Set<Map.Entry<WoodBlock, Identifier>> entries = blocks.entrySet();
        for (Map.Entry<WoodBlock, Identifier> entry : entries) {
            WoodBlock type = entry.getKey();
            Identifier id = entry.getValue();
            Block block = getBlock(type);

            Item item;
            switch (type) {
                case LOG, WOOD, STRIPPED_LOG, STRIPPED_WOOD, PLANKS, SLAB, STAIRS ->
                        item = new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
                case BUTTON, PRESSURE_PLATE ->
                        item = new BlockItem(block, new Item.Settings().group(ItemGroup.REDSTONE));
                case LEAVES, DOOR, TRAPDOOR, FENCE, FENCE_GATE, TRELLIS ->
                        item = new BlockItem(block, new Item.Settings().group(ItemGroup.DECORATIONS));
                case SIGN ->
                        item = new SignItem(new FabricItemSettings().group(ItemGroup.DECORATIONS).maxCount(16), getBlock(WoodBlock.SIGN), getBlock(WoodBlock.WALL_SIGN));
                default ->
                        item = null;
            }
            if (item != null) Registry.register(Registry.ITEM, id, item);
        }

        // Boat stuff. It's cray.
        BasicBoatRegistry.register(variant, getBlock(WoodBlock.PLANKS).asItem());

        // Make leaves compostable
        BakingCompostableItems.registerCompostableItem(0.3f, getBlock(WoodBlock.LEAVES));
    }

    private void register(WoodBlock key, Block block) {
        // Block registering & flammability is handled by WoodType
        blocks.put(key, key.register(variant, block));
    }
}