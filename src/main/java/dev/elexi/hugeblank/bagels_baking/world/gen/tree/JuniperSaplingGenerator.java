package dev.elexi.hugeblank.bagels_baking.world.gen.tree;

import dev.elexi.hugeblank.bagels_baking.world.gen.BakingTreeConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class JuniperSaplingGenerator extends SaplingGenerator {
    public JuniperSaplingGenerator() {}

    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bees) {
        return BakingTreeConfiguredFeatures.JUNIPER_TREE;
    }
}
