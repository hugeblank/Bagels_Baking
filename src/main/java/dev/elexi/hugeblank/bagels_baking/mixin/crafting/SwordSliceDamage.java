package dev.elexi.hugeblank.bagels_baking.mixin.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CraftingResultSlot.class)
public class SwordSliceDamage extends Slot {

    @Final
    @Shadow
    private CraftingInventory input;
    PlayerEntity player;

    public SwordSliceDamage(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);
        RecipeManager manager =  player.world.getRecipeManager();
        DefaultedList<ItemStack> remainingStackList = manager.getRemainingStacks(RecipeType.CRAFTING, this.input, player.world);
        ScreenHandler handler = ((ICIHandlerAccessor)input).getHandler();
        int hasSwordAt = -1;

        for(int i = 0; i < remainingStackList.size(); ++i) {
            ItemStack initialStack = this.input.getStack(i);
            ItemStack remainingStack = remainingStackList.get(i);
            if (!initialStack.isEmpty() && initialStack.getItem() instanceof SwordItem) {
                if (hasSwordAt >= 0) {
                    this.input.removeStack(hasSwordAt);
                    this.input.removeStack(i);
                    initialStack = this.input.getStack(i);
                } else {
                    hasSwordAt = i;
                    if (!player.world.isClient) {
                        initialStack.damage(1, player.world.random, (ServerPlayerEntity) player);
                    }
                    if (initialStack.getDamage() > initialStack.getMaxDamage()) {
                        initialStack.decrement(1);
                    }
                }
            } else if (!initialStack.isEmpty()) {
                this.input.removeStack(i, 1);
                initialStack = this.input.getStack(i);
            }
            // Refresh the slots
            handler.sendContentUpdates();

            if (!remainingStack.isEmpty()) {
                if (initialStack.isEmpty()) {
                    this.input.setStack(i, remainingStack);
                } else if (ItemStack.areItemsEqualIgnoreDamage(initialStack, remainingStack) && ItemStack.areTagsEqual(initialStack, remainingStack)) {
                    remainingStack.increment(initialStack.getCount());
                    this.input.setStack(i, remainingStack);
                } else if (!this.player.inventory.insertStack(remainingStack)) {
                    this.player.dropItem(remainingStack, false);
                }
            }
        }

        return stack;
    }
}
