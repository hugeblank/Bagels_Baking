package dev.elexi.hugeblank.bagels_baking.mixin.smelting;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public class SugarComponent {
    @Shadow
    protected DefaultedList<ItemStack> inventory;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;get(I)Ljava/lang/Object;"), method = "craftRecipe(Lnet/minecraft/recipe/Recipe;)V", cancellable = true)
    public void craftMeringue(Recipe<?> recipe, CallbackInfo ci) {
        ItemStack input = inventory.get(0);
        ItemStack fuel = inventory.get(1);
        ItemStack output = inventory.get(2);
        if (input.getItem() == Baking.RAW_EGG_WHITES && fuel.getItem() == Items.SUGAR) {
            if (output.isEmpty()) {
                inventory.set(2, new ItemStack(Baking.MERINGUE));
                fuel.decrement(1);
            } else if (output.getItem() == Baking.MERINGUE) {
                output.increment(1);
                fuel.decrement(1);
            } else if (output.getItem() == recipe.getOutput().getItem()) {
                output.increment(1);
            }
            input.decrement(1);
            ci.cancel();
        } else if (input.getItem() == Baking.RAW_EGG_WHITES && !output.isEmpty()) {
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Recipe;getOutput()Lnet/minecraft/item/ItemStack;"), method = "canAcceptRecipeOutput(Lnet/minecraft/recipe/Recipe;)Z", cancellable = true)
    public void canAcceptOutput(Recipe<?> recipe, CallbackInfoReturnable<Boolean> cir) {
        ItemStack input = inventory.get(0);
        ItemStack fuel = inventory.get(1);
        ItemStack output = inventory.get(2);
        // If the input is egg whites & the fuel slot has sugar & the output is either empty or has meringue
        if (!input.isEmpty() && input.getItem() == Baking.RAW_EGG_WHITES && !fuel.isEmpty() && fuel.getItem() == Items.SUGAR && output.isEmpty() || output.getItem() == Baking.MERINGUE) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "isValid(ILnet/minecraft/item/ItemStack;)Z", cancellable = true)
    public void sugarIsValid(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (slot == 1 && stack.getItem() == Items.SUGAR) {
            cir.setReturnValue(true);
        }
    }

}
