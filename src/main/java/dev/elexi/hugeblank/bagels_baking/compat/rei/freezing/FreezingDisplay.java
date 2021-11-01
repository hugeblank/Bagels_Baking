package dev.elexi.hugeblank.bagels_baking.compat.rei.freezing;

import dev.elexi.hugeblank.bagels_baking.compat.rei.Plugin;
import dev.elexi.hugeblank.bagels_baking.recipe.FreezingRecipe;
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
public class FreezingDisplay extends BasicDisplay {
    public FreezingDisplay(FreezingRecipe recipe) {
        this(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getOutput())),
                Optional.ofNullable(recipe.getId()));
    }

    public FreezingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return Plugin.FREEZING;
    }

    public static BasicDisplay.Serializer<FreezingDisplay> serializer() {
        return BasicDisplay.Serializer.ofSimple(FreezingDisplay::new);
    }
}
