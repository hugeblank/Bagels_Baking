package dev.elexi.hugeblank.bagels_baking.world.gen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.world.gen.placer.TriplePlantPlacer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.HeightmapDecoratorConfig;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.placer.DoublePlantPlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.function.Predicate;

public class BakingConfiguredFeatures {

    private static final ConfiguredFeature<?, ?> HALITE = registerInBiomes("halite", Feature.ORE
            .configure(new OreFeatureConfig(
                    OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
                    Baking.HALITE.getDefaultState(),
                    33)
            )
            .decorate(Decorator.RANGE.configure(
                    new RangeDecoratorConfig(
                            UniformHeightProvider.create(
                                    YOffset.fixed(0),
                                    YOffset.fixed(79)
                            )
                    )
            ))
            .spreadHorizontally()
            .repeat(10),
            BiomeSelectors.includeByKey( // Deserts, Oceans, and Rivers
                    BiomeKeys.DESERT, BiomeKeys.DESERT_HILLS, BiomeKeys.DESERT_LAKES, BiomeKeys.BADLANDS,
                    BiomeKeys.BADLANDS_PLATEAU, BiomeKeys.MODIFIED_BADLANDS_PLATEAU, BiomeKeys.ERODED_BADLANDS,
                    BiomeKeys.MODIFIED_WOODED_BADLANDS_PLATEAU, BiomeKeys.WOODED_BADLANDS_PLATEAU, BiomeKeys.OCEAN,
                    BiomeKeys.COLD_OCEAN, BiomeKeys.DEEP_COLD_OCEAN, BiomeKeys.DEEP_FROZEN_OCEAN,
                    BiomeKeys.DEEP_LUKEWARM_OCEAN, BiomeKeys.DEEP_OCEAN, BiomeKeys.DEEP_WARM_OCEAN, BiomeKeys.FROZEN_OCEAN,
                    BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.WARM_OCEAN, BiomeKeys.RIVER, BiomeKeys.FROZEN_RIVER
            ),
            GenerationStep.Feature.UNDERGROUND_ORES
    ); // number of veins per chunk
    public static final ConfiguredFeature<?, ?> TEA_TREES = registerInBiomes("tea", Feature.RANDOM_PATCH
            .configure(new RandomPatchFeatureConfig.Builder(
                    new SimpleBlockStateProvider(Baking.TEA.getDefaultState().with(SweetBerryBushBlock.AGE, 3)), SimpleBlockPlacer.INSTANCE)
                    .tries(64).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).cannotProject().build())
            .decorate(Decorator.HEIGHTMAP_SPREAD_DOUBLE
                    .configure(new HeightmapDecoratorConfig(Heightmap.Type.MOTION_BLOCKING))
                    .spreadHorizontally()
            ),
            BiomeSelectors.includeByKey( // Mountain & Birch biomes
                    BiomeKeys.MOUNTAIN_EDGE, BiomeKeys.MOUNTAINS, BiomeKeys.GRAVELLY_MOUNTAINS,
                    BiomeKeys.MODIFIED_GRAVELLY_MOUNTAINS, BiomeKeys.BIRCH_FOREST, BiomeKeys.BIRCH_FOREST_HILLS,
                    BiomeKeys.TALL_BIRCH_FOREST, BiomeKeys.TALL_BIRCH_HILLS
            ),
            GenerationStep.Feature.VEGETAL_DECORATION
    );
    public static final ConfiguredFeature<TreeFeatureConfig, ?> CHERRY_TREE = register("cherry_tree", Feature.TREE
            .configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(Baking.CHERRY_LOG.getDefaultState()), new StraightTrunkPlacer(4, 2, 0), new SimpleBlockStateProvider(Baking.CHERRY_LEAVES.getDefaultState()), new SimpleBlockStateProvider(Baking.CHERRY_SAPLING.getDefaultState()), new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3), new TwoLayersFeatureSize(1, 0, 1)))
                    .ignoreVines()
                    .build())
    );
    public static final ConfiguredFeature<TreeFeatureConfig, ?> CHERRY_TREE_BEES = register("cherry_tree_bees", Feature.TREE
            .configure(CHERRY_TREE.getConfig().setTreeDecorators(ImmutableList.of(new BeehiveTreeDecorator(0.05F))))
    );
    public static final ConfiguredFeature<?, ?> CHERRY_TREES = register("cherry_trees", Feature.RANDOM_SELECTOR
            .configure(new RandomFeatureConfig(ImmutableList.of(CHERRY_TREE_BEES.withChance(0.8F), ConfiguredFeatures.OAK_BEES_005.withChance(0.05F)), BakingConfiguredFeatures.CHERRY_TREE))
            .decorate(Decorator.HEIGHTMAP
                    .configure(new HeightmapDecoratorConfig(Heightmap.Type.MOTION_BLOCKING))
                    .spreadHorizontally())
            .decorate(Decorator.COUNT_EXTRA
                    .configure(new CountExtraDecoratorConfig(10, 0.1F, 1)))
    );

    public static final ConfiguredFeature<TreeFeatureConfig, ?> LEMON_TREE = register("lemon_tree", Feature.TREE
            .configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(Baking.LEMON_LOG.getDefaultState()), new StraightTrunkPlacer(4, 2, 0), new SimpleBlockStateProvider(Baking.LEMON_LEAVES.getDefaultState()), new SimpleBlockStateProvider(Baking.LEMON_SAPLING.getDefaultState()), new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3), new TwoLayersFeatureSize(1, 0, 1)))
                    .ignoreVines()
                    .build())
    );
    public static final ConfiguredFeature<TreeFeatureConfig, ?> LEMON_TREE_BEES = register("lemon_tree_bees", Feature.TREE
            .configure(LEMON_TREE.getConfig().setTreeDecorators(ImmutableList.of(new BeehiveTreeDecorator(0.05F))))
    );
    public static final ConfiguredFeature<?, ?> LEMON_TREES = register("lemon_trees", Feature.RANDOM_SELECTOR
            .configure(new RandomFeatureConfig(ImmutableList.of(LEMON_TREE_BEES.withChance(0.8F), ConfiguredFeatures.OAK_BEES_005.withChance(0.05F)), LEMON_TREE))
            .decorate(Decorator.HEIGHTMAP
                    .configure(new HeightmapDecoratorConfig(Heightmap.Type.MOTION_BLOCKING))
                    .spreadHorizontally())
            .decorate(Decorator.COUNT_EXTRA
                    .configure(new CountExtraDecoratorConfig(10, 0.1F, 1)))
    );

    public static final ConfiguredFeature<?, ?> CINNAMON_TREE = register("cinnamon_tree", Feature.RANDOM_PATCH
            .configure(new RandomPatchFeatureConfig.Builder(
                    new SimpleBlockStateProvider(Baking.CINNAMON_TREE.getDefaultState()), TriplePlantPlacer.INSTANCE)
                    .tries(16).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).cannotProject().build()
            )
    );

    public static final ConfiguredFeature<?, ?> SMALL_CINNAMON_TREE = register("small_cinnamon_tree", Feature.RANDOM_PATCH
            .configure(new RandomPatchFeatureConfig.Builder(
                    new SimpleBlockStateProvider(Baking.SMALL_CINNAMON_TREE.getDefaultState()), DoublePlantPlacer.INSTANCE)
                    .tries(16).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).cannotProject().build()
            )
    );

    public static final ConfiguredFeature<?, ?> CINNAMON_TREES = registerInBiomes("cinnamon_trees", Feature.RANDOM_SELECTOR
            .configure(new RandomFeatureConfig(ImmutableList.of(CINNAMON_TREE.withChance(0.4f)), SMALL_CINNAMON_TREE))
            .decorate(Decorator.HEIGHTMAP
                    .configure(new HeightmapDecoratorConfig(Heightmap.Type.MOTION_BLOCKING))
                    .spreadHorizontally()
            ),
            BiomeSelectors.includeByKey( // Mountain & Birch biomes
                    BiomeKeys.JUNGLE, BiomeKeys.JUNGLE_EDGE, BiomeKeys.JUNGLE_HILLS,
                    BiomeKeys.BAMBOO_JUNGLE, BiomeKeys.BAMBOO_JUNGLE_HILLS, BiomeKeys.MODIFIED_JUNGLE,
                    BiomeKeys.MODIFIED_JUNGLE_EDGE
            ),
            GenerationStep.Feature.VEGETAL_DECORATION
    );

    public static void init() {}

    private static <FC extends FeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> register(String name, ConfiguredFeature<FC, F> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(Baking.ID, name), configuredFeature);
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> registerInBiomes(String name, ConfiguredFeature<?,?> feature, Predicate<BiomeSelectionContext> selector, GenerationStep.Feature category) {
        RegistryKey<ConfiguredFeature<?, ?>> key = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
                new Identifier(Baking.ID, name));

        BiomeModifications.addFeature(selector, category, key);
        return (ConfiguredFeature<FC, F>) Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, key.getValue(), feature);
    }
}
