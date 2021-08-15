package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.state.BakingProperties;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

// Literally the most complicated block I've ever made.
public class GrapeVineBlock extends BasicVineComponentBlock {
    public static final int MAX_DISTANCE = 5;
    public static final IntProperty DISTANCE = BakingProperties.DISTANCE_0_5;
    public static final BooleanProperty PERSISTENT = Properties.PERSISTENT;
    public static final IntProperty AGE = Properties.AGE_2;

    public GrapeVineBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(PERSISTENT, false).with(DISTANCE, 5));
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        return state != null ? state.with(PERSISTENT, true) : null;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(AGE) == 2) {
            dropStack(world, pos, new ItemStack(Baking.GRAPES, 1 + world.random.nextInt(2)));
            world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            world.setBlockState(pos, state.with(AGE, 0), 2);
            return ActionResult.success(world.isClient);
        } else {
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.get(PERSISTENT)) {
            int i = getDistanceFromStem(neighborState) + 1;
            if (i != 1 || state.get(DISTANCE) != i) {
                world.getBlockTickScheduler().schedule(pos, this, 1);
            }
        }

        return !this.hasAdjacentBlocks(state) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, updateDistanceFromStem(state, world, pos), 3);
    }

    public boolean hasRandomTicks(BlockState state) {
        return !state.get(PERSISTENT);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(DISTANCE) == MAX_DISTANCE) {
            if (state.get(AGE) == 2) dropItems(world, pos);
            world.removeBlock(pos, false);
        }
        doPropagation(state, world, pos, random);
        int age = state.get(AGE);
        if (world.random.nextFloat() < getGrowthModifier(world, pos)/2 && age < 2 && state.get(DISTANCE) != 5) {
            world.setBlockState(pos, state.with(AGE, age+1));
        }
        super.randomTick(state, world, pos, random);
    }

    private void doPropagation(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        ArrayList<Direction> facings = getDirectionsFromState(state);
        if (facings.size() == 0) return;
        if (state.get(DISTANCE) < MAX_DISTANCE-1){
            Direction[] directions = Direction.values();
            if ((MAX_DISTANCE-state.get(DISTANCE))/world.random.nextFloat() > 1.2f) {
                if (world.random.nextBoolean()) {
                    for (Direction direction : directions) { // Grow Left/Right if block is air (propagation)
                        BlockPos growPos = pos.offset(direction);
                        BlockState growState = world.getBlockState(growPos);
                        for (Direction facing : facings) {
                            if (direction.getAxis().isHorizontal() && shouldConnectTo(world, pos, this.getDefaultState(), facing) && random.nextFloat() < 0.5f) {
                                BooleanProperty facingProperty = FACING_PROPERTIES.get(facing);
                                if (growState.getBlock() instanceof AirBlock) {
                                    world.setBlockState(growPos, updateDistanceFromStem(
                                            this.getDefaultState()
                                                    .with(DISTANCE, MAX_DISTANCE)
                                                    .with(facingProperty, true),
                                            world,
                                            growPos
                                    ));
                                    return;
                                } else if (growState.getBlock() instanceof GrapeVineBlock && !growState.get(PERSISTENT) && !growState.get(facingProperty)) {
                                    world.setBlockState(growPos, updateDistanceFromStem(
                                            growState
                                                    .with(DISTANCE, MAX_DISTANCE)
                                                    .with(facingProperty, true),
                                            world,
                                            growPos
                                    ));
                                    return;
                                }
                            }
                        }
                    }
                } else if (random.nextFloat() < 0.05f) { // Grow up/down (suckers)
                    BlockState growState = state.with(AGE, 0).with(DISTANCE, MAX_DISTANCE);;
                    if(random.nextBoolean() && world.getBlockState(pos.up()).getBlock() instanceof AirBlock) {
                        pos = pos.up();
                        int doPlacement = facings.size();
                        for (Direction facing : facings) {
                            if (!shouldConnectTo(world, pos, growState, facing)) {
                                growState.with(FACING_PROPERTIES.get(facing), false);
                                doPlacement--;
                            }
                        }

                        if (doPlacement > 0) {
                            world.setBlockState(pos, updateDistanceFromStem(growState, world, pos));
                            return;
                        }
                    } else if (world.getBlockState(pos.down()).getBlock() instanceof AirBlock){
                        pos = pos.down();
                        world.setBlockState(pos, updateDistanceFromStem(growState, world, pos));
                        return;
                    }
                }
            }
        }
        if (random.nextBoolean()){
            for (Direction facing : facings) { // Attach onto new face (rotation)
                Direction newFacing = world.random.nextBoolean() ? facing.rotateCounterclockwise(Direction.Axis.Y) : facing.rotateClockwise(Direction.Axis.Y);
                BooleanProperty newFacingProperty = FACING_PROPERTIES.get(newFacing);
                if (!state.get(newFacingProperty) && shouldConnectTo(world, pos, state, newFacing)) {
                    world.setBlockState(pos, state.with(newFacingProperty, true));
                    return;
                }
            }
        }
    }

    private float getGrowthModifier(WorldView world, BlockPos pos) {
        Direction[] directions = Direction.values();
        float modifier = 0.5f;
        float horizontalVines = 0;
        float verticalVines = 0;
        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                if (world.getBlockState(pos.offset(direction)).getBlock() instanceof GrapeVineBlock) horizontalVines++;
            } else {
                if (world.getBlockState(pos.offset(direction)).getBlock() instanceof GrapeVineBlock) verticalVines++;
            }
        }
        // Reward 1 to 2 adjacent horizontal vines
        if (horizontalVines < 3) modifier *= horizontalVines + 0.1f;
        // Punish more than 2 adjacent horizontal vines
        else modifier /= (horizontalVines/2);
        // Heavily punish adjacent vertical vines
        if (verticalVines > 0) modifier /= (verticalVines*2);
        return modifier/2;
    }

    private static BlockState updateDistanceFromStem(BlockState state, WorldAccess world, BlockPos pos) {
        int distance = MAX_DISTANCE;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        Direction[] directions = Direction.values();

        for (Direction direction : directions) {
            mutable.set(pos, direction);
            distance = Math.min(distance, getDistanceFromStem(world.getBlockState(mutable))+1);
            if (distance == 1) {
                break;
            }
        }

        return state.with(DISTANCE, distance);
    }

    private static int getDistanceFromStem(BlockState state) {
        if (state.getBlock() instanceof GrapeStemBlock) {
            return 0;
        } else {
            return state.getBlock() instanceof GrapeVineBlock ? state.get(DISTANCE) : MAX_DISTANCE;
        }
    }

    public static void dropItems(World world, BlockPos pos) {
        dropStack(world, pos, new ItemStack(Baking.GRAPES, 1 + world.random.nextInt(2)));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(DISTANCE, PERSISTENT, AGE);
    }
}