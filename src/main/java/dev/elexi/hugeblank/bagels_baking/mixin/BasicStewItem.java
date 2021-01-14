package dev.elexi.hugeblank.bagels_baking.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MushroomStewItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(MushroomStewItem.class)
public class BasicStewItem extends Item {

    public BasicStewItem(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "finishUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;", cancellable = true)
    public void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (user instanceof PlayerEntity && !((PlayerEntity) user).isCreative()) {
            ((PlayerEntity) user).giveItemStack(new ItemStack(Items.BOWL));
        }
        cir.setReturnValue(super.finishUsing(stack, world, user));
    }
}
