package dev.elexi.hugeblank.bagels_baking.mixin.brewing;

import net.minecraft.item.Item;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BrewingRecipeRegistry.class)
public interface BrewingRecipeRegistryInvoker {
    @Invoker("registerItemRecipe")
    static void registerItemRecipe(Item input, Item ingredient, Item output) {
        throw new AssertionError();
    }

    @Invoker("registerPotionType")
    static void invokeRegisterPotionType(Item item) {
        throw new AssertionError();
    }

}