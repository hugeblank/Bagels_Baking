package dev.elexi.hugeblank.bagels_baking.mixin.entity;

import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DamageSource.class)
public interface DamageSourceAccessor {
    @Invoker("<init>")
    static DamageSource newDamageSource(String name) {
        throw new AssertionError();
    }
}
