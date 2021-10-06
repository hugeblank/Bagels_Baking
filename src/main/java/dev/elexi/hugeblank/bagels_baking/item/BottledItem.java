package dev.elexi.hugeblank.bagels_baking.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class BottledItem extends PotionItem implements BrewableItem {

    private final SoundEvent soundEffect;
    private final boolean brewable;

    public BottledItem(Settings settings) {
        this(settings, SoundEvents.ENTITY_GENERIC_DRINK);
    }

    public BottledItem(Settings settings, SoundEvent drinkSound) {
        this(settings, drinkSound, false);
    }

    public BottledItem(Settings settings, SoundEvent drinkSound, boolean brewable) {
        super(settings.recipeRemainder(Items.GLASS_BOTTLE));
        soundEffect = drinkSound;
        this.brewable = brewable;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (playerEntity != null) {
                playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        return this.isFood() ? user.eatFood(world, stack) : stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        if (stack.getItem().isFood()) {
            return Objects.requireNonNull(this.getFoodComponent()).isSnack() ? 16 : 32;
        } else {
            return 0;
        }
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add(new ItemStack(this));
        }
    }

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
    public SoundEvent getEatSound() {
        return soundEffect;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return soundEffect;
    }

    public boolean isBrewable() {
        return this.brewable;
    }

}
