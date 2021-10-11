package dev.elexi.hugeblank.bagels_baking.mixin.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(StructurePool.class)
public class StructurePoolMixin {

    @Final
    @Shadow
    private List<Pair<StructurePoolElement, Integer>> elementCounts;

    @Final
    @Shadow
    private List<StructurePoolElement> elements;

    private void addElement(String element, int weight, StructurePool.Projection projection) {
        StructurePoolElement e = StructurePoolElement.ofSingle(element).apply(projection);
        elementCounts.add(Pair.of(e, weight));
        for (int i = 0; i < weight; i++) {
            elements.add(e);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;", ordinal = 0), method = "<init>(Lnet/minecraft/util/Identifier;Lnet/minecraft/util/Identifier;Ljava/util/List;Lnet/minecraft/structure/pool/StructurePool$Projection;)V")
    private void addWineries(Identifier id, Identifier terminatorsId, List elementCounts, StructurePool.Projection projection, CallbackInfo ci) {
        if (Objects.equals(id.getPath(), "village/savanna/houses")) {
            addElement("village/savanna/houses/savanna_winery", 6, projection);
        } else if (Objects.equals(id.getPath(), "village/plains/houses")) {
            addElement("village/plains/houses/plains_winery", 3, projection);
        }
    }
}
