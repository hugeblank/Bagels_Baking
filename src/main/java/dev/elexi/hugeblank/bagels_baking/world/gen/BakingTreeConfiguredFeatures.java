package dev.elexi.hugeblank.bagels_baking.world.gen;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.util.WoodBlock;
import dev.elexi.hugeblank.bagels_baking.world.gen.placer.JuniperFoliagePlacer;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public class BakingTreeConfiguredFeatures {
    private static final BeehiveTreeDecorator BEES_002 = new BeehiveTreeDecorator(0.02F);
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> CHERRY_TREE;
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> CHERRY_TREE_BEES_002;
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> LEMON_TREE;
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> LEMON_TREE_BEES_002;

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> JUNIPER_TREE;

    public static final RegistryEntry<ConfiguredFeature<SimpleBlockFeatureConfig, ?>> CINNAMON_TREE;
    public static final RegistryEntry<ConfiguredFeature<SimpleBlockFeatureConfig, ?>> SMALL_CINNAMON_TREE;

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
        CHERRY_TREE = BakingConfiguredFeatures.register(
                "cherry_tree",
                Feature.TREE,
                cherry()
                        .build()
        );
        CHERRY_TREE_BEES_002 = BakingConfiguredFeatures.register(
                "cherry_tree_bees",
                Feature.TREE,
                cherry()
                        .decorators(List.of(BEES_002))
                        .build()
        );
        LEMON_TREE = BakingConfiguredFeatures.register(
                "lemon_tree",
                Feature.TREE,
                lemon()
                        .build()
        );
        LEMON_TREE_BEES_002 = BakingConfiguredFeatures.register(
                "lemon_tree_bees",
                Feature.TREE,
                lemon()
                        .decorators(List.of(BEES_002))
                        .build()
        );

        JUNIPER_TREE = BakingConfiguredFeatures.register(
                "juniper_tree",
                Feature.TREE,
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
        );

        CINNAMON_TREE = BakingConfiguredFeatures.register(
                "cinnamon_tree",
                Feature.SIMPLE_BLOCK,
                new SimpleBlockFeatureConfig(BlockStateProvider.of(Baking.CINNAMON_TREE))
        );
        SMALL_CINNAMON_TREE = BakingConfiguredFeatures.register(
                "small_cinnamon_tree",
                Feature.SIMPLE_BLOCK,
                new SimpleBlockFeatureConfig(BlockStateProvider.of(Baking.SMALL_CINNAMON_TREE))
        );
    }
}
