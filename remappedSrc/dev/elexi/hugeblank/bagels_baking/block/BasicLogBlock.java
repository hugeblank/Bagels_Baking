package dev.elexi.hugeblank.bagels_baking.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class BasicLogBlock extends PillarBlock{
    private final Supplier<Block> stripped;
    public BasicLogBlock(Supplier<Block> stripped) {
        super(FabricBlockSettings.copy(Blocks.OAK_LOG));
        this.stripped = stripped;
    }

    public BasicLogBlock() {
        super(FabricBlockSettings.copy(Blocks.OAK_LOG));
        this.stripped = null;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getEquippedStack(hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

        if(heldStack.isEmpty()) {
            return ActionResult.FAIL;
        }

        Item held = heldStack.getItem();
        if(!(held instanceof MiningToolItem tool)) {
            return ActionResult.FAIL;
        }

        if(stripped != null && (tool.isSuitableFor(state) || tool.getMiningSpeedMultiplier(heldStack, state) > 1.0F)) {
            world.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);

            if(!world.isClient) {
                BlockState target = stripped.get().getDefaultState().with(PillarBlock.AXIS, state.get(PillarBlock.AXIS));

                world.setBlockState(pos, target);

                heldStack.damage(1, player, consumedPlayer -> consumedPlayer.sendToolBreakStatus(hand));
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }
}
