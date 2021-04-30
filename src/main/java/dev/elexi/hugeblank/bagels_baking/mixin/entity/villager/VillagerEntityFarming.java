package dev.elexi.hugeblank.bagels_baking.mixin.entity.villager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityFarming extends MerchantEntity {

    public VillagerEntityFarming(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "hasSeedToPlant()Z", cancellable = true)
    private void hasSeedToPlant(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.getInventory().containsAny(ImmutableSet.of(Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS, Baking.TOMATO, Baking.CORN_SEEDS, Baking.WILD_RICE)));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;", ordinal = 0), method = "<clinit>")
    private static ImmutableMap<Item, Integer> addFoodValues(Object self0, Object self1, Object self2, Object self3, Object self4, Object self5, Object self6, Object self7) {
        // Can I just say how much I HATE that the ordinal default is not 0.
        return ImmutableMap.<Item, Integer>builder()
                .put(Items.BREAD, 4)
                .put(Items.POTATO, 1)
                .put(Items.CARROT, 1)
                .put(Items.BEETROOT, 1)
                .put(Baking.CORN, 2)
                .put(Baking.TOMATO, 1)
                .build();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableSet;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;", ordinal = 0), method = "<clinit>")
    private static ImmutableSet<Item> addGatherable(Object self0, Object self1, Object self2, Object self3, Object self4, Object self5, Object[] self6) {

        return ImmutableSet.<Item>builder()
                .add(Items.BREAD)
                .add(Items.POTATO)
                .add(Items.CARROT)
                .add(Items.WHEAT)
                .add(Items.WHEAT_SEEDS)
                .add(Items.BEETROOT)
                .add(Items.BEETROOT_SEEDS)
                .add(Baking.CORN_SEEDS)
                .add(Baking.CORN)
                .add(Baking.WILD_RICE)
                .add(Baking.TOMATO)
                .build();
    }
}
