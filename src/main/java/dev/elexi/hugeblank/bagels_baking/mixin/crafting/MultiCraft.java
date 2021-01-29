package dev.elexi.hugeblank.bagels_baking.mixin.crafting;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.item.BasicDrink;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(CraftingResultSlot.class)
public class MultiCraft {

    @Final
    @Shadow
    private PlayerEntity player;

    @Final
    @Shadow
    private CraftingInventory input;

    @Shadow
    private int amount;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onCraft(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;I)V"), method = "onCrafted(Lnet/minecraft/item/ItemStack;)V")
    private void doubleResult(ItemStack stack, CallbackInfo ci) {
        Item item = stack.getItem();
        for (int i = 0; i < input.size(); i++) { // Drop bucket outputs
            Item inputItem = input.getStack(i).getItem();
            if (inputItem instanceof BasicDrink && !((BasicDrink) inputItem).isBucket()) {
                this.giveItem(Baking.CUP);
            }
        }

        if (item.equals(Baking.EGG_YOLK)) { // If we're crafting egg yolks
            player.increaseStat(Stats.CRAFTED.getOrCreateStat(Baking.EGG_WHITES), amount); // Keep track of stats
            this.giveItem(Baking.EGG_WHITES); // Give egg whites too

        }
    }

    private void giveItem(Item item) {
        ItemStack stack = new ItemStack(item, amount);
        if (!player.inventory.insertStack(stack)) {
            player.dropItem(stack, false);
        }
    }
}
