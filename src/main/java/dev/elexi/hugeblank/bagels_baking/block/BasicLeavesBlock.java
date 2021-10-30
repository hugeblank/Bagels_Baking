package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;


public class BasicLeavesBlock extends LeavesBlock {
    public static final IntProperty AGE = Properties.AGE_2;
    private final Item drop;


    public BasicLeavesBlock(Item drop) {
        super(Settings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(BasicLeavesBlock::canSpawnOnLeaves).suffocates(Baking::never).blockVision(Baking::never));
        this.setDefaultState(this.getDefaultState().with(AGE, 0));
        this.drop = drop;
    }

    public boolean hasRandomTicks(BlockState state) {
        return !(Boolean)state.get(PERSISTENT);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.random.nextFloat() < 0.02f) {
            int age = state.get(AGE);
            // Grow regular and budding leaves, and reset fully grown ones
            state = (age < 2) ? state.with(AGE, age + 1) : state.with(AGE, 0);
            world.setBlockState(pos, state);
        }
        super.randomTick(state, world, pos, random);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(AGE) == 2) {
            float num = world.random.nextFloat();
            int extra = 0;
            if (num < 0.20f) extra = 2;
            else if (num < 0.45f) extra = 1;
            Direction[] directions = {Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP};
            for (Direction direction : directions) {
                BlockPos dropPos = pos.offset(direction).add(0.5f, 0, 0.5f);
                if (world.getBlockState(dropPos).getBlock() instanceof AirBlock) {
                    dropStack(world, dropPos, new ItemStack(drop, 1 + extra));
                    world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
                    world.setBlockState(pos, state.with(AGE, 0), 2);
                    return ActionResult.success(world.isClient);
                }
            }
            return ActionResult.FAIL;
        } else {
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AGE);
    }

    private static Boolean canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }
}
