package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.util.AdjacentPosition;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.ArrayList;
import java.util.Random;

public class GrapeStemBlock extends GrapeVineComponentBlock {
    public GrapeStemBlock(Settings settings) {
        super(settings);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return canPlaceAt(state, world, pos) ? super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos) : Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Block below = world.getBlockState(pos.down()).getBlock();
        return below instanceof GrassBlock || below instanceof FarmlandBlock || below instanceof GrapeStemBlock;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockState above = world.getBlockState(pos.up());
        ArrayList<Direction> directions = getDirectionsFromState(state);
        if (directions.size() == 0) return;
        Direction direction = directions.get(random.nextInt(directions.size()));
        if (shouldHaveSide(world, pos.up(), direction) && above.getBlock() instanceof AirBlock && random.nextFloat() < 0.5f) {
                world.setBlockState(pos.up(), Baking.GRAPE_VINE.getDefaultState().with(FACING_PROPERTIES.get(direction), true).with(GrapeVineBlock.DISTANCE, 1));
        } else if (above.getBlock() instanceof GrapeVineBlock && random.nextFloat() < 0.5f) { // Become stem if viable (conversion)
            BlockState stem = this.getDefaultState();
            if (above.get(ADJACENT) == AdjacentPosition.BOTH) {
                if (above.get(GrapeVineBlock.AGE) == 2) {
                    GrapeVineBlock.dropItems(world, pos.up());
                }
                ArrayList<Direction> aboveDirs = getDirectionsFromState(above);
                for (Direction facing : aboveDirs) {
                    stem = stem.with(FACING_PROPERTIES.get(facing), true);
                }
                world.setBlockState(pos.up(), stem.with(ADJACENT, above.get(ADJACENT)));
            }
        }
    }
}
