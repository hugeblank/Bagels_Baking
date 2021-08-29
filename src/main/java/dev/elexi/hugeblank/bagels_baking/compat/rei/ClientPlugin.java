package dev.elexi.hugeblank.bagels_baking.compat.rei;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.compat.rei.cauldron.*;
import dev.elexi.hugeblank.bagels_baking.compat.rei.fermenting.FermentingCategory;
import dev.elexi.hugeblank.bagels_baking.compat.rei.fermenting.FermentingDisplay;
import dev.elexi.hugeblank.bagels_baking.compat.rei.freezing.FreezingCategory;
import dev.elexi.hugeblank.bagels_baking.compat.rei.freezing.FreezingDisplay;
import dev.elexi.hugeblank.bagels_baking.compat.rei.milling.MillingCategory;
import dev.elexi.hugeblank.bagels_baking.compat.rei.milling.MillingDisplay;
import dev.elexi.hugeblank.bagels_baking.recipe.FermentingRecipe;
import dev.elexi.hugeblank.bagels_baking.recipe.FreezingRecipe;
import dev.elexi.hugeblank.bagels_baking.recipe.MillingRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(
                new MillingCategory(),
                new FermentingCategory(),
                new FreezingCategory(),
                new CreamingCategory(),
                new SeparatorCategory(),
                new CoolingCategory()
        );
        registry.addWorkstations(Plugin.MILLING, EntryStacks.of(Baking.MILL_ITEM));
        registry.addWorkstations(Plugin.FERMENTING, EntryStacks.of(Baking.FERMENTER_ITEM));
        registry.addWorkstations(Plugin.FREEZING, EntryStacks.of(Baking.ICE_BOX_ITEM));
        registry.addWorkstations(Plugin.CREAMING, EntryStacks.of(Items.CAULDRON));
        registry.addWorkstations(Plugin.SEPARATING, EntryStacks.of(Items.CAULDRON));
        registry.addWorkstations(Plugin.COOLING, EntryStacks.of(Items.CAULDRON));
        registry.removePlusButton(Plugin.MILLING);
        registry.removePlusButton(Plugin.FERMENTING);
        registry.removePlusButton(Plugin.FREEZING);
        registry.removePlusButton(Plugin.CREAMING);
        registry.removePlusButton(Plugin.SEPARATING);
        registry.removePlusButton(Plugin.COOLING);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(MillingRecipe.class, MillingDisplay::new);
        registry.registerFiller(FermentingRecipe.class, FermentingDisplay::new);
        registry.registerFiller(FreezingRecipe.class, FreezingDisplay::new);
        registry.registerFiller(CreamingRecipe.class, CreamingDisplay::new);
        registry.registerFiller(SeparatorRecipe.class, SeparatorDisplay::new);
        registry.registerFiller(CoolingRecipe.class, CoolingDisplay::new);

        DisplayRegistry.getInstance().add(new CreamingRecipe(
                Ingredient.ofStacks(new ItemStack(Baking.COFFEE_CUP), new ItemStack(Baking.COFFEE_CUP, 2), new ItemStack(Baking.COFFEE_CUP, 3)),
                Ingredient.ofStacks(new ItemStack(Baking.COFFEE_W_CREAMER), new ItemStack(Baking.COFFEE_W_CREAMER, 2), new ItemStack(Baking.COFFEE_W_CREAMER, 3))
        ));
        DisplayRegistry.getInstance().add(new CreamingRecipe(
                Ingredient.ofStacks(new ItemStack(Baking.TEA_CUP), new ItemStack(Baking.TEA_CUP, 2), new ItemStack(Baking.TEA_CUP, 3)),
                Ingredient.ofStacks(new ItemStack(Baking.TEA_W_CREAMER), new ItemStack(Baking.TEA_W_CREAMER, 2), new ItemStack(Baking.TEA_W_CREAMER, 3))
        ));

        DisplayRegistry.getInstance().add(new SeparatorRecipe(
                EntryIngredient.builder()
                        .add(EntryStacks.of(Items.EGG))
                        .add(EntryStacks.of(Items.EGG, 2))
                        .add(EntryStacks.of(Items.EGG, 3))
                        .build(),
                List.of(
                        EntryIngredient.builder()
                                .add(EntryStacks.of(Baking.EGG_YOLK))
                                .add(EntryStacks.of(Baking.EGG_YOLK, 2))
                                .add(EntryStacks.of(Baking.EGG_YOLK, 3))
                                .build(),
                        EntryIngredient.builder()
                                .add(EntryStacks.of(Baking.EGG_WHITES))
                                .add(EntryStacks.of(Baking.EGG_WHITES, 2))
                                .add(EntryStacks.of(Baking.EGG_WHITES, 3))
                                .build()
                )
        ));

        DisplayRegistry.getInstance().add(new CoolingRecipe(
                EntryIngredient.of(EntryStacks.of(Baking.CHEESE)),
                EntryIngredient.of(EntryStacks.of(Baking.CHEESE_BLOCK))
        ));
    }
}

