package dev.elexi.hugeblank.bagels_baking.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.StewItem;

public class MidasSaladItem extends StewItem { // Just merged StewItem with EnchantedGoldenAppleItem
    public MidasSaladItem(Settings settings) {
        super(settings);
    }

    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
