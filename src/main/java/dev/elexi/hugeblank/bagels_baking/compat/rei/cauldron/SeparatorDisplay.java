package dev.elexi.hugeblank.bagels_baking.compat.rei.cauldron;

import dev.architectury.utils.NbtType;
import dev.elexi.hugeblank.bagels_baking.compat.rei.Plugin;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.nbt.NbtCompound;

import java.util.Collections;
import java.util.List;

public class SeparatorDisplay implements Display {
    protected EntryIngredient input;
    protected List<EntryIngredient> outputs;

    public SeparatorDisplay(SeparatorRecipe recipe) {
        this(recipe.inputs(), recipe.outputs());
    }

    public SeparatorDisplay(EntryIngredient input, List<EntryIngredient> outputs) {
        this.input = input;
        this.outputs = outputs;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return Collections.singletonList(input);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return outputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return Plugin.SEPARATING;
    }

    public static DisplaySerializer<SeparatorDisplay> serializer() {
        return new DisplaySerializer<>() {
            @Override
            public NbtCompound save(NbtCompound tag, SeparatorDisplay display) {
                tag.put("input", display.input.get(0).save());
                tag.put("output", EntryIngredients.save(display.outputs));
                return tag;
            }

            @Override
            public SeparatorDisplay read(NbtCompound tag) {
                EntryIngredient input = EntryIngredient.read(tag.getList("input", NbtType.COMPOUND));
                List<EntryIngredient> output = EntryIngredients.read(tag.getList("output", NbtType.COMPOUND));
                return new SeparatorDisplay(input, output);
            }
        };
    }
}
