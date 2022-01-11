package dev.elexi.hugeblank.bagels_baking.compat.rei.milling;

import dev.elexi.hugeblank.bagels_baking.compat.rei.Plugin;
import dev.elexi.hugeblank.bagels_baking.recipe.MillingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class MillingDisplay extends BasicDisplay {
    public MillingDisplay(MillingRecipe recipe) {
        this(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getOutput())),
                Optional.ofNullable(recipe.getId()));
    }

    public MillingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return Plugin.MILLING;
    }

    public static BasicDisplay.Serializer<MillingDisplay> serializer() {
        return BasicDisplay.Serializer.ofSimple(MillingDisplay::new);
    }
}
