package dev.elexi.hugeblank.bagels_baking.world.gen;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class BakingConfiguredFeatures {
    // ConfiguredFeatures methods that apply BBs own ID
    public static <FC extends FeatureConfig, F extends Feature<FC>> RegistryEntry<ConfiguredFeature<?, ?>> register(String name, F feature, FC config) {
        RegistryKey<ConfiguredFeature<?, ?>> registryKey = RegistryKey.of(BuiltinRegistries.CONFIGURED_FEATURE.getKey(), new Identifier(Baking.ID, name));
        return BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, registryKey, new ConfiguredFeature<>(feature, config));
    }
}
