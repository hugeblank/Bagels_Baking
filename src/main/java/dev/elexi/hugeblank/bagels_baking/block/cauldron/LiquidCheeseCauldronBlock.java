package dev.elexi.hugeblank.bagels_baking.block.cauldron;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.Random;

public class LiquidCheeseCauldronBlock extends BasicCauldronBlock{

    public LiquidCheeseCauldronBlock(Settings settings, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings.ticksRandomly(), behaviorMap);
    }



    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextFloat() < 0.3f) {
            world.setBlockState(pos, Baking.SOLID_CHEESE_CAULDRON.getDefaultState());
            world.playSound(null, pos, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }
}
