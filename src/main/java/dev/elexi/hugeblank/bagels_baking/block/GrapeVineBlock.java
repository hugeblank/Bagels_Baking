package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.Random;

public class GrapeVineBlock extends BasicVineComponentBlock {
    public static final int MAX_DISTANCE = 5;
    public static final IntProperty DISTANCE = IntProperty.of("distance", 0, MAX_DISTANCE);
    public static final BooleanProperty PERSISTENT = Properties.PERSISTENT;
    public static final IntProperty AGE = Properties.AGE_2;

    public GrapeVineBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(PERSISTENT, false).with(DISTANCE, 4));
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        int i = getDistanceFromStem(neighborState) + 1;
        if (i != 1 || state.get(DISTANCE) != i) {
            world.getBlockTickScheduler().schedule(pos, this, 1);
        }

        BlockState blockState = this.getPlacementShape(state, world, pos);
        return !this.hasAdjacentBlocks(blockState) ? Blocks.AIR.getDefaultState() : blockState;
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, updateDistanceFromStem(state, world, pos), 3);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(PERSISTENT)) {
            if (state.get(DISTANCE) == MAX_DISTANCE) {
                dropStacks(state, world, pos);
                world.removeBlock(pos, false);
            } else if (state.get(DISTANCE) < MAX_DISTANCE-1){
                Direction[] directions = Direction.values();
                ArrayList<Direction> facingDirections = getDirectionsFromState(state);
                if ((MAX_DISTANCE-state.get(DISTANCE))*world.random.nextFloat() < 0.5f) {
                    if (world.random.nextFloat() < 0.8f) {
                        for (Direction direction : directions) { // Grow Left/Right if block is air (propagation)
                            BlockPos growPos = pos.offset(direction);
                            BlockState growState = world.getBlockState(growPos);
                            for (Direction facing : facingDirections) {
                                if (direction.getAxis().isHorizontal() && growState.isOf(Blocks.AIR) && shouldConnectTo(world, growPos.offset(facing), facing) && random.nextFloat() < 0.5f) {
                                    world.setBlockState(growPos, this.getDefaultState().with(DISTANCE, state.get(DISTANCE) + 1).with(FACING_PROPERTIES.get(facing), true));
                                    return;
                                }
                            }
                        }
                    } else {
                        for (Direction facing : facingDirections) { // Attach onto new face (rotation)
                            Direction newFacing = world.random.nextInt(2) == 0 ? facing.rotateCounterclockwise(Direction.Axis.Y) : facing.rotateClockwise(Direction.Axis.Y);
                            BooleanProperty newFacingProperty = FACING_PROPERTIES.get(newFacing);
                            if (!state.get(newFacingProperty) && shouldConnectTo(world, pos.offset(newFacing), newFacing)) {
                                world.setBlockState(pos, state.with(newFacingProperty, true));
                                return;
                            }
                        }
                    }
                }
                BlockState below = world.getBlockState(pos.down());
                if (below.isOf(Baking.GRAPE_STEM)) {
                    Direction facing = getDirectionsFromState(below).get(0);
                    Direction left = facing.rotateClockwise(Direction.Axis.Y);
                    Direction right = facing.rotateCounterclockwise(Direction.Axis.Y);
                    if (world.getBlockState(pos.offset(left)).isOf(this) && world.getBlockState(pos.offset(right)).isOf(this) && random.nextFloat() < 0.5f) {
                        world.setBlockState(pos, below);
                    }
                }
                // TODO: Rare up/down offshoots
            }
            int age = state.get(AGE);
            if (world.random.nextInt(10) == 0 && age < 2 && state.get(DISTANCE) != 5) {
                world.setBlockState(pos, state.with(AGE, age+1));
            }
        }
        super.randomTick(state, world, pos, random);
    }

    private static BlockState updateDistanceFromStem(BlockState state, WorldAccess world, BlockPos pos) {
        int something = MAX_DISTANCE;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        Direction[] directions = Direction.values();

        for (Direction direction : directions) {
            mutable.set(pos, direction);
            something = Math.min(something, getDistanceFromStem(world.getBlockState(mutable))+1);
            if (something == 1) {
                break;
            }
        }

        return state.with(DISTANCE, something);
    }

    private static int getDistanceFromStem(BlockState state) {
        if (state.isOf(Baking.GRAPE_STEM)) {
            return 0;
        } else {
            return state.getBlock() instanceof GrapeVineBlock ? state.get(DISTANCE) : MAX_DISTANCE;
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(DISTANCE, PERSISTENT, AGE);
    }
}