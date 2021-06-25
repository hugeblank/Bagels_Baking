package dev.elexi.hugeblank.bagels_baking.sprite;

import net.minecraft.client.util.SpriteIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SpriteRegistry {
    private static final List<SpriteIdentifier> ids = new ArrayList<>();

    public static void register(SpriteIdentifier id) {
        SpriteRegistry.ids.add(id);
    }

    public static Collection<SpriteIdentifier> getRegistered() {
        return Collections.unmodifiableCollection(ids);
    }
}
