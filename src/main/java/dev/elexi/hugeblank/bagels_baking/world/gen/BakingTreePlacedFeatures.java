package dev.elexi.hugeblank.bagels_baking.world.gen;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.TreePlacedFeatures;

public class BakingTreePlacedFeatures {
    public static final PlacedFeature CHERRY_CHECKED;
    public static final PlacedFeature CHERRY_BEES_002;
    public static final PlacedFeature LEMON_CHECKED;
    public static final PlacedFeature LEMON_BEES_002;
    public static final PlacedFeature JUNIPER_CHECKED;
    public static final PlacedFeature JUNIPER_ON_SNOW;
    public static final PlacedFeature SMALL_CINNAMON_CHECKED;
    public static final PlacedFeature CINNAMON_CHECKED;

    public static void init() {}

    static {
        CHERRY_CHECKED = BakingPlacedFeatures.register(
                "cherry_checked",
                BakingTreeConfiguredFeatures.CHERRY_TREE.withWouldSurviveFilter(Baking.CHERRY_SAPLING)
        );
        CHERRY_BEES_002 = BakingPlacedFeatures.register(
                "cherry_bees_002",
                BakingTreeConfiguredFeatures.CHERRY_TREE_BEES_002.withWouldSurviveFilter(Baking.CHERRY_SAPLING)
        );
        LEMON_CHECKED = BakingPlacedFeatures.register(
                "lemon_checked",
                BakingTreeConfiguredFeatures.LEMON_TREE.withWouldSurviveFilter(Baking.LEMON_SAPLING)
        );
        LEMON_BEES_002 = BakingPlacedFeatures.register(
                "lemon_bees_002",
                BakingTreeConfiguredFeatures.LEMON_TREE_BEES_002.withWouldSurviveFilter(Baking.LEMON_SAPLING)
        );
        JUNIPER_CHECKED = BakingPlacedFeatures.register(
                "juniper_checked",
                BakingTreeConfiguredFeatures.JUNIPER_TREE.withWouldSurviveFilter(Baking.JUNIPER_SAPLING)
        );
        JUNIPER_ON_SNOW = BakingPlacedFeatures.register(
                "juniper_on_snow",
                BakingTreeConfiguredFeatures.JUNIPER_TREE.withPlacement(TreePlacedFeatures.ON_SNOW_MODIFIERS)
        );
        SMALL_CINNAMON_CHECKED = BakingPlacedFeatures.register(
                "small_cinnamon_checked",
                BakingTreeConfiguredFeatures.SMALL_CINNAMON_TREE.withWouldSurviveFilter(Baking.CINNAMON_SAPLING)
        );
        CINNAMON_CHECKED = BakingPlacedFeatures.register(
                "cinnamon_checked",
                BakingTreeConfiguredFeatures.CINNAMON_TREE.withWouldSurviveFilter(Baking.CINNAMON_SAPLING)
        );
    }
}
