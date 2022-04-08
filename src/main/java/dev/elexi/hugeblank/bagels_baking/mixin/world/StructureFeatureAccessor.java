package dev.elexi.hugeblank.bagels_baking.mixin.world;

import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(StructureFeature.class)
public interface StructureFeatureAccessor {
    @Accessor
    static Map<StructureFeature<?>, GenerationStep.Feature> getSTRUCTURE_TO_GENERATION_STEP() {
        throw new UnsupportedOperationException();
    }
}
