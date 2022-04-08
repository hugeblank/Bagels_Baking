package dev.elexi.hugeblank.bagels_baking.world.gen;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;
import java.util.function.Predicate;

public class BakingPlacedFeatures {

    public static RegistryEntry<PlacedFeature> register(Identifier id, RegistryEntry<? extends ConfiguredFeature<?, ?>> configuredFeature, List<PlacementModifier> modifiers) {
        PlacedFeature pf = new PlacedFeature((RegistryEntry<ConfiguredFeature<?, ?>>) configuredFeature, modifiers);
        RegistryEntry<PlacedFeature> entry = RegistryEntry.of(pf);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, id, pf);
        return entry;
    }

    public static RegistryEntry<PlacedFeature> register(String name, RegistryEntry<? extends ConfiguredFeature<?, ?>> configuredFeature, List<PlacementModifier> modifiers) {
        return register(new Identifier(Baking.ID, name), configuredFeature, modifiers);
    }

    public static RegistryEntry<PlacedFeature> register(String name, RegistryEntry<? extends ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... modifiers) {
        return register(name, configuredFeature, List.of(modifiers));
    }

    public static RegistryEntry<PlacedFeature> registerInBiomes(String name, RegistryEntry<? extends ConfiguredFeature<?, ?>> configuredFeature, Predicate<BiomeSelectionContext> selector, GenerationStep.Feature category, PlacementModifier... modifiers) {
        Identifier id = new Identifier(Baking.ID, name);
        RegistryEntry<PlacedFeature> entry = register(id, configuredFeature, List.of(modifiers));
        BiomeModifications.addFeature(selector, category, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
        return entry;
    }
}
