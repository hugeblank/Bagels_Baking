package dev.elexi.hugeblank.bagels_baking.world.gen.tree;

import dev.elexi.hugeblank.bagels_baking.world.gen.BakingTreeConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Random;

public class LemonSaplingGenerator extends SaplingGenerator {

    public LemonSaplingGenerator() {}

    protected RegistryEntry<ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return bees ? BakingTreeConfiguredFeatures.LEMON_TREE_BEES_002 : BakingTreeConfiguredFeatures.LEMON_TREE;
    }
}
