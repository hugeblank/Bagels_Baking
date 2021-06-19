package dev.elexi.hugeblank.bagels_baking.block;

import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;

public class BasicLogBlock {
    public static PillarBlock create(MapColor topMapColor, MapColor sideMapColor) {
        return new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, (state) ->
                state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor).strength(2.0F).sounds(BlockSoundGroup.WOOD)
        );
    }
}
