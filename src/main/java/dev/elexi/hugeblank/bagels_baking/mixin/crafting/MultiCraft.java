package dev.elexi.hugeblank.bagels_baking.mixin.crafting;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class MultiCraft {

    @Shadow
    private Item item;

    @Inject(at = @At(value = "HEAD"), method = "onCraft(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;I)V")
    private void doubleResult(World world, PlayerEntity player, int amount, CallbackInfo ci) {
        if (item.equals(Baking.EGG_YOLK)) { // If we're crafting egg yolks
            player.increaseStat(Stats.CRAFTED.getOrCreateStat(Baking.EGG_WHITES), amount); // Give egg whites too
            player.inventory.insertStack(new ItemStack(Baking.EGG_WHITES, amount)); // Keep track of stats
        }
    }
}
