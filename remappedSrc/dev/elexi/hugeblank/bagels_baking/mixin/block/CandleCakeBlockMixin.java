package dev.elexi.hugeblank.bagels_baking.mixin.block;

import dev.elexi.hugeblank.bagels_baking.block.BasicCandleCakeBlock;
import net.minecraft.block.*;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(CandleCakeBlock.class)
public abstract class CandleCakeBlockMixin extends AbstractCandleBlock {

    @Shadow @Final private static Map<Block, CandleCakeBlock> CANDLES_TO_CANDLE_CAKES;
    private CandleCakeBlock bagels_baking$cake_original;

    protected CandleCakeBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), method = "<init>")
    private void before(Block candle, AbstractBlock.Settings settings, CallbackInfo ci) {
        bagels_baking$cake_original = CANDLES_TO_CANDLE_CAKES.get(candle);
    }

    @Inject(at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), method = "<init>")
    private void after(Block candle, AbstractBlock.Settings settings, CallbackInfo ci) {
        if ((Object)this instanceof BasicCandleCakeBlock)
            CANDLES_TO_CANDLE_CAKES.put(candle, bagels_baking$cake_original);
    }

    public abstract Iterable<Vec3d> getParticleOffsets(BlockState state);
}