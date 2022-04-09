package dev.elexi.hugeblank.bagels_baking.world.gen;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Random;

public class BakingSaplingGenerator extends net.minecraft.block.sapling.SaplingGenerator {
    private final RegistryEntry<ConfiguredFeature<?,?>> feature;
    public BakingSaplingGenerator(RegistryEntry<ConfiguredFeature<?,?>> feature) { this.feature = feature;}

    @Override
    protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return feature;
    }
}
