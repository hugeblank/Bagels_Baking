package dev.elexi.hugeblank.bagels_baking.world.gen.placer;

import com.mojang.serialization.Codec;
import dev.elexi.hugeblank.bagels_baking.block.TriplePlantBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.placer.BlockPlacer;
import net.minecraft.world.gen.placer.BlockPlacerType;

import java.util.Random;

public class TriplePlantPlacer extends BlockPlacer {
    public static final TriplePlantPlacer INSTANCE = new TriplePlantPlacer();
    public static final Codec<TriplePlantPlacer> CODEC = (Codec.unit(() -> INSTANCE));

    protected BlockPlacerType<?> getType() {
        return BakingPlacers.TRIPLE_BLOCK_PLACER_TYPE;
    }

    public void generate(WorldAccess world, BlockPos pos, BlockState state, Random random) {
        TriplePlantBlock.placeAt(world, state, pos, 2);
    }
}
