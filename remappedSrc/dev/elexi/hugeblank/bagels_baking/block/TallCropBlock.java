package dev.elexi.hugeblank.bagels_baking.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TallCropBlock extends BasicCropBlock implements Fertilizable {

    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    public static final IntProperty AGE = Properties.AGE_7;
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public TallCropBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HALF, DoubleBlockHalf.LOWER).with(this.getAgeProperty(), 0));
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        return blockPos.getY() < 255 ? super.getPlacementState(ctx) : null;
    }

    private void applyGrowth(ServerWorld world, BlockPos pos, BlockState state) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            int growth = this.getAge(state) + this.getGrowthAmount(world);
            int maxAge = this.getMaxAge();
            if (growth > maxAge) {
                growth = maxAge;
            }
            world.setBlockState(pos, this.withAge(growth), 2);
            addTop(world, pos, growth);
        }
    }

    private void addTop(ServerWorld world, BlockPos pos, int growth) {
        BlockState aboveState = world.getBlockState(pos.up());
        if (growth >= 4 && aboveState.getBlock().equals(Blocks.AIR)) {
            world.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER).with(AGE, growth - 4));
        } else if (aboveState.getBlock() instanceof TallCropBlock) {
            world.setBlockState(pos.up(), aboveState.with(AGE, growth - 4));
        }
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBaseLightLevel(pos, 0) >= 9 && state.get(HALF) == DoubleBlockHalf.LOWER) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getAvailableMoisture(this, world, pos);
                if (random.nextInt((int)(25.0F / f) + 1) == 0) {
                    applyGrowth(world, pos, state);
                }
            }
        }
    }

    protected static float getAvailableMoisture(Block block, BlockView world, BlockPos pos) {
        if (world.getBlockState(pos).get(HALF) == DoubleBlockHalf.UPPER) {
            BlockState lowerState = world.getBlockState(pos.down());
            return CropBlock.getAvailableMoisture(lowerState.getBlock(), world, pos.down().down());
        } else {
            return CropBlock.getAvailableMoisture(block, world, pos.down());
        }
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (this.getAge(state) < 4) {
            return AGE_TO_SHAPE[state.get(this.getAgeProperty())];
        } else {
            return AGE_TO_SHAPE[4];
        }
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState lower = world.getBlockState(pos.down());
        return state.get(HALF) == DoubleBlockHalf.LOWER ? super.canPlaceAt(state, world, pos) : (lower.getBlock() == this && state.get(HALF) == DoubleBlockHalf.UPPER && lower.get(AGE) > 3);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        DoubleBlockHalf doubleBlockHalf = state.get(HALF);
        if (doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.UP && (state.get(AGE) > 3 && world.getBlockState(pos.up()).getBlock() == Blocks.AIR)) {
            return Blocks.AIR.getDefaultState();
        } else {

            return direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
        }
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            return !isMature(state);
        } else {
            return !isMature(world.getBlockState(pos.down()));
        }

    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF, AGE);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            applyGrowth(world, pos, state);
        } else {
            BlockState base = world.getBlockState(pos.down());
            ((TallCropBlock)base.getBlock()).grow(world, random, pos.down(), base);
        }
    }
}
