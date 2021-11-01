package dev.elexi.hugeblank.bagels_baking.compat.rei.cauldron;

import dev.architectury.utils.NbtType;
import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.compat.rei.Plugin;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreamingDisplay implements Display {
    public static final EntryIngredient CUP = EntryIngredients.ofIngredient(Ingredient.ofStacks(new ItemStack(Baking.CUP), new ItemStack(Baking.CUP, 2), new ItemStack(Baking.CUP, 3)));
    public static final EntryIngredient CREAMER = EntryIngredients.of(new ItemStack(Baking.CREAMER_CUP));
    protected final List<EntryIngredient> input = new ArrayList<>();
    protected EntryIngredient output;

    public CreamingDisplay(CreamingRecipe recipe) {
        this(recipe.input(), recipe.output());
    }

    public CreamingDisplay(Ingredient input, Ingredient output) {
        this(EntryIngredients.ofIngredient(input), EntryIngredients.ofIngredient(output));
    }

    public CreamingDisplay(EntryIngredient input, EntryIngredient output) {
        this.input.add(input);
        this.input.add(CUP);
        this.input.add(CREAMER);
        this.output = output;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return this.input;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return Collections.singletonList(this.output);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return Plugin.CREAMING;
    }

    public static DisplaySerializer<CreamingDisplay> serializer() {
        return new DisplaySerializer<>() {
            @Override
            public NbtCompound save(NbtCompound tag, CreamingDisplay display) {
                tag.put("input", display.input.get(0).save());
                tag.put("output", display.output.save());
                return tag;
            }

            @Override
            public CreamingDisplay read(NbtCompound tag) {
                EntryIngredient input = EntryIngredient.read(tag.getList("input", NbtType.COMPOUND));
                EntryIngredient output = EntryIngredient.read(tag.getList("output", NbtType.COMPOUND));
                return new CreamingDisplay(input, output);
            }
        };
    }
}
