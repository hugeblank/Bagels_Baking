package dev.elexi.hugeblank.bagels_baking.compat.rei;

import dev.elexi.hugeblank.bagels_baking.compat.rei.milling.MillingDisplay;
import dev.elexi.hugeblank.bagels_baking.recipe.MillingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;

public class Plugin implements REIServerPlugin {

    public static final CategoryIdentifier<MillingDisplay> MILLING = CategoryIdentifier.of(MillingRecipe.ID);

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(MILLING, MillingDisplay.serializer());
    }
}
