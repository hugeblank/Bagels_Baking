package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.block.entity.FermenterBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FermenterBlock extends BlockWithEntity {

    public FermenterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand.equals(Hand.MAIN_HAND)) {
            FermenterBlockEntity entity = (FermenterBlockEntity) world.getBlockEntity(pos);
            if (!world.isClient && entity != null) {
                ItemStack held = player.getActiveItem();
                ItemStack drop;
                if (entity.canFill(held)) {
                    drop = entity.fillFermenter((ServerWorld) world, held);
                } else {
                    drop = entity.drainFermenter((ServerWorld) world, held);
                }
                player.getInventory().insertStack(drop);
            }
        }
        return ActionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
