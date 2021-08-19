package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.util.BakingProperties;
import dev.elexi.hugeblank.bagels_baking.util.TripleBlockThird;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class TriplePlantBlock extends PlantBlock {
    public static final EnumProperty<TripleBlockThird> THIRD = BakingProperties.TRIPLE_BLOCK_THIRD;

    public TriplePlantBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(THIRD, TripleBlockThird.LOWER));
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getAxis() == Direction.Axis.Y) {
            TripleBlockThird third = state.get(THIRD);
            boolean isNotTriplePlant = !neighborState.isOf(this);
            return switch (third) {
                case UPPER -> direction == Direction.DOWN && (isNotTriplePlant || neighborState.get(THIRD) != TripleBlockThird.MIDDLE);
                case MIDDLE -> (direction == Direction.UP && (isNotTriplePlant || neighborState.get(THIRD) != TripleBlockThird.UPPER)) || (direction == Direction.DOWN && (isNotTriplePlant || neighborState.get(THIRD) != TripleBlockThird.LOWER));
                case LOWER -> (direction == Direction.UP && (isNotTriplePlant || neighborState.get(THIRD) != TripleBlockThird.MIDDLE)) || (direction == Direction.DOWN && super.canPlaceAt(state, world, pos));
            } ? Blocks.AIR.getDefaultState() : state;
        } else {
            return state;
        }
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        return blockPos.getY() < world.getTopY() - 2 && world.getBlockState(blockPos.up()).canReplace(ctx) && world.getBlockState(blockPos.up().up()).canReplace(ctx) ? super.getPlacementState(ctx) : null;
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockPos blockPos = pos.up();
        world.setBlockState(blockPos, withWaterloggedState(world, blockPos, this.getDefaultState().with(THIRD, TripleBlockThird.MIDDLE)), Block.NOTIFY_ALL);
        world.setBlockState(blockPos.up(), withWaterloggedState(world, blockPos.up(), this.getDefaultState().with(THIRD, TripleBlockThird.UPPER)), Block.NOTIFY_ALL);
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(THIRD) == TripleBlockThird.LOWER) {
            return super.canPlaceAt(state, world, pos);
        } else {
            BlockState blockState = world.getBlockState(pos.down());
            return blockState.isOf(this) && (blockState.get(THIRD) == TripleBlockThird.LOWER || (state.get(THIRD) == TripleBlockThird.UPPER && blockState.get(THIRD) == TripleBlockThird.MIDDLE));
        }
    }

    public static void placeAt(WorldAccess world, BlockState state, BlockPos bottom, int flags) {
        BlockPos middle = bottom.up();
        BlockPos top = middle.up();
        world.setBlockState(bottom, withWaterloggedState(world, bottom, state.with(THIRD, TripleBlockThird.LOWER)), flags);
        world.setBlockState(middle, withWaterloggedState(world, middle, state.with(THIRD, TripleBlockThird.MIDDLE)), flags);
        world.setBlockState(top, withWaterloggedState(world, top, state.with(THIRD, TripleBlockThird.UPPER)), flags);
    }

    public static BlockState withWaterloggedState(WorldView world, BlockPos pos, BlockState state) {
        return state.contains(Properties.WATERLOGGED) ? state.with(Properties.WATERLOGGED, world.isWater(pos)) : state;
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
        TripleBlockThird third = state.get(THIRD);
        BlockPos blockPos = pos.down();
        BlockState blockState = world.getBlockState(blockPos);
        if (third == TripleBlockThird.UPPER) {
            if (blockState.isOf(state.getBlock()) && blockState.get(THIRD) == TripleBlockThird.MIDDLE) {
                setResidualState(world, player, blockPos, blockState);
            }
        } else if (third == TripleBlockThird.MIDDLE) {
            if (blockState.isOf(state.getBlock()) && blockState.get(THIRD) == TripleBlockThird.LOWER) {
                setResidualState(world, player, blockPos, blockState);
            }
        }

    }

    private static void setResidualState(World world, PlayerEntity player, BlockPos blockPos, BlockState blockState) {
        BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
        world.setBlockState(blockPos, blockState2, Block.NOTIFY_ALL | Block.SKIP_DROPS);
        world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
    }

    public AbstractBlock.OffsetType getOffsetType() {
        return AbstractBlock.OffsetType.XZ;
    }
    // TODO: Apply offset to collision box

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(THIRD);
    }
}
