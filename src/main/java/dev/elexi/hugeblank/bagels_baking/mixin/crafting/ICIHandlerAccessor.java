package dev.elexi.hugeblank.bagels_baking.mixin.crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CraftingInventory.class)
public interface ICIHandlerAccessor extends Inventory {

    @Accessor
    ScreenHandler getHandler();

}
