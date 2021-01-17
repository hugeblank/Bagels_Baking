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

    private static float f = 0.3F;
    //private static float g = 0.5F;
    private static float h = 0.65F;
    private static float i = 0.85F;
    private static float j = 1.0F;

    @Inject(at = @At(value="TAIL"), method= "registerDefaultCompostableItems()V")
    private static void registerCompostableItems(CallbackInfo ci) {

        // Sandwiches

        registerCompostableItem(h, Baking.BERRY_JAM_SANDWICH);
        registerCompostableItem(h, Baking.APPLE_JAM_SANDWICH);

        // Pies

        registerCompostableItem(i, Baking.BERRY_PIE);
        registerCompostableItem(i, Baking.APPLE_PIE);

        // Cakes

        registerCompostableItem(j, Baking.CARROT_CAKE_ITEM);
        registerCompostableItem(j, Baking.CHOCOLATE_CAKE_ITEM);
        registerCompostableItem(j, Baking.RED_VELVET_CAKE_ITEM);

        // Raw/Cooked Goods

        registerCompostableItem(f, Baking.RAW_FRENCH_FRIES);
        registerCompostableItem(f, Baking.COOKED_FRENCH_FRIES);

        // Misc

        registerCompostableItem(f, Baking.BAGEL);
        registerCompostableItem(f, Baking.DONUT);
    }

}
