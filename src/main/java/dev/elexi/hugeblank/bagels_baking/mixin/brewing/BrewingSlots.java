package dev.elexi.hugeblank.bagels_baking.mixin.brewing;

import dev.elexi.hugeblank.bagels_baking.item.BrewableItem;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingSlots {


    @Inject(at = @At(value = "HEAD"), method = "isValid(ILnet/minecraft/item/ItemStack;)Z", cancellable = true)
    private void checkValid(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Item item = stack.getItem();
        if (item instanceof BrewableItem && slot != 3 && slot != 4) {
            cir.setReturnValue(((BrewableItem) item).isBrewable());
        }
    }

}
