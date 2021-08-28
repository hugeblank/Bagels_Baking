package dev.elexi.hugeblank.bagels_baking.compat.rei;

import dev.elexi.hugeblank.bagels_baking.compat.rei.fermenting.FermentingDisplay;
import dev.elexi.hugeblank.bagels_baking.compat.rei.freezing.FreezingDisplay;
import dev.elexi.hugeblank.bagels_baking.compat.rei.milling.MillingDisplay;
import dev.elexi.hugeblank.bagels_baking.recipe.FermentingRecipe;
import dev.elexi.hugeblank.bagels_baking.recipe.FreezingRecipe;
import dev.elexi.hugeblank.bagels_baking.recipe.MillingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;

public class Plugin implements REIServerPlugin {

    public static final CategoryIdentifier<MillingDisplay> MILLING = CategoryIdentifier.of(MillingRecipe.ID);
    public static final CategoryIdentifier<FermentingDisplay> FERMENTING = CategoryIdentifier.of(FermentingRecipe.ID);
    public static final CategoryIdentifier<FreezingDisplay> FREEZING = CategoryIdentifier.of(FreezingRecipe.ID);

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(MILLING, MillingDisplay.serializer());
        registry.register(FERMENTING, FermentingDisplay.serializer());
        registry.register(FREEZING, FreezingDisplay.serializer());
    }
}
