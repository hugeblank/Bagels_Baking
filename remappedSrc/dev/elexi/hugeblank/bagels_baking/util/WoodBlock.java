package dev.elexi.hugeblank.bagels_baking.util;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;

// Wood blocks that are offered for any *type* of wood
public enum WoodBlock {
    LOG((name) -> name + "_log", 5, 5),
    WOOD((name) -> name + "_wood", 5, 5),
    STRIPPED_LOG((name) -> "stripped_" + name + "_log", 5, 5),
    STRIPPED_WOOD((name) -> "stripped_" + name + "_wood", 5, 5),
    LEAVES((name) -> name + "_leaves", 30, 60),
    PLANKS((name) -> name + "_planks", 5, 20),
    PRESSURE_PLATE((name) -> name + "_pressure_plate", 5, 20),
    BUTTON((name) -> name + "_button", 5, 20),
    DOOR((name) -> name + "_door", 5, 20),
    FENCE_GATE((name) -> name + "_fence_gate", 5, 20),
    FENCE((name) -> name + "_fence", 5, 20),
    SLAB((name) -> name + "_slab", 5, 20),
    STAIRS((name) -> name + "_stairs", 5, 20),
    TRAPDOOR((name) -> name + "_trapdoor", 5, 20),
    SIGN((name) -> name + "_sign", 5, 20),
    WALL_SIGN((name) -> name + "_wall_sign", 5, 20),
    TRELLIS((name) -> name + "_trellis", 5, 20);

    Function<String, String> toString;
    int burn;
    int spread;

    WoodBlock(Function<String, String> toString, int burn, int spread) {
        this.toString = toString;
        this.burn = burn;
        this.spread = spread;
    }

    public Identifier registerBlock(String variant, Block block) { // Register block & Flammability
        Identifier id = new Identifier(Baking.ID, toString.apply(variant));
        Registry.register(Registry.BLOCK, id, block);
        FlammableBlockRegistry.getDefaultInstance().add(block, this.burn, this.spread);
        return id;
    }
}