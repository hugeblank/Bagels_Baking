package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Iterator;

public class MilkCup extends BasicDrinkItem {

    public MilkCup() {
        super(Baking.CUP);
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)user;
            Iterator<StatusEffectInstance> effects = serverPlayerEntity.getActiveStatusEffects().values().iterator();
            if (effects.hasNext()) {
                StatusEffect effect = effects.next().getEffectType();
                serverPlayerEntity.removeStatusEffect(effect);
            }
        }
        return super.finishUsing(stack, world, user);
    }
}
