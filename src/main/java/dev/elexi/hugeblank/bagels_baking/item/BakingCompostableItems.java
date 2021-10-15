package dev.elexi.hugeblank.bagels_baking.item;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemConvertible;

public class BakingCompostableItems {

    public static void registerCompostableItem(float levelIncreaseChance, ItemConvertible item) {
        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(item.asItem(), levelIncreaseChance);
    }

    private static final float f = 0.3F;
    private static final float g = 0.5F;
    private static final float h = 0.65F;
    private static final float i = 0.85F;
    private static final float j = 1.0F;

    public static void init() {

        // Sandwiches

        registerCompostableItem(h, Baking.SWEET_BERRY_JAM_SANDWICH);
        registerCompostableItem(h, Baking.APPLE_JAM_SANDWICH);
        registerCompostableItem(h, Baking.GLOW_BERRY_JAM_SANDWICH);
        registerCompostableItem(h, Baking.GRAPE_JAM_SANDWICH);

        // Pies

        registerCompostableItem(i, Baking.SWEET_BERRY_PIE);
        registerCompostableItem(i, Baking.APPLE_PIE);
        registerCompostableItem(i, Baking.CHERRY_MERINGUE_PIE);
        registerCompostableItem(i, Baking.LEMON_MERINGUE_PIE);

        // Cakes - handled through `Baking.registerCakeType`

        // Ingredients

        registerCompostableItem(g, Baking.FLOUR);
        registerCompostableItem(g, Baking.CORN_MEAL);
        registerCompostableItem(g, Baking.DOUGH);
        registerCompostableItem(g, Baking.PASTA_DOUGH);
        registerCompostableItem(g, Baking.LINGUINE);
        registerCompostableItem(g, Baking.MACARONI);
        registerCompostableItem(g, Baking.MASHED_POTATOES);
        registerCompostableItem(g, Baking.TOMATO_SAUCE);
        registerCompostableItem(g, Baking.CINNAMON_BARK);
        registerCompostableItem(g, Baking.CINNAMON_POWDER);
        registerCompostableItem(g, Baking.GROUND_COFFEE);
        registerCompostableItem(g, Baking.GROUND_TEA);
        registerCompostableItem(g, Baking.COCOA_POWDER);

        // Raw/Cooked Goods

        registerCompostableItem(f, Baking.FRENCH_FRIES);
        registerCompostableItem(i, Baking.COOKED_FRENCH_FRIES);

        // Crops

        registerCompostableItem(f, Baking.TOMATO);
        registerCompostableItem(f, Baking.CORN_SEEDS);
        registerCompostableItem(g, Baking.CORN);
        registerCompostableItem(f, Baking.WILD_RICE);
        registerCompostableItem(f, Baking.RICE);
        registerCompostableItem(f, Baking.WILD_RICE_BALL);
        registerCompostableItem(f, Baking.RICE_BALL);
        registerCompostableItem(f, Baking.COOKED_WILD_RICE_BALL);
        registerCompostableItem(f, Baking.COOKED_WILD_RICE_BALL);
        registerCompostableItem(f, Baking.WHEAT_CEREAL);
        registerCompostableItem(f, Baking.CORN_CEREAL);
        registerCompostableItem(f, Baking.RICE_CEREAL);
        registerCompostableItem(f, Baking.COFFEE_BEANS);
        registerCompostableItem(f, Baking.TEA_LEAVES);
        registerCompostableItem(f, Baking.TEA_SEEDS);
        registerCompostableItem(f, Baking.DRIED_TEA_LEAVES);

        // Trees - Leaves & Fruit handled through `WoodType`

        registerCompostableItem(f, Baking.CHERRY_SAPLING);
        registerCompostableItem(f, Baking.CINNAMON_SAPLING);
        registerCompostableItem(f, Baking.JUNIPER_SAPLING);
        registerCompostableItem(f, Baking.LEMON_SAPLING);
        registerCompostableItem(f, Baking.GRAPES);
        registerCompostableItem(g, Baking.GRAPE_VINE);
        registerCompostableItem(g, Baking.GRAPE_STEM);

        // Misc

        registerCompostableItem(g, Baking.BAGEL);
        registerCompostableItem(g, Baking.DONUT);
        registerCompostableItem(g, Baking.BROWNIE);
        registerCompostableItem(h, Baking.CARAMEL_APPLE);
        registerCompostableItem(i, Baking.SNICKERDOODLE_COOKIE);
        registerCompostableItem(i, Baking.SUGAR_COOKIE);
    }
}