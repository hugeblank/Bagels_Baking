package dev.elexi.hugeblank.bagels_baking.mixin.composting;

import dev.elexi.hugeblank.bagels_baking.Baking;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemConvertible;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComposterBlock.class)
public abstract class Composting {

    @Final
    @Shadow public static Object2FloatMap<ItemConvertible> ITEM_TO_LEVEL_INCREASE_CHANCE;

    @Shadow
    private static void registerCompostableItem(float levelIncreaseChance, ItemConvertible item) {
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(item.asItem(), levelIncreaseChance);
    }

    private static final float f = 0.3F;
    private static final float g = 0.5F;
    private static final float h = 0.65F;
    private static final float i = 0.85F;
    private static final float j = 1.0F;

    @Inject(at = @At(value="TAIL"), method= "registerDefaultCompostableItems()V")
    private static void registerCompostableItems(CallbackInfo ci) {

        // Sandwiches

        registerCompostableItem(h, Baking.SWEET_BERRY_JAM_SANDWICH);
        registerCompostableItem(h, Baking.APPLE_JAM_SANDWICH);
        registerCompostableItem(h, Baking.GLOW_BERRY_JAM_SANDWICH);
        registerCompostableItem(h, Baking.GRAPE_JAM_SANDWICH);

        // Pies

        registerCompostableItem(i, Baking.SWEET_BERRY_PIE);
        registerCompostableItem(i, Baking.APPLE_PIE);

        // Cakes

        //registerCompostableItem(j, Baking.CARROT_CAKE.asItem());
        //registerCompostableItem(j, Baking.CHOCOLATE_CAKE.asItem());
        //registerCompostableItem(j, Baking.RED_VELVET_CAKE.asItem());
        //registerCompostableItem(j, Baking.LEMON_CAKE.asItem());

        // Ingredients

        registerCompostableItem(h, Baking.FLOUR);
        registerCompostableItem(h, Baking.DOUGH);
        registerCompostableItem(h, Baking.PASTA_DOUGH);
        registerCompostableItem(h, Baking.LINGUINE);
        registerCompostableItem(h, Baking.MACARONI);
        registerCompostableItem(h, Baking.MASHED_POTATOES);
        registerCompostableItem(h, Baking.TOMATO_SAUCE);

        // Raw/Cooked Goods

        registerCompostableItem(f, Baking.FRENCH_FRIES);
        registerCompostableItem(i, Baking.COOKED_FRENCH_FRIES);

        // Crops

        registerCompostableItem(f, Baking.TOMATO);
        registerCompostableItem(f, Baking.CORN_SEEDS);
        registerCompostableItem(g, Baking.CORN);
        registerCompostableItem(f, Baking.WILD_RICE);
        registerCompostableItem(f, Baking.COFFEE_BEANS);
        registerCompostableItem(f, Baking.TEA_LEAVES);
        registerCompostableItem(f, Baking.TEA_SEEDS);
        registerCompostableItem(f, Baking.GRAPES);
        registerCompostableItem(f, Baking.LEMON);
        registerCompostableItem(f, Baking.CHERRIES);

        // Misc

        registerCompostableItem(f, Baking.MASHED_POTATOES);
        registerCompostableItem(f, Baking.BAGEL);
        registerCompostableItem(f, Baking.DONUT);
    }

}
