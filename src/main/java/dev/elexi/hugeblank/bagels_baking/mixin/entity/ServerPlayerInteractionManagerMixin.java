package dev.elexi.hugeblank.bagels_baking.mixin.entity;

import dev.elexi.hugeblank.bagels_baking.block.FermenterBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/network/ServerPlayerEntity;shouldCancelInteraction()Z", ordinal = 0), method = "interactBlock(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;")
    private boolean unCancelInteraction(boolean bl2, ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult) {
        if (world.getBlockState(hitResult.getBlockPos()).getBlock() instanceof FermenterBlock) {
            return false;
        }
        return bl2;
    }
}