package dev.elexi.hugeblank.bagels_baking.mixin.entity.passive;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.entity.FeedItems;
import dev.elexi.hugeblank.bagels_baking.item.CupItem;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CowEntity.class)
class CowEntityMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;ofItems([Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/recipe/Ingredient;", ordinal = 0), method = "initGoals()V")
    private Ingredient breedItems(ItemConvertible[] items) {
        return FeedItems.set(FeedItems.COW, items);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"), method = "interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", cancellable = true)
    private void milkWithCup(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Baking.CUP) && !((CowEntity)(Object)this).isBaby()) {
            CupItem item = (CupItem) itemStack.getItem();
            player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.5F);
            if (item.fill(itemStack, player, Baking.MILK_CUP.getDefaultStack()).isEmpty()) {
                PlayerInventory invo = player.getInventory();
                int slot = invo.getSlotWithStack(Baking.MILK_CUP.getDefaultStack());
                player.setStackInHand(hand, invo.removeStack(slot));
            }
            cir.setReturnValue(ActionResult.success(((CowEntity)(Object)this).world.isClient));
        }
    }
}
