package dev.elexi.hugeblank.bagels_baking.compat.rei.cauldron;

import com.google.common.collect.Lists;
import dev.elexi.hugeblank.bagels_baking.compat.rei.Plugin;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SeparatorCategory implements DisplayCategory<SeparatorDisplay> {

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Items.EGG);
    }

    @Override
    public Text getTitle() {
        return new TranslatableText("category.rei.separating");
    }

    @Override
    public @NotNull List<Widget> setupDisplay(SeparatorDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 57, bounds.getCenterY() - 20);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 93, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 93, startPoint.y + 5)).entries(display.getOutputEntries().get(1)).disableBackground().markOutput());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }

    @Override
    public CategoryIdentifier<? extends SeparatorDisplay> getCategoryIdentifier() {
        return Plugin.SEPARATING;
    }
}
