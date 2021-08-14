package dev.elexi.hugeblank.bagels_baking.block;

import com.google.common.collect.ImmutableMap;
import dev.elexi.hugeblank.bagels_baking.state.AdjacentPosition;
import dev.elexi.hugeblank.bagels_baking.state.BakingProperties;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BasicVineComponentBlock extends Block {
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final EnumProperty<AdjacentPosition> ADJACENT;
    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES;
    private static final VoxelShape EAST_SHAPE;
    private static final VoxelShape EAST_LEFT_SHAPE;
    private static final VoxelShape EAST_RIGHT_SHAPE;
    private static final VoxelShape WEST_SHAPE;
    private static final VoxelShape WEST_LEFT_SHAPE;
    private static final VoxelShape WEST_RIGHT_SHAPE;
    private static final VoxelShape SOUTH_SHAPE;
    private static final VoxelShape SOUTH_LEFT_SHAPE;
    private static final VoxelShape SOUTH_RIGHT_SHAPE;
    private static final VoxelShape NORTH_SHAPE;
    private static final VoxelShape NORTH_LEFT_SHAPE;
    private static final VoxelShape NORTH_RIGHT_SHAPE;
    private final ImmutableMap<BlockState, VoxelShape> shapesByState;

    public BasicVineComponentBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(ADJACENT, AdjacentPosition.NONE));
        this.shapesByState = ImmutableMap.copyOf(this.stateManager.getStates().stream().collect(Collectors.toMap(Function.identity(), BasicVineComponentBlock::getShapeForState)));
    }

    private static VoxelShape getShapeForState(BlockState state) {
        VoxelShape voxelShape = VoxelShapes.empty();

        if (state.get(NORTH)) {
            voxelShape = SOUTH_SHAPE;
            switch (state.get(ADJACENT)) {
                case BOTH -> voxelShape = VoxelShapes.union(voxelShape, SOUTH_LEFT_SHAPE, SOUTH_RIGHT_SHAPE);
                case LEFT -> voxelShape = VoxelShapes.union(voxelShape, SOUTH_LEFT_SHAPE);
                case RIGHT -> voxelShape = VoxelShapes.union(voxelShape, SOUTH_RIGHT_SHAPE);
            }
        }

        voxelShape = getVoxelShape(state, voxelShape, SOUTH, NORTH_SHAPE, NORTH_LEFT_SHAPE, NORTH_RIGHT_SHAPE);

        voxelShape = getVoxelShape(state, voxelShape, EAST, WEST_SHAPE, WEST_LEFT_SHAPE, WEST_RIGHT_SHAPE);

        voxelShape = getVoxelShape(state, voxelShape, WEST, EAST_SHAPE, EAST_LEFT_SHAPE, EAST_RIGHT_SHAPE);

        if (state.get(SOUTH) && state.get(EAST)) {
            voxelShape = VoxelShapes.union(voxelShape, NORTH_LEFT_SHAPE, WEST_RIGHT_SHAPE);
            switch (state.get(ADJACENT)) {
                case BOTH -> voxelShape = VoxelShapes.union(voxelShape, NORTH_RIGHT_SHAPE, WEST_LEFT_SHAPE);
                case LEFT -> voxelShape = VoxelShapes.union(voxelShape, WEST_LEFT_SHAPE);
                case RIGHT -> voxelShape = VoxelShapes.union(voxelShape, NORTH_RIGHT_SHAPE);
            }
        }

        if (state.get(NORTH) && state.get(EAST)) {
            voxelShape = VoxelShapes.union(voxelShape, SOUTH_RIGHT_SHAPE, WEST_LEFT_SHAPE);
            switch (state.get(ADJACENT)) {
                case BOTH -> voxelShape = VoxelShapes.union(voxelShape, SOUTH_LEFT_SHAPE, WEST_RIGHT_SHAPE);
                case LEFT -> voxelShape = VoxelShapes.union(voxelShape, SOUTH_LEFT_SHAPE);
                case RIGHT -> voxelShape = VoxelShapes.union(voxelShape, WEST_RIGHT_SHAPE);
            }
        }

        if (state.get(SOUTH) && state.get(WEST)) {
            voxelShape = VoxelShapes.union(voxelShape, NORTH_RIGHT_SHAPE, EAST_LEFT_SHAPE);
            switch (state.get(ADJACENT)) {
                case BOTH -> voxelShape = VoxelShapes.union(voxelShape, NORTH_LEFT_SHAPE, EAST_RIGHT_SHAPE);
                case LEFT -> voxelShape = VoxelShapes.union(voxelShape, NORTH_LEFT_SHAPE);
                case RIGHT -> voxelShape = VoxelShapes.union(voxelShape, EAST_RIGHT_SHAPE);
            }
        }

        if (state.get(NORTH) && state.get(WEST)) {
            voxelShape = VoxelShapes.union(voxelShape, SOUTH_RIGHT_SHAPE, EAST_LEFT_SHAPE);
            switch (state.get(ADJACENT)) {
                case BOTH -> voxelShape = VoxelShapes.union(voxelShape, SOUTH_LEFT_SHAPE, EAST_RIGHT_SHAPE);
                case LEFT -> voxelShape = VoxelShapes.union(voxelShape, SOUTH_LEFT_SHAPE);
                case RIGHT -> voxelShape = VoxelShapes.union(voxelShape, EAST_RIGHT_SHAPE);
            }
        }

        return voxelShape;
    }

    private static VoxelShape getVoxelShape(BlockState state, VoxelShape voxelShape, BooleanProperty directionProperty, VoxelShape centerShape, VoxelShape leftShape, VoxelShape rightShape) {
        if (state.get(directionProperty)) {
            voxelShape = VoxelShapes.union(voxelShape, centerShape);
            switch (state.get(ADJACENT)) {
                case BOTH -> voxelShape = VoxelShapes.union(voxelShape, leftShape, rightShape);
                case LEFT -> voxelShape = VoxelShapes.union(voxelShape, leftShape);
                case RIGHT -> voxelShape = VoxelShapes.union(voxelShape, rightShape);
            }
        }
        return voxelShape;
    }

    public ArrayList<Direction> getDirectionsFromState(BlockState state) {
        ArrayList<Direction> directions = new ArrayList<>();
        if (state.get(NORTH)) {
            directions.add(Direction.NORTH);
        }
        if (state.get(SOUTH)) {
            directions.add(Direction.SOUTH);
        }
        if (state.get(EAST)) {
            directions.add(Direction.EAST);
        }
        if (state.get(WEST)) {
            directions.add(Direction.WEST);
        }
        return directions;
    }

    public Direction getLeftmostFromState(BlockState state) {
        ArrayList<Direction> directions = getDirectionsFromState(state);
        for (Direction direction : directions) {
            if (!state.get(FACING_PROPERTIES.get(direction.rotateCounterclockwise(Direction.Axis.Y)))) {
                return direction.rotateCounterclockwise(Direction.Axis.Y);
            }
        }
        if (!directions.isEmpty()) {
            return directions.get(0);
        } else { // This should never happen
            return Direction.NORTH;
        }
    }

    public Direction getRightmostFromState(BlockState state) {
        ArrayList<Direction> directions = getDirectionsFromState(state);
        for (Direction direction : directions) {
            if (!state.get(FACING_PROPERTIES.get(direction.rotateClockwise(Direction.Axis.Y)))) {
                return direction.rotateClockwise(Direction.Axis.Y);
            }
        }
        if (!directions.isEmpty()) {
            return directions.get(0);
        } else { // This should never happen
            return Direction.NORTH;
        }
    }

    public BlockState getAdjacencyState(WorldAccess world, BlockPos pos, BlockState state) {
        BlockState left = world.getBlockState(pos.offset(getLeftmostFromState(state)));
        BlockState right = world.getBlockState(pos.offset(getRightmostFromState(state)));

        boolean isLeft = left.getBlock() instanceof BasicVineComponentBlock;
        boolean isRight = right.getBlock() instanceof BasicVineComponentBlock;
        if (isLeft && isRight) {
            state = state.with(ADJACENT, AdjacentPosition.BOTH);
        } else if (isLeft) {
            state = state.with(ADJACENT, AdjacentPosition.LEFT);
        } else if (isRight) {
            state = state.with(ADJACENT, AdjacentPosition.RIGHT);
        } else {
            state = state.with(ADJACENT, AdjacentPosition.NONE);
        }

        return state;
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {

        state = getAdjacencyState(world, pos, state);

        return !this.hasAdjacentBlocks(state) ? Blocks.AIR.getDefaultState() : state;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapesByState.get(state);
    }

    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return this.hasAdjacentBlocks(state);
    }

    protected boolean hasAdjacentBlocks(BlockState state) {
        return this.getAdjacentBlockCount(state) > 0;
    }

    private int getAdjacentBlockCount(BlockState state) {
        int i = 0;

        for (BooleanProperty booleanProperty : FACING_PROPERTIES.values()) {
            if (state.get(booleanProperty)) {
                ++i;
            }
        }

        return i;
    }

    private boolean shouldHaveSide(BlockView world, BlockPos pos, Direction side) {
        if (side == Direction.DOWN || side == Direction.UP) {
            return false;
        } else {
            BlockPos blockPos = pos.offset(side);
            if (shouldConnectTo(world, blockPos, side)) {
                return true;
            } else if (side.getAxis() == Direction.Axis.Y) {
                return false;
            } else {
                BooleanProperty booleanProperty = FACING_PROPERTIES.get(side);
                BlockState blockState = world.getBlockState(pos.up());
                return blockState.getBlock() instanceof BasicVineComponentBlock && blockState.get(booleanProperty);
            }
        }
    }

    public static boolean shouldConnectTo(BlockView world, BlockPos pos, Direction direction) {
        // TODO rewrite THIS
        BlockState blockState = world.getBlockState(pos);
        return Block.isFaceFullSquare(blockState.getCollisionShape(world, pos), direction.getOpposite());
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {

    }

    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        BlockState blockState = context.getWorld().getBlockState(context.getBlockPos());
        if (blockState.getBlock() instanceof BasicVineComponentBlock) {
            return this.getAdjacentBlockCount(blockState) < FACING_PROPERTIES.size();
        } else {
            return super.canReplace(state, context);
        }
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        boolean isVine = blockState.getBlock() instanceof BasicVineComponentBlock;
        BlockState blockState2 = isVine ? blockState : this.getDefaultState();
        Direction[] var5 = Direction.values();

        for (Direction direction : var5) {
            if (direction != Direction.DOWN && direction != Direction.UP) {
                BooleanProperty booleanProperty = getFacingProperty(direction);
                boolean sameFacing = isVine && blockState.get(booleanProperty);
                if (!sameFacing && this.shouldHaveSide(ctx.getWorld(), ctx.getBlockPos(), direction)) {
                    return blockState2.with(booleanProperty, true);
                }
            }
        }

        return isVine ? getAdjacencyState(ctx.getWorld(), ctx.getBlockPos(), blockState2) : null;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(NORTH, EAST, SOUTH, WEST, ADJACENT);
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 -> state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST)).with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
            case COUNTERCLOCKWISE_90 -> state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH)).with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
            case CLOCKWISE_90 -> state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH)).with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
            default -> state;
        };
    }

    public static BooleanProperty getFacingProperty(Direction direction) {
        return FACING_PROPERTIES.get(direction);
    }

    static {
        NORTH = ConnectingBlock.NORTH;
        EAST = ConnectingBlock.EAST;
        SOUTH = ConnectingBlock.SOUTH;
        WEST = ConnectingBlock.WEST;
        ADJACENT = BakingProperties.ADJACENT;
        FACING_PROPERTIES = ConnectingBlock.FACING_PROPERTIES.entrySet().stream().filter((entry) -> (!(entry.getKey() == Direction.DOWN || entry.getKey() == Direction.UP))).collect(Util.toMap());
        EAST_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 6.0D, 1.0D, 16.0D, 10.0D);
        EAST_RIGHT_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 10.0D);
        EAST_LEFT_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 6.0D, 1.0D, 16.0D, 16.0D);
        WEST_SHAPE = Block.createCuboidShape(15.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
        WEST_RIGHT_SHAPE = Block.createCuboidShape(15.0D, 0.0D, 6.0D, 16.0D, 16.0D, 16.0D);
        WEST_LEFT_SHAPE = Block.createCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 10.0D);
        SOUTH_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 1.0D);
        SOUTH_LEFT_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 10.0D, 16.0D, 1.0D);
        SOUTH_RIGHT_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
        NORTH_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 15.0D, 10.0D, 16.0D, 16.0D);
        NORTH_LEFT_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
        NORTH_RIGHT_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 15.0D, 10.0D, 16.0D, 16.0D);
    }

}
