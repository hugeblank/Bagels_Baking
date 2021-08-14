package dev.elexi.hugeblank.bagels_baking.state;

import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;

public class BakingProperties {

    public static final EnumProperty<AdjacentPosition> ADJACENT = EnumProperty.of("adjacent", AdjacentPosition.class);
}
