package dev.elexi.hugeblank.bagels_baking.mixin.world;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.BlockState;
import net.minecraft.block.CocoaBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.treedecorator.CocoaBeansTreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

@Mixin(CocoaBeansTreeDecorator.class)
public abstract class CoffeeBeansTreeDecorator extends TreeDecorator {

    @Final
    @Shadow
    private float probability;

    @Inject(at = @At("HEAD"), method = "generate(Lnet/minecraft/world/TestableWorld;Ljava/util/function/BiConsumer;Ljava/util/Random;Ljava/util/List;Ljava/util/List;)V", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, List<BlockPos> logPositions, List<BlockPos> leavesPositions, CallbackInfo ci) {
        if (!(random.nextFloat() >= this.probability/2)) {
            int i = logPositions.get(0).getY();
            logPositions.stream().filter((pos) -> pos.getY() - i <= 2).forEach((pos) -> {

                for (Direction direction : Direction.Type.HORIZONTAL) {
                    if (random.nextFloat() <= 0.25F) {
                        Direction direction2 = direction.getOpposite();
                        BlockPos blockPos = pos.add(direction2.getOffsetX(), 0, direction2.getOffsetZ());
                        if (Feature.isAir(world, blockPos)) {
                            replacer.accept(blockPos, Baking.COFFEE.getDefaultState().with(CocoaBlock.AGE, random.nextInt(3)).with(CocoaBlock.FACING, direction));
                        }
                    }
                }

            });
            ci.cancel();
        }
    }
}
