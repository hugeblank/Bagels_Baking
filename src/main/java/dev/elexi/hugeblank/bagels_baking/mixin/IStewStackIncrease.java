package dev.elexi.hugeblank.bagels_baking.mixin;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)

public interface IStewStackIncrease {
    @Accessor
    void setMaxCount(int maxCount);
}
