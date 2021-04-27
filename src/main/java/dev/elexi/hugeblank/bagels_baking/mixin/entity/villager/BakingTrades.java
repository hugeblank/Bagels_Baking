package dev.elexi.hugeblank.bagels_baking.mixin.entity.villager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;

@Mixin(TradeOffers.class)
public class BakingTrades {

    private static TradeOffers.Factory[] mergeTrades(TradeOffers.Factory[] oldTrades, ImmutableSet<TradeOffers.Factory> newTrades) {
        newTrades.addAll(Arrays.asList(oldTrades));
        return newTrades.toArray(new TradeOffers.Factory[0]);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;", ordinal = 0), method = "method_16929(Ljava/util/HashMap;)V")
    private static ImmutableMap<Integer, TradeOffers.Factory[]> farmerTrades(Object int1, Object l1Trades, Object int2, Object l2Trades, Object int3, Object l3Trades, Object int4, Object l4Trades, Object int5, Object l5Trades) {
        return ImmutableMap.of(
                1, mergeTrades((TradeOffers.Factory[]) l1Trades, ImmutableSet.copyOf(new TradeOffers.Factory[]{
                        new TradeOffers.BuyForOneEmeraldFactory(Baking.CORN, 26, 16, 2),
                        new TradeOffers.BuyForOneEmeraldFactory(Baking.TOMATO, 22, 16, 2),
                        new TradeOffers.BuyForOneEmeraldFactory(Baking.WILD_RICE, 20, 16, 2)
                })),
                2, mergeTrades((TradeOffers.Factory[]) l2Trades, ImmutableSet.copyOf(new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(Baking.APPLE_PIE, 1, 4, 5),
                        new TradeOffers.SellItemFactory(Baking.BERRY_PIE, 1, 4, 5),
                        new TradeOffers.SellItemFactory(Baking.APPLE_JAM, 1, 1, 5),
                        new TradeOffers.SellItemFactory(Baking.BERRY_JAM, 1, 1, 5)
                })),
                3, (TradeOffers.Factory[]) l3Trades,
                4, (TradeOffers.Factory[]) l4Trades,
                5, (TradeOffers.Factory[]) l5Trades
                );
    }
}
