package dev.elexi.hugeblank.bagels_baking.item;

import dev.elexi.hugeblank.bagels_baking.entity.TomatoEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TomatoItem extends BlockItem {

    public TomatoItem(Block block, Item.Settings settings) {
        super(block, settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.isSneaking()) {
            return super.use(world, user, hand);
        } else {
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!world.isClient && user instanceof ServerPlayerEntity) {

                TomatoEntity tomato = new TomatoEntity(world, user);
                tomato.setOwner(user);
                tomato.setItem(itemStack);
                tomato.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
                world.spawnEntity(tomato);
            }

            user.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!user.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            return TypedActionResult.success(itemStack, world.isClient());
        }
    }
}
