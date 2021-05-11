package dev.elexi.hugeblank.bagels_baking.mixin.entity.passive;

import dev.elexi.hugeblank.bagels_baking.entity.FeedItems;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PigEntity.class)
class PigFeed {
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;ofItems([Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/recipe/Ingredient;", ordinal = 0), method = "<clinit>")
    private static Ingredient breedItems(ItemConvertible[] items) {
        return FeedItems.set(FeedItems.PIG, items);
    }
}
