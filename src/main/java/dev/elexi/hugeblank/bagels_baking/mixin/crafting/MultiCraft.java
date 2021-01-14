package dev.elexi.hugeblank.bagels_baking.mixin.crafting;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class MayoCraft { // This is so damn janky lol

    @Shadow
    private Item item;

    @Inject(at = @At(value = "HEAD"), method = "onCraft(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;I)V")
    private void doubleResult(World world, PlayerEntity player, int amount, CallbackInfo ci) {
        if (item.equals(Baking.RAW_EGG_YOLK)) {
            player.increaseStat(Stats.CRAFTED.getOrCreateStat(Baking.RAW_EGG_WHITES), amount);
            player.inventory.insertStack(new ItemStack(Baking.RAW_EGG_WHITES, amount));
        }
    }
}
