package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.Random;

public class GrapeStemBlock extends BasicVineComponentBlock {
    public GrapeStemBlock(Settings settings) {
        super(settings);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (neighborPos.equals(pos.down())) {
            Block block = neighborState.getBlock();
            return (block instanceof GrassBlock || block instanceof FarmlandBlock || block instanceof GrapeStemBlock) ? state : Blocks.AIR.getDefaultState();
        } else {
            return state;
        }
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Block above = world.getBlockState(pos.up()).getBlock();
        Direction direction = getDirectionsFromState(state).get(0);
        if (shouldConnectTo(world, pos.offset(direction).up(), direction)) {
            if (above instanceof GrapeVineBlock) {
                if (random.nextFloat() < 0.5f) {
                    world.setBlockState(pos.up(), state);
                }
            } else if (above instanceof AirBlock) {
                //if (random.nextFloat() < 0.25f) {
                BlockState vine = Baking.GRAPE_VINE.getDefaultState().with(FACING_PROPERTIES.get(direction), true).with(GrapeVineBlock.DISTANCE, 0);
                world.setBlockState(pos.up(), vine);
                //}
            }
        }
    }
}
