package dev.elexi.hugeblank.bagels_baking.entity;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import java.util.List;

public class BakingVillagerTrades {

    public BakingVillagerTrades() {}

    private static void buyItem(List<TradeOffers.Factory> factories, Item a, int count, int rewardedExp) {
        factories.add((entity, random) -> new TradeOffer(new ItemStack(a, count), new ItemStack(Items.EMERALD), 16, rewardedExp, 0.5f));
    }

    private static void sellItem(List<TradeOffers.Factory> factories, Item a, int price, int count, int rewardedExp) {
        factories.add((entity, random) -> new TradeOffer(new ItemStack(Items.EMERALD, price), new ItemStack(a, count), 12, rewardedExp, 0.5f));
    }

    private static void processItem(List<TradeOffers.Factory> factories, Item a, Item b, int count, int rewardedExp) {
        factories.add((entity, random) -> new TradeOffer(new ItemStack(a, count), new ItemStack(Items.EMERALD, 1), new ItemStack(b, count), 16, rewardedExp, 0.5f));
    }

    public static void init() {
        // Farmer Trades

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 1, (factories -> {
            buyItem(factories, Baking.CORN, 26, 2);
            buyItem(factories, Baking.TOMATO, 22, 2);
            buyItem(factories, Baking.WILD_RICE, 20, 2);
            sellItem(factories, Baking.BAGEL, 1, 4, 1);
        }));

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 2, (factories -> {
            sellItem(factories, Baking.APPLE_PIE, 1, 4, 5);
            sellItem(factories, Baking.BERRY_PIE, 1, 4, 5);
            sellItem(factories, Baking.APPLE_JAM, 1, 1, 5);
        }));

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 3, (factories -> {
            sellItem(factories, Baking.BROWNIE, 6, 2, 10);
        }));

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 4, (factories -> {
            sellItem(factories, Baking.CHOCOLATE_CAKE.asItem(), 1, 1, 15);
            sellItem(factories, Baking.CARROT_CAKE.asItem(), 1, 1, 15);
            sellItem(factories, Baking.RED_VELVET_CAKE.asItem(), 1, 1, 15);
            sellItem(factories, Baking.FRUIT_SALAD, 1, 1, 15);
            sellItem(factories, Baking.VEGGIE_MEDLEY, 1, 1, 15);
        }));

        // Fisher Trades

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, 3, (factories -> {
            processItem(factories, Baking.CUT_SALMON, Baking.SMOKED_SALMON, 3, 5);
        }));

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, 4, (factories -> {
            sellItem(factories, Baking.CALAMARI, 1, 6, 30);
            processItem(factories, Baking.CALAMARI, Baking.COOKED_CALAMARI, 6, 10);
        }));

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, 5, (factories -> {
            sellItem(factories, Baking.FISH_STEW, 1, 1, 30);
            sellItem(factories, Baking.SALMON_SUSHI, 1, 1, 30);
        }));

        // Cleric Trades

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 3, (factories -> {
            buyItem(factories, Baking.COMPRESSED_TEA_BLOCK.asItem(), 10, 20);
        }));

        // Butcher Trades

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.BUTCHER, 1, (factories -> {
            sellItem(factories, Baking.STEAK_STEW, 3, 1, 2);
            sellItem(factories, Baking.CHICKEN_STEW, 2, 1, 2);
        }));

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.BUTCHER, 4, (factories -> {
            sellItem(factories, Baking.SMOKED_BACON, 3, 1, 30);
            sellItem(factories, Baking.SMOKED_JERKY, 2, 1, 30);
        }));

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.BUTCHER, 5, (factories -> {
            sellItem(factories, Baking.BERRY_JAM, 1, 1, 30);
        }));

        // Stone Mason Trades

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.MASON, 3, (factories -> {
            buyItem(factories, Baking.HALITE.asItem(), 12, 20);
            sellItem(factories, Baking.POLISHED_HALITE.asItem(), 1, 4, 20);
        }));
    }
}