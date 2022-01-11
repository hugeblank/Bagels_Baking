package dev.elexi.hugeblank.bagels_baking.world.gen.placer;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.mixin.world.FoliagePlacerTypeAccessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class BakingPlacers {
    //public static final BlockPlacerType<TriplePlantPlacer> TRIPLE_BLOCK_PLACER_TYPE = Registry.register(Registry.BLOCK_PLACER_TYPE, new Identifier(Baking.ID, "triple_placer_type"), BlockPlacerTypeAccessor.newBlockPlacerType(TriplePlantPlacer.CODEC));
    public static final FoliagePlacerType<JuniperFoliagePlacer> JUNIPER_FOLIAGE_PLACER = Registry.register(Registry.FOLIAGE_PLACER_TYPE, new Identifier(Baking.ID, "juniper_foliage_placer_type"), FoliagePlacerTypeAccessor.newFoliagePlacerType(JuniperFoliagePlacer.CODEC));

    public void init() {}
}
