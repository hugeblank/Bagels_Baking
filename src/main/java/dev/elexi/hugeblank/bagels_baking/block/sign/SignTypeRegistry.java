package dev.elexi.hugeblank.bagels_baking.block.sign;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.mixin.block.SignTypeAccessor;
import dev.elexi.hugeblank.bagels_baking.sprite.SpriteRegistry;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;

import java.util.HashMap;
import java.util.Map;

public class SignTypeRegistry extends SignType {
    private static final Map<String, SpriteIdentifier> textures = new HashMap<>();

    protected SignTypeRegistry(String name) {
        super(name);
    }

    public static SignType register(String name) {
        SignType type = new SignTypeRegistry(name);
        SignTypeAccessor.getVALUES().add(type);

        // Friendly reminder - If we ever want to change the structure of how entity textures are stored, change the identifier here :)
        SpriteIdentifier sprite = new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, new Identifier(Baking.ID, "entity/" + name + "_sign"));
        textures.put(name, sprite);
        SpriteRegistry.register(sprite);

        return type;
    }

    public static SpriteIdentifier getTexture(String name) {
        return textures.get(name);
    }
}
