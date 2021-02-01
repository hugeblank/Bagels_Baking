package dev.elexi.hugeblank.bagels_baking.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DoubleCrop extends PlantBlock implements Fertilizable {

    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    public static final IntProperty AGE = Properties.AGE_7;
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public DoubleCrop(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HALF, DoubleBlockHalf.LOWER).with(this.getAgeProperty(), 0));
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        return blockPos.getY() < 255 ? super.getPlacementState(ctx) : null;
    }

    private void applyGrowth(ServerWorld world, BlockPos pos, int growth) {
        BlockState upper = world.getBlockState(pos.up());
        if ((upper.getBlock() instanceof DoubleCrop || upper.getBlock() == Blocks.AIR)) {
            world.setBlockState(pos, this.withAge(growth), 2);
            addTop( world, pos, growth);
        } else if (growth < 4) {
            world.setBlockState(pos, this.withAge(growth), 2);
            addTop( world, pos, growth);
        }
    }

    private void addTop(ServerWorld world, BlockPos pos, int growth) {
        BlockState aboveState = world.getBlockState(pos.up());
        if (growth >= 4 && aboveState.getBlock().equals(Blocks.AIR)) {
            world.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER).with(AGE, growth - 4));
        } else if (aboveState.getBlock() instanceof DoubleCrop) {
            world.setBlockState(pos.up(), aboveState.with(AGE, growth - 4));
        }
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBaseLightLevel(pos, 0) >= 9 && state.get(HALF) == DoubleBlockHalf.LOWER) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getAvailableMoisture(this, world, pos);
                if (random.nextInt((int)(25.0F / f) + 1) == 0) {
                    applyGrowth(world, pos, i+1);
                }
            }
        }
    }

    protected static float getAvailableMoisture(Block block, BlockView world, BlockPos pos) {
        if (world.getBlockState(pos).get(HALF) == DoubleBlockHalf.UPPER) {
            BlockState lowerState = world.getBlockState(pos.down());
            return getAvailableMoisture(lowerState.getBlock(), world, pos.down());
        }
        float f = 1.0F;
        BlockPos blockPos = pos.down();

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float g = 0.0F;
                BlockState blockState = world.getBlockState(blockPos.add(i, 0, j));
                if (blockState.isOf(Blocks.FARMLAND)) {
                    g = 1.0F;
                    if (blockState.get(FarmlandBlock.MOISTURE) > 0) {
                        g = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    g /= 4.0F;
                }

                f += g;
            }
        }

        BlockPos blockPos2 = pos.north();
        BlockPos blockPos3 = pos.south();
        BlockPos blockPos4 = pos.west();
        BlockPos blockPos5 = pos.east();
        boolean bl = block == world.getBlockState(blockPos4).getBlock() || block == world.getBlockState(blockPos5).getBlock();
        boolean bl2 = block == world.getBlockState(blockPos2).getBlock() || block == world.getBlockState(blockPos3).getBlock();
        if (bl && bl2) {
            f /= 2.0F;
        } else {
            boolean bl3 = block == world.getBlockState(blockPos4.north()).getBlock() || block == world.getBlockState(blockPos5.north()).getBlock() || block == world.getBlockState(blockPos5.south()).getBlock() || block == world.getBlockState(blockPos4.south()).getBlock();
            if (bl3) {
                f /= 2.0F;
            }
        }

        return f;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (this.getAge(state) < 4) {
            return AGE_TO_SHAPE[state.get(this.getAgeProperty())];
        } else {
            return AGE_TO_SHAPE[4];
        }
    }

    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            if (player.isCreative()) {
                onBreakInCreative(world, pos, state, player);
            } else {
                dropStacks(state, world, pos, null, player, player.getMainHandStack());
            }
        }

        super.onBreak(world, pos, state, player);
    }

    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, Blocks.AIR.getDefaultState(), blockEntity, stack);
    }

    protected static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        DoubleBlockHalf doubleBlockHalf = state.get(HALF);
        if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
            BlockPos blockPos = pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.getBlock() == state.getBlock() && blockState.get(HALF) == DoubleBlockHalf.LOWER) {
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 35);
                world.syncWorldEvent(player, 2001, blockPos, Block.getRawIdFromState(blockState));
            }
        }

    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState lower = world.getBlockState(pos.down());
        return state.get(HALF) == DoubleBlockHalf.LOWER ? lower.isOf(Blocks.FARMLAND) : (lower.getBlock() == this && state.get(HALF) == DoubleBlockHalf.UPPER);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        DoubleBlockHalf doubleBlockHalf = state.get(HALF);
        if (doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.UP && (state.get(AGE) > 3 && world.getBlockState(pos.up()).getBlock() == Blocks.AIR)) {
            return Blocks.AIR.getDefaultState();
        } else {
            return direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
        }
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof RavagerEntity && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            world.breakBlock(pos, true, entity);
        }

        super.onEntityCollision(state, world, pos, entity);
    }

    public IntProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 7;
    }

    protected int getAge(BlockState state) {
        return state.get(this.getAgeProperty());
    }

    public BlockState withAge(int age) {
        return this.getDefaultState().with(this.getAgeProperty(), age);
    }

    public boolean isMature(BlockState state) {
        return state.get(this.getAgeProperty()) >= this.getMaxAge();
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
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    protected int getGrowthAmount(World world) {
        return MathHelper.nextInt(world.random, 2, 5);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            int i = this.getAge(state) + this.getGrowthAmount(world);
            int j = this.getMaxAge();
            if (i > j) {
                i = j;
            }
            applyGrowth(world, pos, i);
        } else {
            BlockState base = world.getBlockState(pos.down());
            ((DoubleCrop)base.getBlock()).grow(world, random, pos.down(), base);
        }
    }
}
