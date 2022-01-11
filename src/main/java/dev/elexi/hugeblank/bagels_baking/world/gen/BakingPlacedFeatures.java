package dev.elexi.hugeblank.bagels_baking.world.gen;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.*;

import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.world.gen.feature.PlacedFeatures.createCountExtraModifier;

public class BakingPlacedFeatures {
    public static final PlacedFeature CHERRY_CHECKED;
    public static final PlacedFeature CHERRY_BEES_002;
    public static final PlacedFeature TREES_ORCHARD; // Cherry Orchard
    public static final PlacedFeature LEMON_CHECKED;
    public static final PlacedFeature LEMON_BEES_002;
    public static final PlacedFeature TREES_GROVE; // Lemon Grove
    public static final PlacedFeature JUNIPER_CHECKED;
    public static final PlacedFeature JUNIPER_ON_SNOW;
    public static final PlacedFeature TREES_TAIGA; // Juniper Taiga
    public static final PlacedFeature TREES_TAIGA_SNOWY; // Snowy Juniper Taiga
    public static final PlacedFeature TREES_BIRCH_AND_OAK_SPARSE;
    public static final PlacedFeature SMALL_CINNAMON_CHECKED;
    public static final PlacedFeature CINNAMON_CHECKED;
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
        CHERRY_CHECKED = register(
                "cherry_checked",
                BakingConfiguredFeatures.CHERRY_TREE.withWouldSurviveFilter(Baking.CHERRY_SAPLING)
        );
        CHERRY_BEES_002 = register(
                "cherry_bees_002",
                BakingConfiguredFeatures.CHERRY_TREE_BEES_002.withWouldSurviveFilter(Baking.CHERRY_SAPLING)
        );
        TREES_ORCHARD = register(
                "trees_orchard",
                BakingConfiguredFeatures.TREES_ORCHARD.withPlacement(VegetationPlacedFeatures.modifiers(
                        createCountExtraModifier(5, 0.1F, 1))
                )
        );
        LEMON_CHECKED = register(
                "lemon_checked",
                BakingConfiguredFeatures.LEMON_TREE.withWouldSurviveFilter(Baking.LEMON_SAPLING)
        );
        LEMON_BEES_002 = register(
                "lemon_bees_002",
                BakingConfiguredFeatures.LEMON_TREE_BEES_002.withWouldSurviveFilter(Baking.LEMON_SAPLING)
        );
        TREES_GROVE = register(
                "trees_grove",
                BakingConfiguredFeatures.TREES_GROVE.withPlacement(VegetationPlacedFeatures.modifiers(
                        createCountExtraModifier(5, 0.1F, 1))
                )
        );
        JUNIPER_CHECKED = register(
                "juniper_checked",
                BakingConfiguredFeatures.JUNIPER_TREE.withWouldSurviveFilter(Baking.JUNIPER_SAPLING)
        );
        JUNIPER_ON_SNOW = register(
                "juniper_on_snow",
                BakingConfiguredFeatures.JUNIPER_TREE.withPlacement(TreePlacedFeatures.ON_SNOW_MODIFIERS)
        );
        TREES_TAIGA = register(
                "trees_taiga",
                BakingConfiguredFeatures.TREES_TAIGA.withPlacement(VegetationPlacedFeatures.modifiers(
                        createCountExtraModifier(5, 0.1F, 1))
                )
        );
        TREES_TAIGA_SNOWY = register(
                "trees_taiga_snowy",
                BakingConfiguredFeatures.TREES_TAIGA_SNOWY.withPlacement(VegetationPlacedFeatures.modifiers(
                        createCountExtraModifier(5, 0.1F, 1))
                )
        );
        TREES_BIRCH_AND_OAK_SPARSE = register(
                "trees_birch_and_oak_sparse",
                VegetationConfiguredFeatures.TREES_BIRCH_AND_OAK.withPlacement(VegetationPlacedFeatures.modifiers(
                        createCountExtraModifier(5, 0.1F, 1))
                )
        );
        SMALL_CINNAMON_CHECKED = register(
                "small_cinnamon_checked",
                BakingConfiguredFeatures.SMALL_CINNAMON_TREE.withWouldSurviveFilter(Baking.CINNAMON_SAPLING)
        );
        CINNAMON_CHECKED = register(
                "cinnamon_checked",
                BakingConfiguredFeatures.CINNAMON_TREE.withWouldSurviveFilter(Baking.CINNAMON_SAPLING)
        );
        PATCH_CINNAMON = registerInBiomes(
                "patch_cinnamon",
                BakingConfiguredFeatures.PATCH_CINNAMON.withPlacement(
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
        PATCH_TEA = registerInBiomes(
                "patch_tea",
                BakingConfiguredFeatures.PATCH_CINNAMON.withPlacement(
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
        ORE_HALITE = registerInBiomes(
                "ore_halite",
                BakingConfiguredFeatures.ORE_HALITE.withPlacement(modifiersWithCount(
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

    private static PlacedFeature register(String id, PlacedFeature feature) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(Baking.ID, id), feature);
    }

    private static PlacedFeature registerInBiomes(String name, PlacedFeature feature, Predicate<BiomeSelectionContext> selector, GenerationStep.Feature category) {
        RegistryKey<PlacedFeature> key = RegistryKey.of(Registry.PLACED_FEATURE_KEY,
                new Identifier(Baking.ID, name));
        BiomeModifications.addFeature(selector, category, key);
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, key.getValue(), feature);
    }
}
