package dev.elexi.hugeblank.bagels_baking.compat.rei.cauldron;

import dev.architectury.utils.NbtType;
import dev.elexi.hugeblank.bagels_baking.compat.rei.Plugin;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.nbt.NbtCompound;

import java.util.Collections;
import java.util.List;

public class CoolingDisplay implements Display {
    protected EntryIngredient input;
    protected EntryIngredient output;

    public CoolingDisplay(CoolingRecipe recipe) {
        this(recipe.input(), recipe.output());
    }

    public CoolingDisplay(EntryIngredient input, EntryIngredient output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return Collections.singletonList(this.input);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return Collections.singletonList(this.output);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return Plugin.COOLING;
    }

    public static DisplaySerializer<CoolingDisplay> serializer() {
        return new DisplaySerializer<>() {
            @Override
            public NbtCompound save(NbtCompound tag, CoolingDisplay display) {
                tag.put("input", display.input.save());
                tag.put("output", display.output.save());
                return tag;
            }

            @Override
            public CoolingDisplay read(NbtCompound tag) {
                EntryIngredient input = EntryIngredient.read(tag.getList("input", NbtType.COMPOUND));
                EntryIngredient output = EntryIngredient.read(tag.getList("output", NbtType.COMPOUND));
                return new CoolingDisplay(input, output);
            }
        };
    }
}
