package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.block.entity.FermenterBlockEntity;
import dev.elexi.hugeblank.bagels_baking.util.BakingProperties;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FermenterBlock extends BlockWithEntity {
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BakingProperties.ACTIVE;
    private static final VoxelShape SHAPE = VoxelShapes.union(Block.createCuboidShape(3.0d, 0.0d, 3.0d, 13.0d, 12.0d, 13.0d), Block.createCuboidShape(7.0d, 12.0d, 7.0d, 9.0d, 16.0d, 9.0d));

    public FermenterBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(ACTIVE, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        FermenterBlockEntity entity = (FermenterBlockEntity) world.getBlockEntity(pos);
        if (!world.isClient && entity != null && hand.equals(Hand.MAIN_HAND)) {
            ItemStack held = player.getStackInHand(Hand.MAIN_HAND);
            boolean useAll = player.isSneaking();
            ItemStack drop;
            if (entity.canFill(held)) {
                drop = entity.fillFermenter((ServerWorld) world, pos, held, useAll);
            } else {
                drop = entity.drainFermenter((ServerWorld) world, pos, held, useAll);
            }
            if (!drop.isOf(held.getItem())) {
                player.getInventory().insertStack(drop);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        FermenterBlockEntity entity = (FermenterBlockEntity) world.getBlockEntity(pos);
        if (entity != null) {
            if (state.get(ACTIVE) && random.nextFloat() < 0.05f && !entity.isEmpty()) {
                world.setBlockState(pos, state.with(ACTIVE, false));
            }
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(ACTIVE) && random.nextFloat() < 0.05f) {
            double x = pos.getX() + 0.5D;
            double y = pos.getY() + 1.0D;
            double z = pos.getZ() + 0.5D;
            world.addParticle(ParticleTypes.BUBBLE_POP, true, x, y, z, 0.0d, 0.05d, 0.0d);
            world.playSound(x, y, z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        Direction current = state.get(FACING);
        return mirror.apply(current) == current ? state : state.with(FACING, mirror.apply(current));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FermenterBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, ACTIVE);
    }
}
