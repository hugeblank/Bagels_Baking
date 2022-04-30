package dev.elexi.hugeblank.bagels_baking.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationConfiguredFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;

import static net.minecraft.world.gen.feature.PlacedFeatures.createCountExtraModifier;

public class BakingClusterPlacedFeatures {
    public static final RegistryEntry<PlacedFeature> TREES_ORCHARD; // Cherry Orchard
    public static final RegistryEntry<PlacedFeature> TREES_GROVE; // Lemon Grove
    public static final RegistryEntry<PlacedFeature> TREES_TAIGA; // Juniper Taiga
    public static final RegistryEntry<PlacedFeature> TREES_TAIGA_SNOWY; // Snowy Juniper Taiga
    public static final RegistryEntry<PlacedFeature> TREES_BIRCH_AND_OAK_SPARSE;
    public static final RegistryEntry<PlacedFeature> PATCH_CINNAMON;
    public static final RegistryEntry<PlacedFeature> PATCH_TEA;
    public static final RegistryEntry<PlacedFeature> ORE_HALITE;

    static {
        TREES_ORCHARD = BakingPlacedFeatures.register(
                "trees_orchard",
                BakingClusterConfiguredFeatures.TREES_ORCHARD,
                VegetationPlacedFeatures.modifiers(createCountExtraModifier(5, 0.1F, 1))
        );
        TREES_GROVE = BakingPlacedFeatures.register(
                "trees_grove",
                BakingClusterConfiguredFeatures.TREES_GROVE,
                VegetationPlacedFeatures.modifiers(createCountExtraModifier(5, 0.1F, 1))
        );
        TREES_TAIGA = BakingPlacedFeatures.register(
                "trees_taiga",
                BakingClusterConfiguredFeatures.TREES_TAIGA,
                VegetationPlacedFeatures.modifiers(createCountExtraModifier(5, 0.1F, 1))
        );
        TREES_TAIGA_SNOWY = BakingPlacedFeatures.register(
                "trees_taiga_snowy",
                BakingClusterConfiguredFeatures.TREES_TAIGA_SNOWY,
                VegetationPlacedFeatures.modifiers(createCountExtraModifier(5, 0.1F, 1))
        );
        TREES_BIRCH_AND_OAK_SPARSE = BakingPlacedFeatures.register(
                "trees_birch_and_oak_sparse",
                VegetationConfiguredFeatures.TREES_BIRCH_AND_OAK,
                VegetationPlacedFeatures.modifiers(createCountExtraModifier(5, 0.1F, 1))
        );
        PATCH_CINNAMON = BakingPlacedFeatures.registerInBiomes(
                "patch_cinnamon",
                BakingClusterConfiguredFeatures.PATCH_CINNAMON,
                BiomeSelectors.includeByKey( // Jungle biomes
                        BiomeKeys.JUNGLE, BiomeKeys.SPARSE_JUNGLE, BiomeKeys.BAMBOO_JUNGLE
                ),
                GenerationStep.Feature.VEGETAL_DECORATION,
                RarityFilterPlacementModifier.of(32),
                SquarePlacementModifier.of(),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of()
        );
        PATCH_TEA = BakingPlacedFeatures.registerInBiomes(
                "patch_tea",
                BakingClusterConfiguredFeatures.PATCH_TEA,
                BiomeSelectors.includeByKey( // Mountain & Birch biomes
                        BiomeKeys.STONY_PEAKS, BiomeKeys.SNOWY_SLOPES, BiomeKeys.JAGGED_PEAKS,
                        BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.BIRCH_FOREST
                ),
                GenerationStep.Feature.VEGETAL_DECORATION,
                RarityFilterPlacementModifier.of(32),
                SquarePlacementModifier.of(),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of()
        );
        ORE_HALITE = BakingPlacedFeatures.registerInBiomes(
                "ore_halite",
                BakingClusterConfiguredFeatures.ORE_HALITE,
                BiomeSelectors.includeByKey( // Deserts, Oceans, and Rivers
                        BiomeKeys.DESERT, BiomeKeys.BADLANDS, BiomeKeys.ERODED_BADLANDS,
                        BiomeKeys.WOODED_BADLANDS, BiomeKeys.OCEAN, BiomeKeys.COLD_OCEAN,
                        BiomeKeys.DEEP_COLD_OCEAN, BiomeKeys.DEEP_FROZEN_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN,
                        BiomeKeys.DEEP_OCEAN, BiomeKeys.FROZEN_OCEAN, BiomeKeys.LUKEWARM_OCEAN,
                        BiomeKeys.WARM_OCEAN, BiomeKeys.RIVER, BiomeKeys.FROZEN_RIVER
                ),
                GenerationStep.Feature.UNDERGROUND_ORES,
                CountPlacementModifier.of(2), // # of veins per chunk
                HeightRangePlacementModifier.uniform(
                        YOffset.fixed(0),
                        YOffset.fixed(72)
                        // Vanilla stone ores in their "lower" form go up to y=60. I choose to go higher because I like
                        // the concept of some surface halite peeking out around the edges of rivers and along the base
                        // of mountains. ~ hugeblank
                ),
                BiomePlacementModifier.of()
        );

    }

    public static void init() {}
}
