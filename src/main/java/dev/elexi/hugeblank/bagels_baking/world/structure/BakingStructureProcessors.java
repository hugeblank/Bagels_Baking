package dev.elexi.hugeblank.bagels_baking.world.structure;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;

public class BakingStructureProcessors {
    public static final BlockIgnoreStructureProcessor IGNORE_STONE = new BlockIgnoreStructureProcessor(ImmutableList.of(Blocks.STONE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.GRAVEL, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.COAL_ORE));

    public static void init() {}
}
