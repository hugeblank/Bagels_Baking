package dev.elexi.hugeblank.bagels_baking.mixin.world;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.placer.BlockPlacer;
import net.minecraft.world.gen.placer.BlockPlacerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockPlacerType.class)
public interface BlockPlacerTypeAccessor {
    @Invoker("<init>")
    static <P extends BlockPlacer> BlockPlacerType<P> newBlockPlacerType(Codec<P> codec) {
        throw new AssertionError();
    }
}
