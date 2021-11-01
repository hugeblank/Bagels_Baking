package dev.elexi.hugeblank.bagels_baking.mixin.client;

import net.minecraft.client.toast.Toast;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Toast.Visibility.class)
public interface VisibilityAccessor {
    @Invoker("<init>")
    static Toast.Visibility createVisibility(String name, int id, SoundEvent sound) {
        throw new UnsupportedOperationException();
    }
}
