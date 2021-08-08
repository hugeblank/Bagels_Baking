package dev.elexi.hugeblank.bagels_baking.world.tree;

import dev.elexi.hugeblank.bagels_baking.world.BakingConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class LemonSaplingGenerator extends SaplingGenerator {

    public LemonSaplingGenerator() {}

    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bees) {
        return bees ? BakingConfiguredFeatures.LEMON_TREE_BEES : BakingConfiguredFeatures.LEMON_TREE;
    }
}
