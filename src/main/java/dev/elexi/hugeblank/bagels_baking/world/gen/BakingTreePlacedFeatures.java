package dev.elexi.hugeblank.bagels_baking.world.gen;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.TreePlacedFeatures;

public class BakingTreePlacedFeatures {
    public static final RegistryEntry<PlacedFeature> CHERRY_CHECKED;
    public static final RegistryEntry<PlacedFeature> CHERRY_BEES_002;
    public static final RegistryEntry<PlacedFeature> LEMON_CHECKED;
    public static final RegistryEntry<PlacedFeature> LEMON_BEES_002;
    public static final RegistryEntry<PlacedFeature> JUNIPER_CHECKED;
    public static final RegistryEntry<PlacedFeature> JUNIPER_ON_SNOW;
    public static final RegistryEntry<PlacedFeature> SMALL_CINNAMON_CHECKED;
    public static final RegistryEntry<PlacedFeature> CINNAMON_CHECKED;

    public static void init() {}

    static {
        CHERRY_CHECKED = PlacedFeatures.register(
                "cherry_checked",
                BakingTreeConfiguredFeatures.CHERRY_TREE,
                PlacedFeatures.wouldSurvive(Baking.CHERRY_SAPLING)
        );
        CHERRY_BEES_002 = PlacedFeatures.register(
                "cherry_bees_002",
                BakingTreeConfiguredFeatures.CHERRY_TREE_BEES_002,
                PlacedFeatures.wouldSurvive(Baking.CHERRY_SAPLING)
        );
        LEMON_CHECKED = PlacedFeatures.register(
                "lemon_checked",
                BakingTreeConfiguredFeatures.LEMON_TREE,
                PlacedFeatures.wouldSurvive(Baking.LEMON_SAPLING)
        );
        LEMON_BEES_002 = PlacedFeatures.register(
                "lemon_bees_002",
                BakingTreeConfiguredFeatures.LEMON_TREE_BEES_002,
                PlacedFeatures.wouldSurvive(Baking.LEMON_SAPLING)
        );
        JUNIPER_CHECKED = PlacedFeatures.register(
                "juniper_checked",
                BakingTreeConfiguredFeatures.JUNIPER_TREE,
                PlacedFeatures.wouldSurvive(Baking.JUNIPER_SAPLING)
        );
        JUNIPER_ON_SNOW = PlacedFeatures.register(
                "juniper_on_snow",
                BakingTreeConfiguredFeatures.JUNIPER_TREE,
                TreePlacedFeatures.ON_SNOW_MODIFIERS
        );
        SMALL_CINNAMON_CHECKED = PlacedFeatures.register(
                "small_cinnamon_checked",
                BakingTreeConfiguredFeatures.SMALL_CINNAMON_TREE,
                PlacedFeatures.wouldSurvive(Baking.CINNAMON_SAPLING)
        );
        CINNAMON_CHECKED = PlacedFeatures.register(
                "cinnamon_checked",
                BakingTreeConfiguredFeatures.CINNAMON_TREE,
                PlacedFeatures.wouldSurvive(Baking.CINNAMON_SAPLING)
        );
    }
}
