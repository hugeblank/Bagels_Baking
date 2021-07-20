package dev.elexi.hugeblank.bagels_baking.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class GrapeVineBlock extends BasicVineComponentBlock {
    public GrapeVineBlock(Settings settings) {
        super(settings);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        BlockState blockState = this.getPlacementShape(state, world, pos);
        return !this.hasAdjacentBlocks(blockState) ? Blocks.AIR.getDefaultState() : blockState;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {

    }
}
