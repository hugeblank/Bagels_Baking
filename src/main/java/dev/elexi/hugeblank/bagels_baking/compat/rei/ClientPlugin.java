package dev.elexi.hugeblank.bagels_baking.compat.rei;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.compat.rei.fermenting.FermentingCategory;
import dev.elexi.hugeblank.bagels_baking.compat.rei.fermenting.FermentingDisplay;
import dev.elexi.hugeblank.bagels_baking.compat.rei.milling.MillingCategory;
import dev.elexi.hugeblank.bagels_baking.compat.rei.milling.MillingDisplay;
import dev.elexi.hugeblank.bagels_baking.recipe.FermentingRecipe;
import dev.elexi.hugeblank.bagels_baking.recipe.MillingRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new MillingCategory(), new FermentingCategory());
        registry.addWorkstations(Plugin.MILLING, EntryStacks.of(Baking.MILL_ITEM));
        registry.addWorkstations(Plugin.FERMENTING, EntryStacks.of(Baking.FERMENTER_ITEM));
        registry.removePlusButton(Plugin.MILLING);
        registry.removePlusButton(Plugin.FERMENTING);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(MillingRecipe.class, MillingDisplay::new);
        registry.registerFiller(FermentingRecipe.class, FermentingDisplay::new);
    }
}

