package dev.elexi.hugeblank.bagels_baking.compat.rei;

import dev.elexi.hugeblank.bagels_baking.compat.rei.milling.MillingCategory;
import dev.elexi.hugeblank.bagels_baking.compat.rei.milling.MillingDisplay;
import dev.elexi.hugeblank.bagels_baking.recipe.MillingRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new MillingCategory());
        registry.addWorkstations(Plugin.MILLING, Plugin.MILL);
        registry.removePlusButton(Plugin.MILLING);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(MillingRecipe.class, MillingDisplay::new);
    }
}

