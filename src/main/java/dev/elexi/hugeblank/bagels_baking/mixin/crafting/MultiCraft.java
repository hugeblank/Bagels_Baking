package dev.elexi.hugeblank.bagels_baking.mixin.crafting;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
public class MultiCraft {

    @Final
    @Shadow
    private PlayerEntity player;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onCraft(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;I)V"), method = "onCrafted(Lnet/minecraft/item/ItemStack;)V")
    private void doubleResult(ItemStack output, CallbackInfo ci) {
        if (output.getItem().equals(Baking.EGG_YOLK)) { // If we're crafting egg yolks
            player.increaseStat(Stats.CRAFTED.getOrCreateStat(Baking.EGG_WHITES), 1); // Keep track of stats
            ItemStack stack = new ItemStack(Baking.EGG_WHITES, 1);
            if (!player.getInventory().insertStack(stack)) {
                player.dropItem(stack, false);
            }
        }
    }
}
