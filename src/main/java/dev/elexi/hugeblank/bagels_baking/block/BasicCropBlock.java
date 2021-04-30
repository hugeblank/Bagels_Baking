package dev.elexi.hugeblank.bagels_baking.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;

public class BasicCropBlock extends CropBlock {

    private ItemConvertible seed;

    public BasicCropBlock(Settings settings) {
        super(settings);
    }

    public void setSeed(ItemConvertible item) {
        seed = item;
    }

    @Environment(EnvType.CLIENT)
    protected ItemConvertible getSeedsItem() {
        return seed;
    }

}
