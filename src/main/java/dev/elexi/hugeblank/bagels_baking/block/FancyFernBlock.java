package dev.elexi.hugeblank.bagels_baking.block;

import it.unimi.dsi.fastutil.Function;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class FancyFernBlock extends FernBlock {
    // God mojang should hire me isn't this so CLEAN?!?!
    private final Function<Random, PlantBlock> growsInto;

    public FancyFernBlock(Settings settings, Function<Random, PlantBlock> growsInto) {
        super(settings);
        this.growsInto = growsInto;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getLightLevel(pos.up()) >= 9 && random.nextInt(7) == 0) {
            this.grow(world, random, pos, state);
        }
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        PlantBlock plant = growsInto.get(random);
        if (plant instanceof TriplePlantBlock && plant.getDefaultState().canPlaceAt(world, pos) && world.isAir(pos.up()) && world.isAir(pos.up().up())) {
            TriplePlantBlock.placeAt(world, plant.getDefaultState(), pos, 2);
        } else if (plant instanceof TallPlantBlock && plant.getDefaultState().canPlaceAt(world, pos) && world.isAir(pos.up())) {
            TallPlantBlock.placeAt(world, plant.getDefaultState(), pos, 2);
        } else if (plant.getDefaultState().canPlaceAt(world, pos)) {
            world.setBlockState(pos, plant.getDefaultState(), 2);
        }
    }
}
