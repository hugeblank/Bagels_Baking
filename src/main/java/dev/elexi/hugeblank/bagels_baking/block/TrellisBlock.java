package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.state.AdjacentPosition;
import dev.elexi.hugeblank.bagels_baking.state.BakingProperties;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TrellisBlock extends Block implements Waterloggable {
    private static final VoxelShape EAST_SHAPE;
    private static final VoxelShape WEST_SHAPE;
    private static final VoxelShape SOUTH_SHAPE;
    private static final VoxelShape NORTH_SHAPE;
    public static final BooleanProperty WATERLOGGED;
    public static final DirectionProperty FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF;
    public static final IntProperty DISTANCE;
    public static final EnumProperty<AdjacentPosition> ADJACENT;
    public static final int MAX_DISTANCE = 6;

    public TrellisBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(DISTANCE, 0)
                .with(HALF, DoubleBlockHalf.LOWER)
                .with(WATERLOGGED, false)
                .with(ADJACENT, AdjacentPosition.NONE)
        );
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        BlockState trellisState = this.getDefaultState().with(FACING, ctx.getPlayerFacing());
        BlockState below = world.getBlockState(pos.down());
        if (!below.isSideSolidFullSquare(world, pos, Direction.UP)) {
            trellisState = trellisState.with(HALF, DoubleBlockHalf.UPPER);

            BlockState hitState = world.getBlockState(pos.offset(ctx.getSide().getOpposite()));
            if (hitState.getBlock() instanceof TrellisBlock) {
                trellisState = trellisState.with(FACING, hitState.get(FACING));
            }

            trellisState = calculateDistance(world, pos, trellisState);

            if (trellisState.get(DISTANCE) == MAX_DISTANCE) return null;
        }
        return trellisState;
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        state = calculateDistance(world, pos, state);
        if (state.get(DISTANCE) == MAX_DISTANCE) {
            world.breakBlock(pos, true);
        } else {
            world.setBlockState(pos, state, 3);
        }
    }

    public BlockState calculateDistance(BlockView world, BlockPos pos, BlockState state) {
        Direction facing = state.get(FACING);
        BlockState below = world.getBlockState(pos.down());
        BlockState left = world.getBlockState(pos.offset(facing.rotateClockwise(Direction.Axis.Y)));
        BlockState right = world.getBlockState(pos.offset(facing.rotateCounterclockwise(Direction.Axis.Y)));
        // Tests for necessary adjacent blocks
        boolean isBelow = below.getBlock() instanceof TrellisBlock && below.get(HALF) == DoubleBlockHalf.LOWER && below.get(FACING) == facing;
        boolean isLeft = left.getBlock() instanceof TrellisBlock && left.get(FACING) == facing;
        boolean isRight = right.getBlock() instanceof TrellisBlock && right.get(FACING) == facing;

        if ((state.getBlock() instanceof TrellisBlock && state.get(HALF) == DoubleBlockHalf.LOWER) || isBelow) {
            // If the block we're calculating the distance for is the lower trellis, or a lower trellis is below us

            // Handle adjacent trellis state for model
            if (isLeft) {
                if (isRight) {
                    state = state.with(ADJACENT, AdjacentPosition.BOTH);
                } else {
                    state = state.with(ADJACENT, AdjacentPosition.LEFT);
                }
            } else if (isRight) {
                state = state.with(ADJACENT, AdjacentPosition.RIGHT);
            }

            return state.with(DISTANCE, 0);
        }
        int i = MAX_DISTANCE;
        if (isLeft) {
            i = Math.min(i, left.get(DISTANCE)+1);
        }
        if (isRight) {
            i = Math.min(i, right.get(DISTANCE)+1);
        }
        return state.with(DISTANCE, i);
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient) {
            world.getBlockTickScheduler().schedule(pos, this, 1);
        }
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (!world.isClient()) {
            world.getBlockTickScheduler().schedule(pos, this, 1);
        }

        if (state.get(HALF) == DoubleBlockHalf.LOWER && !world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP)) {
            return Blocks.AIR.getDefaultState();
        }

        return state;
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, HALF, DISTANCE, ADJACENT, WATERLOGGED);
    }

    static {
        EAST_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D);
        WEST_SHAPE = Block.createCuboidShape(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
        SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D);
        NORTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D);

        HALF = Properties.DOUBLE_BLOCK_HALF;
        DISTANCE = IntProperty.of("distance", 0, 6);
        FACING = Properties.HORIZONTAL_FACING;
        WATERLOGGED = Properties.WATERLOGGED;
        ADJACENT = BakingProperties.ADJACENT;
    }
}