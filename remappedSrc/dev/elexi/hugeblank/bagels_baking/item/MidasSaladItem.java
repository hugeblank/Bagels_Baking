package dev.elexi.hugeblank.bagels_baking.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.MushroomStewItem;

public class MidasSaladItem extends MushroomStewItem { // Just merged MushroomStewItem with EnchantedGoldenAppleItem
    public MidasSaladItem(Settings settings) {
        super(settings);
    }

    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
