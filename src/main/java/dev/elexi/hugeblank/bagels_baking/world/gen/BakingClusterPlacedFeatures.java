package dev.elexi.hugeblank.bagels_baking.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationConfiguredFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

import java.util.List;

import static net.minecraft.world.gen.feature.PlacedFeatures.createCountExtraModifier;

public class BakingClusterPlacedFeatures {
    public static final PlacedFeature TREES_ORCHARD; // Cherry Orchard
    public static final PlacedFeature TREES_GROVE; // Lemon Grove
    public static final PlacedFeature TREES_TAIGA; // Juniper Taiga
    public static final PlacedFeature TREES_TAIGA_SNOWY; // Snowy Juniper Taiga
    public static final PlacedFeature TREES_BIRCH_AND_OAK_SPARSE;
    public static final PlacedFeature PATCH_CINNAMON;
    public static final PlacedFeature PATCH_TEA;
    public static final PlacedFeature ORE_HALITE;

    private static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }

    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
        return modifiers(CountPlacementModifier.of(count), heightModifier);
    }

    private static List<PlacementModifier> modifiersWithRarity(int chance, PlacementModifier heightModifier) {
        // TODO: 0.4.1 - Bring parity with other stone blobs.
        return modifiers(RarityFilterPlacementModifier.of(chance), heightModifier);
    }

    static {
        TREES_ORCHARD = BakingPlacedFeatures.register(
                "trees_orchard",
                BakingClusterConfiguredFeatures.TREES_ORCHARD.withPlacement(VegetationPlacedFeatures.modifiers(
                        createCountExtraModifier(5, 0.1F, 1))
                )
        );
        TREES_GROVE = BakingPlacedFeatures.register(
                "trees_grove",
                BakingClusterConfiguredFeatures.TREES_GROVE.withPlacement(VegetationPlacedFeatures.modifiers(
                        createCountExtraModifier(5, 0.1F, 1))
                )
        );
        TREES_TAIGA = BakingPlacedFeatures.register(
                "trees_taiga",
                BakingClusterConfiguredFeatures.TREES_TAIGA.withPlacement(VegetationPlacedFeatures.modifiers(
                        createCountExtraModifier(5, 0.1F, 1))
                )
        );
        TREES_TAIGA_SNOWY = BakingPlacedFeatures.register(
                "trees_taiga_snowy",
                BakingClusterConfiguredFeatures.TREES_TAIGA_SNOWY.withPlacement(VegetationPlacedFeatures.modifiers(
                        createCountExtraModifier(5, 0.1F, 1))
                )
        );
        TREES_BIRCH_AND_OAK_SPARSE = BakingPlacedFeatures.register(
                "trees_birch_and_oak_sparse",
                VegetationConfiguredFeatures.TREES_BIRCH_AND_OAK.withPlacement(VegetationPlacedFeatures.modifiers(
                        createCountExtraModifier(5, 0.1F, 1))
                )
        );
        PATCH_CINNAMON = BakingPlacedFeatures.registerInBiomes(
                "patch_cinnamon",
                BakingClusterConfiguredFeatures.PATCH_CINNAMON.withPlacement(
                        RarityFilterPlacementModifier.of(32),
                        SquarePlacementModifier.of(),
                        PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                        BiomePlacementModifier.of()
                ),
                BiomeSelectors.includeByKey( // Jungle biomes
                        BiomeKeys.JUNGLE, BiomeKeys.SPARSE_JUNGLE, BiomeKeys.BAMBOO_JUNGLE
                ),
                GenerationStep.Feature.VEGETAL_DECORATION
        );
        PATCH_TEA = BakingPlacedFeatures.registerInBiomes(
                "patch_tea",
                BakingClusterConfiguredFeatures.PATCH_TEA.withPlacement(
                        RarityFilterPlacementModifier.of(32),
                        SquarePlacementModifier.of(),
                        PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                        BiomePlacementModifier.of()
                ),
                BiomeSelectors.includeByKey( // Mountain & Birch biomes
                        BiomeKeys.STONY_PEAKS, BiomeKeys.SNOWY_SLOPES, BiomeKeys.JAGGED_PEAKS,
                        BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.BIRCH_FOREST
                ),
                GenerationStep.Feature.VEGETAL_DECORATION
        );
        ORE_HALITE = BakingPlacedFeatures.registerInBiomes(
                "ore_halite",
                BakingClusterConfiguredFeatures.ORE_HALITE.withPlacement(modifiersWithCount(
                        10, // # of veins per chunk
                        HeightRangePlacementModifier.uniform(
                                YOffset.fixed(0),
                                YOffset.fixed(79)
                        )
                )),
                BiomeSelectors.includeByKey( // Deserts, Oceans, and Rivers
                        BiomeKeys.DESERT, BiomeKeys.BADLANDS, BiomeKeys.ERODED_BADLANDS,
                        BiomeKeys.WOODED_BADLANDS, BiomeKeys.OCEAN, BiomeKeys.COLD_OCEAN,
                        BiomeKeys.DEEP_COLD_OCEAN, BiomeKeys.DEEP_FROZEN_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN,
                        BiomeKeys.DEEP_OCEAN, BiomeKeys.FROZEN_OCEAN, BiomeKeys.LUKEWARM_OCEAN,
                        BiomeKeys.WARM_OCEAN, BiomeKeys.RIVER, BiomeKeys.FROZEN_RIVER
                ),
                GenerationStep.Feature.UNDERGROUND_ORES
        );

    }

    public static void init() {}
}
