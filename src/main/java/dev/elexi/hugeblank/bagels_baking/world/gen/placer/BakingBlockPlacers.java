package dev.elexi.hugeblank.bagels_baking.world.gen.placer;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.mixin.world.BlockPlacerTypeAccessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.placer.BlockPlacerType;

public class BakingBlockPlacers {
    public static final BlockPlacerType<TriplePlantPlacer> TRIPLE_BLOCK_PLACER_TYPE = Registry.register(Registry.BLOCK_PLACER_TYPE, new Identifier(Baking.ID, "triple_placer_type"), BlockPlacerTypeAccessor.newBlockPlacerType(TriplePlantPlacer.CODEC));

    public void init() {}
}
