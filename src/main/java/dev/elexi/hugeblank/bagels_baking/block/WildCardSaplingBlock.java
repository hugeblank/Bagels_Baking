package dev.elexi.hugeblank.bagels_baking.block;

import it.unimi.dsi.fastutil.Function;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class WildCardSaplingBlock extends FernBlock {
    public static final IntProperty AGE = Properties.AGE_15;

    // God mojang should hire me isn't this so CLEAN?!?!
    private final Function<Random, PlantBlock> growsInto;

    public WildCardSaplingBlock(Settings settings, Function<Random, PlantBlock> growsInto) {
        super(settings);
        this.growsInto = growsInto;
        this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(AGE);
        if (world.getLightLevel(pos.up()) >= 9 && age == 15) {
            this.grow(world, random, pos, state);
        } else {
            world.setBlockState(pos, state.with(AGE, age+1));
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

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
