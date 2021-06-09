package dev.elexi.hugeblank.bagels_baking.compat.rei;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.compat.rei.milling.MillingDisplay;
import dev.elexi.hugeblank.bagels_baking.recipe.MillingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import net.minecraft.block.Block;

public class Plugin implements REIServerPlugin {

    public static final CategoryIdentifier<MillingDisplay> MILLING = CategoryIdentifier.of(MillingRecipe.ID);
    public static final EntryStack<Block> MILL = EntryStack.of(EntryType.deferred(MillingRecipe.ID), Baking.MILL);

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(MILLING, MillingDisplay.serializer());
    }
}
