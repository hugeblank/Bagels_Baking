package dev.elexi.hugeblank.bagels_baking.world.gen;

import com.google.common.collect.ImmutableList;
import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;

public class BakingClusterConfiguredFeatures {
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_ORCHARD; // Cherry Orchard
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_GROVE; // Lemon Grove
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_TAIGA; // Juniper Taiga
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_TAIGA_SNOWY; // Snowy Juniper Taiga
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PATCH_CINNAMON;
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PATCH_TEA;
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> ORE_HALITE;

    public static void init() {}

    static {
        TREES_ORCHARD = BakingConfiguredFeatures.register(
                "trees_orchard",
                Feature.RANDOM_SELECTOR,
                new RandomFeatureConfig(ImmutableList.of(
                        new RandomFeatureEntry(BakingTreePlacedFeatures.CHERRY_BEES_002, 0.8F),
                        new RandomFeatureEntry(TreePlacedFeatures.OAK_BEES_0002, 0.05F)
                ), BakingTreePlacedFeatures.CHERRY_CHECKED)
        );
        TREES_GROVE = BakingConfiguredFeatures.register(
                "trees_grove",
                Feature.RANDOM_SELECTOR,
                new RandomFeatureConfig(ImmutableList.of(
                        new RandomFeatureEntry(BakingTreePlacedFeatures.LEMON_BEES_002, 0.8F),
                        new RandomFeatureEntry(TreePlacedFeatures.OAK_BEES_0002, 0.05F)
                ), BakingTreePlacedFeatures.LEMON_CHECKED)
        );
        TREES_TAIGA = BakingConfiguredFeatures.register(
                "trees_taiga",
                Feature.RANDOM_SELECTOR,
                new RandomFeatureConfig(ImmutableList.of(
                        new RandomFeatureEntry(BakingTreePlacedFeatures.JUNIPER_CHECKED, 0.1F),
                        new RandomFeatureEntry(TreePlacedFeatures.PINE_CHECKED, 0.25F),
                        new RandomFeatureEntry(TreePlacedFeatures.SPRUCE_CHECKED, 0.25F)
                ), BakingTreePlacedFeatures.JUNIPER_CHECKED)
        );
        TREES_TAIGA_SNOWY = BakingConfiguredFeatures.register(
                "trees_taiga_snowy",
                Feature.RANDOM_SELECTOR,
                new RandomFeatureConfig(ImmutableList.of(
                        new RandomFeatureEntry(BakingTreePlacedFeatures.JUNIPER_ON_SNOW, 0.1F),
                        new RandomFeatureEntry(TreePlacedFeatures.PINE_ON_SNOW, 0.25F),
                        new RandomFeatureEntry(TreePlacedFeatures.SPRUCE_ON_SNOW, 0.25F)
                ), BakingTreePlacedFeatures.JUNIPER_ON_SNOW)
        );
        PATCH_CINNAMON = BakingConfiguredFeatures.register(
                "vegetation_cinnamon",
                Feature.RANDOM_PATCH,
                ConfiguredFeatures.createRandomPatchFeatureConfig(
                        Feature.RANDOM_SELECTOR,
                        new RandomFeatureConfig(
                                ImmutableList.of(
                                        new RandomFeatureEntry(BakingTreePlacedFeatures.CINNAMON_CHECKED, 0.4f)
                                ),
                                BakingTreePlacedFeatures.SMALL_CINNAMON_CHECKED
                        ),
                        List.of(Blocks.GRASS_BLOCK)
                )
        );
        PATCH_TEA = BakingConfiguredFeatures.register(
                "patch_tea",
                Feature.RANDOM_PATCH,
                ConfiguredFeatures.createRandomPatchFeatureConfig(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(
                                BlockStateProvider.of(
                                        Baking.TEA.getDefaultState().with(Properties.AGE_3, 3)
                                )
                        ),
                        List.of(Blocks.GRASS_BLOCK)
                )
        );
        ORE_HALITE = BakingConfiguredFeatures.register(
                "ore_halite",
                Feature.ORE,
                new OreFeatureConfig(
                        OreConfiguredFeatures.BASE_STONE_OVERWORLD,
                        Baking.HALITE.getDefaultState(),
                        33
                )
        );
    }
}
