package dev.elexi.hugeblank.bagels_baking.mixin.furnace;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractFurnaceBlockEntity.class)
public class SmeltConversion {

    @Shadow
    protected DefaultedList<ItemStack> inventory;
    // 0 Input Slot
    // 1 Fuel Slot
    // 2 Output Slot

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"), method = "craftRecipe(Lnet/minecraft/recipe/Recipe;)V", cancellable = true)
    private void onCompletion(Recipe<?> recipe, CallbackInfo ci) {
        ItemStack itemStack = inventory.get(0);
        ItemStack itemStack2 = inventory.get(2);
        // Convert Glass bottle to Mayo & Egg Whites
        if (itemStack.getItem() == Items.EGG && !inventory.get(1).isEmpty() && inventory.get(1).getItem() == Items.GLASS_BOTTLE) {
            if (itemStack2.isEmpty()) {
                inventory.set(2, new ItemStack(Baking.EGG_WHITES));
            } else if (itemStack2.getItem() == Baking.EGG_WHITES) {
                itemStack2.increment(1);
            } else {
                ci.cancel();
            }
            inventory.set(1, new ItemStack(Baking.MAYONNAISE));
            itemStack.decrement(1);
            ci.cancel();
        } else if (itemStack.getItem() == Items.MILK_BUCKET) {
            inventory.set(0, new ItemStack(Items.BUCKET));
            if (itemStack2.isEmpty()) {
                inventory.set(2, new ItemStack(Baking.EGG_WHITES));
            } else if (itemStack2.getItem() == Baking.CHEESE) {
                itemStack2.increment(1);
            }
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "isValid(ILnet/minecraft/item/ItemStack;)Z", cancellable = true)
    public void allowBottle(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) { // Handle insertion via dropper
        if (slot == 1 && stack.getItem() == Items.GLASS_BOTTLE && this.inventory.get(1).getItem() != Items.GLASS_BOTTLE) {
            cir.setReturnValue(true);
        }
    }
}
