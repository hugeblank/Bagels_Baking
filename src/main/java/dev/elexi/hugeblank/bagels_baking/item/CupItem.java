package dev.elexi.hugeblank.bagels_baking.item;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class CupItem extends Item {

    public CupItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (hitResult.getType() != HitResult.Type.MISS) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = hitResult.getBlockPos();
                if (!world.canPlayerModifyAt(user, blockPos)) {
                    return TypedActionResult.pass(itemStack);
                }

                if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                    world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    return TypedActionResult.success(this.fill(itemStack, user, new ItemStack(Baking.WATER_CUP)), world.isClient());
                }
            }

        }
        return TypedActionResult.pass(itemStack);
    }

    protected ItemStack fill(ItemStack itemStack, PlayerEntity playerEntity, ItemStack itemStack2) {
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        boolean bl2 = playerEntity.getAbilities().creativeMode;
        if (bl2) {
            if (!playerEntity.getInventory().contains(itemStack2)) {
                playerEntity.getInventory().insertStack(itemStack2);
            }
        } else {
            itemStack.decrement(1);

            if (!playerEntity.getInventory().insertStack(itemStack2)) {
                playerEntity.dropItem(itemStack2, false);
            }

        }
        return itemStack;
    }
}
