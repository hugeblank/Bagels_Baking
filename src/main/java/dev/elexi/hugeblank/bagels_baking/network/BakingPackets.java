package dev.elexi.hugeblank.bagels_baking.network;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.util.Identifier;

public class BakingPackets {
    // not a lot here... yet :)
    public static final Identifier TOMATO_PACKET = new Identifier(Baking.ID, "tomato_spawn_packet");
    public static final Identifier BOAT_PACKET = new Identifier(Baking.ID, "boat_spawn_packet");

    public static void init() {}
}
