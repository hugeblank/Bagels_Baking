package dev.elexi.hugeblank.bagels_baking.util;

import net.minecraft.util.StringIdentifiable;

public enum TripleBlockThird implements StringIdentifiable {
    LOWER("lower"),
    MIDDLE("middle"),
    UPPER("upper");

    private final String name;

    TripleBlockThird(String name) {
        this.name = name;
    }

    public TripleBlockThird below(TripleBlockThird current) {
        return switch (current) {
            case UPPER -> MIDDLE;
            case MIDDLE -> LOWER;
            case LOWER -> null;
        };
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }
}
