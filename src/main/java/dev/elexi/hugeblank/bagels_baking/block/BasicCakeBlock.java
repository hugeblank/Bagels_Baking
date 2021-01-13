package dev.elexi.hugeblank.bagels_baking.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;

public class BasicCakeBlock extends CakeBlock {
    public BasicCakeBlock() {
        super(FabricBlockSettings.copy(Blocks.CAKE));
    }
}
