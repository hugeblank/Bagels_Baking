package dev.elexi.hugeblank.bagels_baking.mixin.brewing;

import dev.elexi.hugeblank.bagels_baking.item.BottledItem;
import dev.elexi.hugeblank.bagels_baking.item.BrewableItem;
import dev.elexi.hugeblank.bagels_baking.item.CupItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.screen.BrewingStandScreenHandler$PotionSlot")
public class BrewingSlotsClient {
    @Inject(at = @At(value = "HEAD"), method = "matches(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
    private static void isValid(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Item item = stack.getItem();
        if (item instanceof BrewableItem) {
            cir.setReturnValue(((BrewableItem) item).isBrewable());
        }
    }

}
