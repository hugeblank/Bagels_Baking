package dev.elexi.hugeblank.bagels_baking.block;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

public class BasicBucketDrinkItem extends MilkBucketItem {

    public BasicBucketDrinkItem(int hunger, float saturation) {
        super(new Item.Settings().group(ItemGroup.FOOD).maxCount(1).recipeRemainder(Items.BUCKET)
        .food(new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation).build()));
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)user;
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        if (user instanceof PlayerEntity && !((PlayerEntity)user).abilities.creativeMode) {
            stack.decrement(1);
        }

        return stack.isEmpty() ? new ItemStack(Items.BUCKET) : stack;
    }
}
