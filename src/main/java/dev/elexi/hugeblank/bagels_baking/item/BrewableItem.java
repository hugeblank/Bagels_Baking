package dev.elexi.hugeblank.bagels_baking.item;

public interface BrewableItem {
    default boolean isBrewable() {
        return false;
    }
}
