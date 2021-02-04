package dev.elexi.hugeblank.bagels_baking.mixin.brewing;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.item.BrewableItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipes {

    private static void recipe(Item input, Item ingredient, Item output) {
        BrewingRecipeRegistryInvoker.registerItemRecipe(input, ingredient, output);
    }

    private static void item(Item input) {
        BrewingRecipeRegistryInvoker.invokeRegisterPotionType(input);
    }

    @Inject(at = @At(value = "TAIL"), method = "registerDefaults()V")
    private static void addRecipes(CallbackInfo ci) {
        // Recipes
        recipe(Baking.EGG_WHITES, Items.SUGAR, Baking.MERINGUE);
        recipe(Baking.WATER_CUP, Baking.GROUND_COFFEE, Baking.COFFEE_CUP);

        // Items
        item(Baking.EGG_WHITES);
        item(Baking.MERINGUE);
        item(Baking.WATER_CUP);
        item(Baking.COFFEE_CUP);
    }

}
