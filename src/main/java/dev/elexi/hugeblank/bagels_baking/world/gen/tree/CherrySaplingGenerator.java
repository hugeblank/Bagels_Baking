package dev.elexi.hugeblank.bagels_baking.world.gen.tree;

import dev.elexi.hugeblank.bagels_baking.world.gen.BakingConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class CherrySaplingGenerator extends SaplingGenerator {
    public CherrySaplingGenerator() {}

    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bees) {
        return bees ? BakingConfiguredFeatures.CHERRY_TREE_BEES_005 : BakingConfiguredFeatures.CHERRY_TREE;
    }
}
