package dev.elexi.hugeblank.bagels_baking.world.gen;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class BakingConfiguredFeatures {
    // ConfiguredFeatures methods that apply BBs own ID

    public static <FC extends FeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> register(String name, ConfiguredFeature<FC, F> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(Baking.ID, name), configuredFeature);
    }
}
