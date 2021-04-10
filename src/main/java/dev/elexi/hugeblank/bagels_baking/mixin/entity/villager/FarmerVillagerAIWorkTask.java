package dev.elexi.hugeblank.bagels_baking.mixin.entity.villager;

import com.google.common.collect.ImmutableList;
import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.entity.ai.brain.task.FarmerWorkTask;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FarmerWorkTask.class)
public class FarmerVillagerAIWorkTask {
    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;of(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;"), method = "<clinit>")
    private static ImmutableList<Item> CompostableItems(Object a, Object b) {
        return ImmutableList.of(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Baking.CORN_SEEDS);
    }
}
