package dev.elexi.hugeblank.bagels_baking.world.gen;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.function.Predicate;

public class BakingPlacedFeatures {

    public static PlacedFeature register(String id, PlacedFeature feature) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(Baking.ID, id), feature);
    }

    public static PlacedFeature registerInBiomes(String name, PlacedFeature feature, Predicate<BiomeSelectionContext> selector, GenerationStep.Feature category) {
        RegistryKey<PlacedFeature> key = RegistryKey.of(Registry.PLACED_FEATURE_KEY,
                new Identifier(Baking.ID, name));
        BiomeModifications.addFeature(selector, category, key);
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, key.getValue(), feature);
    }
}
