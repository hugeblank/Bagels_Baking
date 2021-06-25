package dev.elexi.hugeblank.bagels_baking.mixin.block;

import net.minecraft.util.SignType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(SignType.class)
public interface SignTypeAccessor {
    @Accessor
    static Set<SignType> getVALUES() {
        throw new UnsupportedOperationException();
    }
}
