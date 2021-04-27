package dev.elexi.hugeblank.bagels_baking.mixin.entity.villager;

import com.google.common.collect.ImmutableMap;
import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Mixin(TradeOffers.class)
public class BakingTrades {

    private static TradeOffers.Factory[] mergeTrades(TradeOffers.Factory[] oldTrades, HashSet<TradeOffers.Factory> newTrades) {
        newTrades.addAll(Arrays.asList(oldTrades));
        return newTrades.toArray(new TradeOffers.Factory[0]);
    }

    private static HashSet<TradeOffers.Factory> addAll(TradeOffers.Factory[] trades) {
        HashSet<TradeOffers.Factory> set = new HashSet<>();
        Collections.addAll(set, trades);
        return set;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;", ordinal = 0), method = "method_16929(Ljava/util/HashMap;)V")
    private static ImmutableMap<Integer, TradeOffers.Factory[]> farmerTrades(Object int1, Object l1Trades, Object int2, Object l2Trades, Object int3, Object l3Trades, Object int4, Object l4Trades, Object int5, Object l5Trades) {
        return ImmutableMap.of(
                1, mergeTrades((TradeOffers.Factory[]) l1Trades, addAll(new TradeOffers.Factory[]{
                        new TradeOffers.BuyForOneEmeraldFactory(Baking.CORN, 26, 16, 2),
                        new TradeOffers.BuyForOneEmeraldFactory(Baking.TOMATO, 22, 16, 2),
                        new TradeOffers.BuyForOneEmeraldFactory(Baking.WILD_RICE, 20, 16, 2),
                        new TradeOffers.SellItemFactory(Baking.BAGEL, 1, 4, 1),
                })),
                2, mergeTrades((TradeOffers.Factory[]) l2Trades, addAll(new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(Baking.APPLE_PIE, 1, 4, 5),
                        new TradeOffers.SellItemFactory(Baking.BERRY_PIE, 1, 4, 5),
                        new TradeOffers.SellItemFactory(Baking.APPLE_JAM, 1, 1, 5),
                })),
                3, mergeTrades((TradeOffers.Factory[]) l3Trades, addAll(new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(Baking.BROWNIE, 6, 2, 10)
                })),
                4, mergeTrades((TradeOffers.Factory[]) l4Trades, addAll(new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(Baking.CHOCOLATE_CAKE.asItem(), 1, 1, 15),
                        new TradeOffers.SellItemFactory(Baking.CARROT_CAKE.asItem(), 1, 1, 15),
                        new TradeOffers.SellItemFactory(Baking.RED_VELVET_CAKE.asItem(), 1, 1, 15),
                        new TradeOffers.SellItemFactory(Baking.FRUIT_SALAD, 1, 1, 15),
                        new TradeOffers.SellItemFactory(Baking.VEGGIE_MEDLEY, 1, 1, 15),
                })),
                5, (TradeOffers.Factory[]) l5Trades
                );
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;", ordinal = 1), method = "method_16929(Ljava/util/HashMap;)V")
    private static ImmutableMap<Integer, TradeOffers.Factory[]> fisherTrades(Object int1, Object l1Trades, Object int2, Object l2Trades, Object int3, Object l3Trades, Object int4, Object l4Trades, Object int5, Object l5Trades) {
        return ImmutableMap.of(
                1, (TradeOffers.Factory[]) l1Trades,
                2, (TradeOffers.Factory[]) l2Trades,
                3, mergeTrades((TradeOffers.Factory[]) l3Trades, addAll(new TradeOffers.Factory[]{
                        new TradeOffers.ProcessItemFactory(Baking.CUT_SALMON, 3, Baking.SMOKED_SALMON, 3, 16, 5)
                })),
                4, mergeTrades((TradeOffers.Factory[]) l4Trades, addAll(new TradeOffers.Factory[]{
                        new TradeOffers.BuyForOneEmeraldFactory(Baking.CALAMARI, 6, 12, 30),
                        new TradeOffers.ProcessItemFactory(Baking.CALAMARI, 6, Baking.COOKED_CALAMARI, 6, 16, 10)
                })),
                5, mergeTrades((TradeOffers.Factory[]) l5Trades, addAll(new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(Baking.FISH_STEW, 3, 1, 30),
                        new TradeOffers.SellItemFactory(Baking.SALMON_SUSHI, 2, 1, 30),
                }))
        );
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;", ordinal = 5), method = "method_16929(Ljava/util/HashMap;)V")
    private static ImmutableMap<Integer, TradeOffers.Factory[]> clericTrades(Object int1, Object l1Trades, Object int2, Object l2Trades, Object int3, Object l3Trades, Object int4, Object l4Trades, Object int5, Object l5Trades) {
        return ImmutableMap.of(
                1, (TradeOffers.Factory[]) l1Trades,
                2, (TradeOffers.Factory[]) l2Trades,
                3, mergeTrades((TradeOffers.Factory[]) l3Trades, addAll(new TradeOffers.Factory[]{
                        new TradeOffers.BuyForOneEmeraldFactory(Baking.COMPRESSED_TEA_BLOCK.asItem(), 10, 16, 20),
                })),
                4, (TradeOffers.Factory[]) l4Trades,
                5, (TradeOffers.Factory[]) l5Trades
        );
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;", ordinal = 9), method = "method_16929(Ljava/util/HashMap;)V")
    private static ImmutableMap<Integer, TradeOffers.Factory[]> butcherTrades(Object int1, Object l1Trades, Object int2, Object l2Trades, Object int3, Object l3Trades, Object int4, Object l4Trades, Object int5, Object l5Trades) {
        return ImmutableMap.of(
                1, mergeTrades((TradeOffers.Factory[]) l1Trades, addAll(new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(Baking.STEAK_STEW, 1, 1, 2),
                        new TradeOffers.SellItemFactory(Baking.CHICKEN_STEW, 1, 1, 2),
                })),
                2, (TradeOffers.Factory[]) l2Trades,
                3, (TradeOffers.Factory[]) l3Trades,
                4, mergeTrades((TradeOffers.Factory[]) l4Trades, addAll(new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(Baking.SMOKED_BACON, 3, 1, 30),
                        new TradeOffers.SellItemFactory(Baking.SMOKED_JERKY, 2, 1, 30),
                })),
                5, mergeTrades((TradeOffers.Factory[]) l5Trades, addAll(new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(Baking.BERRY_JAM, 1, 1, 30)
                }))
        );
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;", ordinal = 11), method = "method_16929(Ljava/util/HashMap;)V")
    private static ImmutableMap<Integer, TradeOffers.Factory[]> masonTrades(Object int1, Object l1Trades, Object int2, Object l2Trades, Object int3, Object l3Trades, Object int4, Object l4Trades, Object int5, Object l5Trades) {
        return ImmutableMap.of(
                1, (TradeOffers.Factory[]) l1Trades,
                2, (TradeOffers.Factory[]) l2Trades,
                3, mergeTrades((TradeOffers.Factory[]) l3Trades, addAll(new TradeOffers.Factory[]{
                        new TradeOffers.BuyForOneEmeraldFactory(Baking.HALITE.asItem(), 12, 16, 20),
                        new TradeOffers.SellItemFactory(Baking.POLISHED_HALITE.asItem(), 1, 4, 20),
                })),
                4, (TradeOffers.Factory[]) l4Trades,
                5, (TradeOffers.Factory[]) l5Trades
        );
    }
}
