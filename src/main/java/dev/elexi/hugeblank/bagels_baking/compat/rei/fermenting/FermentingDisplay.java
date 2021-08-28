package dev.elexi.hugeblank.bagels_baking.compat.rei.fermenting;

import dev.architectury.utils.NbtType;
import dev.elexi.hugeblank.bagels_baking.compat.rei.Plugin;
import dev.elexi.hugeblank.bagels_baking.recipe.FermentingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class FermentingDisplay implements Display {
    protected List<EntryIngredient> inputs;
    protected EntryIngredient collector;
    protected EntryIngredient output;

    public FermentingDisplay(FermentingRecipe recipe) {
        this(EntryIngredients.ofIngredients(Collections.singletonList(recipe.getInput())), EntryIngredients.of(recipe.getCollector()), EntryIngredients.of(recipe.getOutput()));
    }

    public FermentingDisplay(List<EntryIngredient> inputs, EntryIngredient collector, EntryIngredient output) {
        this.inputs = inputs;
        this.output = output;
        this.collector = collector;
    }

    public List<EntryIngredient> getCollectorEntries() {
        return Collections.singletonList(collector);
    }


    @Override
    public List<EntryIngredient> getInputEntries() {
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return Collections.singletonList(output);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return Plugin.FERMENTING;
    }

    public static DisplaySerializer<FermentingDisplay> serializer() {
        return new DisplaySerializer<>() {
            @Override
            public NbtCompound save(NbtCompound tag, FermentingDisplay display) {
                tag.put("input", EntryIngredients.save(display.inputs));
                tag.put("reactant", display.collector.save());
                tag.put("output", display.output.save());
                return tag;
            }

            @Override
            public FermentingDisplay read(NbtCompound tag) {
                List<EntryIngredient> input = EntryIngredients.read(tag.getList("input", NbtType.COMPOUND));
                EntryIngredient reactant = EntryIngredient.read(tag.getList("reactant", NbtType.COMPOUND));
                EntryIngredient output = EntryIngredient.read(tag.getList("output", NbtType.COMPOUND));
                return new FermentingDisplay(input, reactant, output);
            }
        };
    }

}
