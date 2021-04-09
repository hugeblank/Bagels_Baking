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
        recipe(Baking.WATER_CUP, Baking.GROUND_TEA, Baking.TEA_CUP);
        recipe(Baking.WATER_CUP, Items.SUGAR, Baking.SUGAR_WATER);
        recipe(Baking.SUGAR_WATER, Items.COAL, Baking.CLUB_SODA);
        recipe(Baking.CLUB_SODA, Items.BEETROOT, Baking.ROOT_BEER);
        recipe(Baking.CLUB_SODA, Items.COCOA_BEANS, Baking.COLA);
        recipe(Baking.CLUB_SODA, Items.SWEET_BERRIES, Baking.FRUITY_SODA);
        recipe(Baking.CLUB_SODA, Items.MELON_SLICE, Baking.MOUNTAIN_FOUNTAIN);
        recipe(Baking.CLUB_SODA, Items.CACTUS, Baking.CACTUS_CHILLER);
        recipe(Baking.CLUB_SODA, Items.POPPED_CHORUS_FRUIT, Baking.GRAPE_SODA);

        // Items
        item(Baking.EGG_WHITES);
        item(Baking.MERINGUE);
        item(Baking.WATER_CUP);
        item(Baking.COFFEE_CUP);
        item(Baking.TEA_CUP);
        item(Baking.SUGAR_WATER);
        item(Baking.CLUB_SODA);
        item(Baking.ROOT_BEER);
        item(Baking.COLA);
        item(Baking.FRUITY_SODA);
        item(Baking.MOUNTAIN_FOUNTAIN);
        item(Baking.CACTUS_CHILLER);
        item(Baking.GRAPE_SODA);
    }

}
