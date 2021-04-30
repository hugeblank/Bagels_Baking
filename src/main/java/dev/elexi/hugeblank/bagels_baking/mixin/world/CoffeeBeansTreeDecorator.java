package dev.elexi.hugeblank.bagels_baking.mixin.world;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.BlockState;
import net.minecraft.block.CocoaBlock;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.tree.CocoaBeansTreeDecorator;
import net.minecraft.world.gen.tree.TreeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

@Mixin(CocoaBeansTreeDecorator.class)
public abstract class CoffeeBeansTreeDecorator extends TreeDecorator {

    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F"), method = "method_23466(Ljava/util/Random;Lnet/minecraft/world/StructureWorldAccess;Ljava/util/Set;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/BlockPos;)V", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void generate(Random random, StructureWorldAccess world, Set<BlockPos> placedStates, BlockBox box, BlockPos pos, CallbackInfo ci, Iterator var6, Direction direction) {
        if (random.nextFloat() <= 0.25F) {
            Direction direction2 = direction.getOpposite();
            BlockPos blockPos = pos.add(direction2.getOffsetX(), 0, direction2.getOffsetZ());
            if (Feature.isAir(world, blockPos)) {
                BlockState blockState = Baking.COFFEE.getDefaultState().with(CocoaBlock.AGE, random.nextInt(3)).with(CocoaBlock.FACING, direction);
                this.setBlockStateAndEncompassPosition(world, blockPos, blockState, placedStates, box);
            }
        }
    }
}
