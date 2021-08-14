package dev.elexi.hugeblank.bagels_baking.state;

import net.minecraft.util.StringIdentifiable;

public enum AdjacentPosition implements StringIdentifiable {
    NONE("none"),
    LEFT("left"),
    RIGHT("right"),
    BOTH("both");

    private final String name;

    AdjacentPosition(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }
}
