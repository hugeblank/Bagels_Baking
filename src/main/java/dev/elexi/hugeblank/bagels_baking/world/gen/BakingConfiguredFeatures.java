package dev.elexi.hugeblank.bagels_baking.world.gen;

import com.google.common.collect.ImmutableList;
import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.util.WoodBlock;
import dev.elexi.hugeblank.bagels_baking.world.gen.placer.JuniperFoliagePlacer;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public class BakingConfiguredFeatures {
    private static final BeehiveTreeDecorator BEES_002 = new BeehiveTreeDecorator(0.02F);
    public static final ConfiguredFeature<TreeFeatureConfig, ?> CHERRY_TREE;
    public static final ConfiguredFeature<TreeFeatureConfig, ?> CHERRY_TREE_BEES_002;
    public static final ConfiguredFeature<RandomFeatureConfig, ?> TREES_ORCHARD; // Cherry Orchard
    public static final ConfiguredFeature<TreeFeatureConfig, ?> LEMON_TREE;
    public static final ConfiguredFeature<TreeFeatureConfig, ?> LEMON_TREE_BEES_002;
    public static final ConfiguredFeature<RandomFeatureConfig, ?> TREES_GROVE; // Lemon Grove
    public static final ConfiguredFeature<TreeFeatureConfig, ?> JUNIPER_TREE;
    public static final ConfiguredFeature<RandomFeatureConfig, ?> TREES_TAIGA; // Juniper Taiga
    public static final ConfiguredFeature<RandomFeatureConfig, ?> TREES_TAIGA_SNOWY; // Snowy Juniper Taiga
    public static final ConfiguredFeature<SimpleBlockFeatureConfig, ?> CINNAMON_TREE;
    public static final ConfiguredFeature<SimpleBlockFeatureConfig, ?> SMALL_CINNAMON_TREE;
    public static final ConfiguredFeature<RandomPatchFeatureConfig, ?> PATCH_CINNAMON;
    public static final ConfiguredFeature<RandomPatchFeatureConfig, ?> PATCH_TEA;
    public static final ConfiguredFeature<OreFeatureConfig, ?> ORE_HALITE;

    private static TreeFeatureConfig.Builder cherry() {
        return new TreeFeatureConfig.Builder(
                BlockStateProvider.of(Baking.CHERRY_WOOD_TYPE.getBlock(WoodBlock.LOG).getDefaultState()),
                new StraightTrunkPlacer(4, 2, 0),
                BlockStateProvider.of(Baking.CHERRY_WOOD_TYPE.getBlock(WoodBlock.LEAVES).getDefaultState()),
                new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        ).ignoreVines();
    }

    private static TreeFeatureConfig.Builder lemon() {
        return new TreeFeatureConfig.Builder(
                BlockStateProvider.of(Baking.LEMON_WOOD_TYPE.getBlock(WoodBlock.LOG).getDefaultState()),
                new StraightTrunkPlacer(4, 2, 0),
                BlockStateProvider.of(Baking.LEMON_WOOD_TYPE.getBlock(WoodBlock.LEAVES).getDefaultState()),
                new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        ).ignoreVines();
    }

    public static void init() {}

    static {
        CHERRY_TREE = register(
                "cherry_tree",
                Feature.TREE.configure(
                    cherry()
                    .build()
                )
        );
        CHERRY_TREE_BEES_002 = register("cherry_tree_bees",
                Feature.TREE.configure(
                        cherry()
                        .decorators(List.of(BEES_002))
                        .build()
                )
        );
        TREES_ORCHARD = register(
                "cherry_trees",
                Feature.RANDOM_SELECTOR.configure(
                        new RandomFeatureConfig(ImmutableList.of(
                                new RandomFeatureEntry(BakingPlacedFeatures.CHERRY_BEES_002, 0.8F),
                                new RandomFeatureEntry(TreePlacedFeatures.OAK_BEES_0002, 0.05F)
                        ), BakingPlacedFeatures.CHERRY_CHECKED)
                )
        );
        LEMON_TREE = register(
                "lemon_tree",
                Feature.TREE.configure(lemon()
                        .build()
                )
        );
        LEMON_TREE_BEES_002 = register(
                "lemon_tree_bees",
                Feature.TREE.configure(
                        lemon()
                        .decorators(List.of(BEES_002))
                        .build()
                )
        );
        TREES_GROVE = register(
                "lemon_trees",
                Feature.RANDOM_SELECTOR.configure(
                        new RandomFeatureConfig(ImmutableList.of(
                                new RandomFeatureEntry(BakingPlacedFeatures.LEMON_BEES_002, 0.8F),
                                new RandomFeatureEntry(TreePlacedFeatures.OAK_BEES_0002, 0.05F)
                        ), BakingPlacedFeatures.LEMON_CHECKED))
        );
        JUNIPER_TREE = register(
                "juniper_tree",
                Feature.TREE.configure(
                        new TreeFeatureConfig.Builder(
                                BlockStateProvider.of(Baking.JUNIPER_WOOD_TYPE.getBlock(WoodBlock.LOG)),
                                new StraightTrunkPlacer(10, 3, 0),
                                BlockStateProvider.of(Baking.JUNIPER_WOOD_TYPE.getBlock(WoodBlock.LEAVES)),
                                new JuniperFoliagePlacer(
                                        ConstantIntProvider.create(2),
                                        ConstantIntProvider.create(1),
                                        ConstantIntProvider.create(10)
                                ),
                                new TwoLayersFeatureSize(1, 0, 1)
                        ).ignoreVines().build()
                )
        );
        TREES_TAIGA = register(
                "juniper_trees",
                Feature.RANDOM_SELECTOR.configure(
                        new RandomFeatureConfig(ImmutableList.of(
                                new RandomFeatureEntry(BakingPlacedFeatures.JUNIPER_CHECKED, 0.1F),
                                new RandomFeatureEntry(TreePlacedFeatures.PINE_CHECKED, 0.25F),
                                new RandomFeatureEntry(TreePlacedFeatures.SPRUCE_CHECKED, 0.25F)
                        ), BakingPlacedFeatures.JUNIPER_CHECKED)
                )
        );
        TREES_TAIGA_SNOWY = register(
                "juniper_trees_snowy",
                Feature.RANDOM_SELECTOR.configure(
                        new RandomFeatureConfig(ImmutableList.of(
                                new RandomFeatureEntry(BakingPlacedFeatures.JUNIPER_ON_SNOW, 0.1F),
                                new RandomFeatureEntry(TreePlacedFeatures.PINE_ON_SNOW, 0.25F),
                                new RandomFeatureEntry(TreePlacedFeatures.SPRUCE_ON_SNOW, 0.25F)
                        ), BakingPlacedFeatures.JUNIPER_ON_SNOW)
                )
        );
        CINNAMON_TREE = register(
                "cinnamon_tree",
                Feature.SIMPLE_BLOCK.configure(
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(Baking.CINNAMON_TREE))
                )
        );
        SMALL_CINNAMON_TREE = register(
                "small_cinnamon_tree",
                Feature.SIMPLE_BLOCK.configure(
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(Baking.SMALL_CINNAMON_TREE))
                )
        );
        PATCH_CINNAMON = register(
                "vegetation_cinnamon",
                Feature.RANDOM_PATCH.configure(
                        ConfiguredFeatures.createRandomPatchFeatureConfig(
                                Feature.RANDOM_SELECTOR.configure(
                                        new RandomFeatureConfig(
                                                ImmutableList.of(
                                                        new RandomFeatureEntry(BakingPlacedFeatures.CINNAMON_CHECKED, 0.4f)
                                                ),
                                                BakingPlacedFeatures.SMALL_CINNAMON_CHECKED
                                        )
                                ),
                                List.of(Blocks.GRASS_BLOCK)
                        )
                )
        );
        PATCH_TEA = register(
                "patch_tea",
                Feature.RANDOM_PATCH.configure(
                        ConfiguredFeatures.createRandomPatchFeatureConfig(
                                Feature.SIMPLE_BLOCK.configure(
                                        new SimpleBlockFeatureConfig(
                                                BlockStateProvider.of(
                                                        Baking.TEA.getDefaultState().with(Properties.AGE_3, 3)
                                                ))
                                ),
                                List.of(Blocks.GRASS_BLOCK)
                        )
                )
        );
        ORE_HALITE = register(
                "ore_halite",
                Feature.ORE.configure(
                        new OreFeatureConfig(
                                OreConfiguredFeatures.BASE_STONE_OVERWORLD,
                                Baking.HALITE.getDefaultState(),
                                33
                        )
                )
        );
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> register(String name, ConfiguredFeature<FC, F> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(Baking.ID, name), configuredFeature);
    }
}
