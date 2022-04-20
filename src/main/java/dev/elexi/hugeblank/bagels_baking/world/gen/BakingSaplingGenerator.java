package dev.elexi.hugeblank.bagels_baking.world.gen;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Random;
import java.util.function.Supplier;

public class BakingSaplingGenerator extends net.minecraft.block.sapling.SaplingGenerator {
    private final Supplier<RegistryEntry<ConfiguredFeature<?,?>>> featureSupplier;
    public BakingSaplingGenerator(Supplier<RegistryEntry<ConfiguredFeature<?,?>>> featureSupplier) { this.featureSupplier = featureSupplier;}

    @Override
    protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return featureSupplier.get();
    }
}
