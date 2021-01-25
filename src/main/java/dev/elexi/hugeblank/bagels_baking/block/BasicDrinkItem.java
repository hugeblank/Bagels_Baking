package dev.elexi.hugeblank.bagels_baking.block;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class BasicDrinkItem extends Item {

    private static Item.Settings genSettings(Item type) {
        return new Item.Settings().group(ItemGroup.FOOD)
                .maxCount(type.equals(Items.BUCKET) ? 1 : 16)
                .recipeRemainder(type.equals(Items.BUCKET) ? Items.BUCKET : null);
    }

    private boolean isBucket;
    private void bucketDrink(Item type) {
        this.isBucket = type.equals(Items.BUCKET);
    }

    public BasicDrinkItem(Item type) {
        super(genSettings(type));
        bucketDrink(type);
    }
    public BasicDrinkItem(Item type, int hunger, float saturation) {
        super(genSettings(type)
        .food(new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation).build()));
        bucketDrink(type);
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

    public int getMaxUseTime(ItemStack stack) {
        return isBucket ? 32 : 16;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
