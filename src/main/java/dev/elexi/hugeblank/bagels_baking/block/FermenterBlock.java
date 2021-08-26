package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.block.entity.FermenterBlockEntity;
import dev.elexi.hugeblank.bagels_baking.util.BakingProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FermenterBlock extends BlockWithEntity {
    public static final BooleanProperty FERMENTED = BakingProperties.FERMENTED;

    public FermenterBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FERMENTED, false));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FERMENTED);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand.equals(Hand.MAIN_HAND)) {
            FermenterBlockEntity entity = (FermenterBlockEntity) world.getBlockEntity(pos);
            if (!world.isClient && entity != null) {
                ItemStack held = player.getStackInHand(Hand.MAIN_HAND);
                ItemStack drop;
                if (entity.canFill(held)) {
                    drop = entity.fillFermenter((ServerWorld) world, pos, held);
                } else {
                    drop = entity.drainFermenter((ServerWorld) world, pos, held);
                }
                if (!drop.isOf(held.getItem())) {
                    player.getInventory().insertStack(drop);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.FAIL;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        FermenterBlockEntity entity = (FermenterBlockEntity) world.getBlockEntity(pos);
        if (entity != null) {
            if (!state.get(FERMENTED) && random.nextFloat() < 0.2f && !entity.isEmpty()) {
                world.setBlockState(pos, state.with(FERMENTED, true));
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FermenterBlockEntity(pos, state);
    }
}
