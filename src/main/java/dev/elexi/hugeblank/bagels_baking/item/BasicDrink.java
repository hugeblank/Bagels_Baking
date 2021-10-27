package dev.elexi.hugeblank.bagels_baking.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BasicDrink extends PotionItem implements BrewableItem {

    private final Item type;
    private final boolean brewable;

    public BasicDrink(Settings settings, FoodComponent foodComponent, Item type) {
        super(settings.food(foodComponent));
        this.brewable = !foodComponent.getStatusEffects().isEmpty();
        this.type = type;
    }

    public BasicDrink(Settings settings, Item type) {
        super(settings);
        this.brewable = false;
        this.type = type;
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        if (this.isFood()) {
            user.eatFood(world, stack);
        } else {
            stack.decrement(1);
        }

        ItemStack empty = new ItemStack(type);
        if (user instanceof PlayerEntity playerEntity && !((PlayerEntity)user).getAbilities().creativeMode) {
            if (!playerEntity.getInventory().insertStack(empty)) {
                playerEntity.dropItem(empty, false);
            }
        }
        return stack;
    }

    public int getMaxUseTime(ItemStack stack) {
        return type instanceof CupItem ? 16 : super.getMaxUseTime(stack);
    }

    // All of the below are for overwriting fancy potion stuffs.
    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return getTranslationKey();
    }

    @Override
    public ItemStack getDefaultStack() {
        return new ItemStack(this);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add(new ItemStack(this));
        }
    }

    public SoundEvent getEatSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }

    @Override
    public boolean isBrewable() {
        return brewable;
    }
}
