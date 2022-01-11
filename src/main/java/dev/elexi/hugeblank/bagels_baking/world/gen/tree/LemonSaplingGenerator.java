package dev.elexi.hugeblank.bagels_baking.world.gen.tree;

import dev.elexi.hugeblank.bagels_baking.world.gen.BakingTreeConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class LemonSaplingGenerator extends SaplingGenerator {

    public LemonSaplingGenerator() {}

    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bees) {
        return bees ? BakingTreeConfiguredFeatures.LEMON_TREE_BEES_002 : BakingTreeConfiguredFeatures.LEMON_TREE;
    }
}
