package dev.elexi.hugeblank.bagels_baking.util;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;

public class BakingProperties {

    public static final EnumProperty<AdjacentPosition> ADJACENT = EnumProperty.of("adjacent", AdjacentPosition.class);
    public static final IntProperty DISTANCE_0_5 = IntProperty.of("distance", 0, 5);
    public static final EnumProperty<TripleBlockThird> TRIPLE_BLOCK_THIRD = EnumProperty.of("third", TripleBlockThird.class);
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
}
