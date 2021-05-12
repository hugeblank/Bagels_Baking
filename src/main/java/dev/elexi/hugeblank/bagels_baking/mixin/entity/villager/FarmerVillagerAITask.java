package dev.elexi.hugeblank.bagels_baking.mixin.entity.villager;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.task.FarmerVillagerTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FarmerVillagerTask.class)
public class FarmerVillagerAITask {

    @Shadow
    private BlockPos currentTarget;

    private boolean doTask = false;

    private void executeTask() {
        doTask = true;
    }

    @Inject(at = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V", locals = LocalCapture.CAPTURE_FAILHARD)
    private void farmTask(ServerWorld serverWorld, VillagerEntity villagerEntity, long l, CallbackInfo ci, BlockState blockState, Block block, Block block2, SimpleInventory simpleInventory, int i, ItemStack itemStack, boolean bl) {
        if (itemStack.getItem() == Baking.TOMATO) {
            serverWorld.setBlockState(currentTarget, Baking.TOMATO_PLANT.getDefaultState(), 3);
            executeTask();
        } else if (itemStack.getItem() == Baking.CORN_SEEDS) {
            serverWorld.setBlockState(currentTarget, Baking.CORN_STALK.getDefaultState(), 3);
            executeTask();
        } else if (itemStack.getItem() == Baking.WILD_RICE) {
            serverWorld.setBlockState(currentTarget, Baking.RICE_PLANT.getDefaultState(), 3);
            executeTask();
        }
    }

    @ModifyVariable(at = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V")
    private boolean overrideTask(boolean original) {
        if (doTask) {
            doTask = false;
            return true;
        }
        return original;
    }
}
