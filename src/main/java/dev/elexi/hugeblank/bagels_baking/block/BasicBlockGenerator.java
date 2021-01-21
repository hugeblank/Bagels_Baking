package dev.elexi.hugeblank.bagels_baking.block;


import it.unimi.dsi.fastutil.Hash;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BasicBlockGenerator { // Mojang pls hire me - hugeblank
    private final ArrayList<String> names = new ArrayList<>();
    private final HashMap<String, Block> blocks = new HashMap<>();

    public BasicBlockGenerator(String name, Block base) {
        names.add(name);
        names.add(name.concat("_stairs"));
        names.add(name.concat("_slab"));
        names.add(name.concat("_wall"));
        blocks.put(name, base);
        blocks.put(names.get(1), new StairBlock(base.getDefaultState(), FabricBlockSettings.copy(base)));
        blocks.put(names.get(2), new SlabBlock(FabricBlockSettings.copy(base)));
        blocks.put(names.get(3), new WallBlock(FabricBlockSettings.copy(base)));
    }

    public void register(String namespace) {
        for (int i = 0; i < names.size(); i++) {
            Identifier id = new Identifier(namespace, names.get(i));
            Block block = blocks.get(names.get(i));
            Registry.register(Registry.BLOCK, id, block);
            Registry.register(Registry.ITEM, id, new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        }
    }

    public Block getBlock(String name) {
        if (blocks.containsKey(name)) {
            return blocks.get(name);
        }
        return null;
    }
}
