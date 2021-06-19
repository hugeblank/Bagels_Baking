package dev.elexi.hugeblank.bagels_baking.world.tree;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class CherryTreeSaplingGenerator extends SaplingGenerator {
    public CherryTreeSaplingGenerator() {
    }

    protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bees) {
        return bees ? (ConfiguredFeature<TreeFeatureConfig, ?>) Baking.CHERRY_TREE_BEES : (ConfiguredFeature<TreeFeatureConfig, ?>) Baking.CHERRY_TREE;
    }
}
