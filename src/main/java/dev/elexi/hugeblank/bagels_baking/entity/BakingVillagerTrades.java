package dev.elexi.hugeblank.bagels_baking.entity;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import java.util.List;

public class BakingVillagerTrades {

    public BakingVillagerTrades() {}

    private static void buyItem(List<TradeOffers.Factory> factories, Item a, int count, int rewardedExp) {
        factories.add((entity, random) -> new TradeOffer(new ItemStack(a, count), new ItemStack(Items.EMERALD), 16, rewardedExp, 0.05f));
    }

    private static void sellItem(List<TradeOffers.Factory> factories, Item a, int price, int count, int rewardedExp) {
        factories.add((entity, random) -> new TradeOffer(new ItemStack(Items.EMERALD, price), new ItemStack(a, count), 12, rewardedExp, 0.05f));
    }

    private static void processItem(List<TradeOffers.Factory> factories, Item a, Item b, int count, int rewardedExp) {
        factories.add((entity, random) -> new TradeOffer(new ItemStack(a, count), new ItemStack(Items.EMERALD, 1), new ItemStack(b, count), 16, rewardedExp, 0.05f));
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
            sellItem(factories, Baking.SWEET_BERRY_PIE, 1, 4, 5);
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
            buyItem(factories, Baking.COMPRESSED_TEA_BLOCK.asItem(), 10, 10);
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
            sellItem(factories, Baking.SWEET_BERRY_JAM, 1, 1, 30);
        }));

        // Stone Mason Trades

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.MASON, 3, (factories -> {
            buyItem(factories, Baking.HALITE.asItem(), 12, 20);
            sellItem(factories, Baking.POLISHED_HALITE.asItem(), 1, 4, 20);
        }));

        // Vintner Trades

        TradeOfferHelper.registerVillagerOffers(BakingVillagerProfessions.VINTNER_PROFESSION, 1, (factories -> {
            buyItem(factories, Baking.GRAPES, 26, 2);
            sellItem(factories, Baking.GRAPE_JAM, 1, 1, 2);
            sellItem(factories, Baking.GRAPE_JAM_SANDWICH, 1, 2, 1);
        }));

        TradeOfferHelper.registerVillagerOffers(BakingVillagerProfessions.VINTNER_PROFESSION, 2, (factories -> {
            buyItem(factories, Items.GLASS_BOTTLE, 12, 5);
            sellItem(factories, Baking.CINNAMON_BARK, 4, 2, 5);
            sellItem(factories, Baking.GRAPE_STEM.asItem(), 10, 4, 5);
        }));

        TradeOfferHelper.registerVillagerOffers(BakingVillagerProfessions.VINTNER_PROFESSION, 3, (factories -> {
            buyItem(factories, Baking.MOLASSES, 4, 10);
            buyItem(factories, Items.HONEY_BOTTLE, 4, 10);
            buyItem(factories, Baking.MALT_VINEGAR, 4, 10);
            sellItem(factories, Baking.RED_WINE, 4, 1, 20);
            sellItem(factories, Baking.BEER, 2, 1, 20);
            sellItem(factories, Baking.WHISKEY, 3, 1, 20);
        }));

        TradeOfferHelper.registerVillagerOffers(BakingVillagerProfessions.VINTNER_PROFESSION, 4, (factories -> {
            sellItem(factories, Baking.OAK_TRELLIS.asItem(), 5, 2, 30);
            sellItem(factories, Baking.BIRCH_TRELLIS.asItem(), 5, 2, 30);
            sellItem(factories, Baking.SPRUCE_TRELLIS.asItem(), 5, 2, 30);
            sellItem(factories, Baking.JUNGLE_TRELLIS.asItem(), 5, 2, 30);
            sellItem(factories, Baking.ACACIA_TRELLIS.asItem(), 5, 2, 30);
            sellItem(factories, Baking.DARK_OAK_TRELLIS.asItem(), 5, 2, 30);
            sellItem(factories, Baking.CRIMSON_TRELLIS.asItem(), 5, 2, 30);
            sellItem(factories, Baking.WARPED_TRELLIS.asItem(), 5, 2, 30);
            sellItem(factories, Registry.ITEM.get(new Identifier(Baking.ID, "lemon_trellis")), 5, 2, 30);
            sellItem(factories, Registry.ITEM.get(new Identifier(Baking.ID, "cherry_trellis")), 5, 2, 30);
            sellItem(factories, Registry.ITEM.get(new Identifier(Baking.ID, "juniper_trellis")), 5, 2, 30);
        }));

        TradeOfferHelper.registerVillagerOffers(BakingVillagerProfessions.VINTNER_PROFESSION, 5, (factories -> {
            buyItem(factories, Baking.JUNIPER_BERRIES, 24, 30);
            sellItem(factories, Baking.GIN, 6, 1, 30);
            sellItem(factories, Baking.SPICED_RUM, 8, 1, 30);
        }));
    }
}
