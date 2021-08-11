package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.ArrayList;
import java.util.Random;

public class GrapeStemBlock extends BasicVineComponentBlock {
    public GrapeStemBlock(Settings settings) {
        super(settings);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return canPlaceAt(state, world, pos) ? state : Blocks.AIR.getDefaultState();
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
        if (shouldConnectTo(world, pos.offset(direction).up(), direction) && above.getBlock() instanceof AirBlock && random.nextFloat() < 0.5f) {
                world.setBlockState(pos.up(), Baking.GRAPE_VINE.getDefaultState().with(FACING_PROPERTIES.get(direction), true).with(GrapeVineBlock.DISTANCE, 1));
        } else if (above.getBlock() instanceof GrapeVineBlock && random.nextFloat() < 0.5f) { // Become stem if viable (conversion)
            BlockState stem = getDefaultState();
            pos = pos.up(); // Shift focus to vine block above
            ArrayList<Direction> facings = getDirectionsFromState(world.getBlockState(pos));
            int adjacentVines = 0;
            for (Direction facing : facings) {
                BooleanProperty facingProperty = FACING_PROPERTIES.get(facing);
                stem = stem.with(facingProperty, true);
                BlockState left = world.getBlockState(pos.offset(facing.rotateClockwise(Direction.Axis.Y)));
                BlockState right = world.getBlockState(pos.offset(facing.rotateCounterclockwise(Direction.Axis.Y)));
                // Make sure either the left or right block matches, isn't persistent, and is on the same wall that we are
                if (left.getBlock() instanceof GrapeVineBlock && !left.get(GrapeVineBlock.PERSISTENT) && left.get(facingProperty)) {
                    adjacentVines++;
                }
                if (right.getBlock() instanceof GrapeVineBlock && !right.get(GrapeVineBlock.PERSISTENT) && right.get(facingProperty)) {
                    adjacentVines++;
                }
            }
            if (adjacentVines > 1) {
                if (above.get(GrapeVineBlock.AGE) == 2) {
                    GrapeVineBlock.dropItems(world, pos);
                }
                world.setBlockState(pos, stem);
            }
        }
    }
}
