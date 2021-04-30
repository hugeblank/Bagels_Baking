package dev.elexi.hugeblank.bagels_baking.mixin.entity;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityTeaDamage {

    @ModifyVariable(at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;world:Lnet/minecraft/world/World;", ordinal = 4), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
    private byte getHurtSound(byte e, DamageSource source) {
        if (source == Baking.TEA_TREE_DMGSRC) {
            return 44;
        }
        return e;
    }
}
