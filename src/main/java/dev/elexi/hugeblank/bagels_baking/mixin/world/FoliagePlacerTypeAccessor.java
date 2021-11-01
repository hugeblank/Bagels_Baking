package dev.elexi.hugeblank.bagels_baking.mixin.world;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FoliagePlacerType.class)
public interface FoliagePlacerTypeAccessor {

    @Invoker("<init>")
    static <P extends FoliagePlacer> FoliagePlacerType<P> newFoliagePlacerType(Codec<P> codec) {
        throw new AssertionError();
    }
}