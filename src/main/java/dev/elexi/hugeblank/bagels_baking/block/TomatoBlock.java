package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;

public class TomatoBlock extends CropBlock {

    public TomatoBlock(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    protected ItemConvertible getSeedsItem() {
        return Baking.TOMATO;
    }

}
