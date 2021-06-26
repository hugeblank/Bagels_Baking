package dev.elexi.hugeblank.bagels_baking.mixin.block;

import dev.elexi.hugeblank.bagels_baking.block.BasicCandleCakeBlock;
import net.minecraft.block.CandleCakeBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(CandleCakeBlock.class)
public class CandleCakeBlockMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), method = "<init>")
    private <K, V> V avoidMapInsertion(Map<K, V> map, K key, V value) {
        return value instanceof BasicCandleCakeBlock ? null : map.put(key, value);
    }
}
