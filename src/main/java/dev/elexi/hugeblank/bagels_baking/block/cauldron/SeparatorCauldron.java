package dev.elexi.hugeblank.bagels_baking.block.cauldron;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class SeparatorCauldron extends LeveledCauldronBlock {

    public static final IntProperty YOLKS = IntProperty.of("yolks", 0, 3);

    public SeparatorCauldron(Settings settings, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings, (precipitation) -> false, behaviorMap);
        this.setDefaultState(this.getStateManager().getDefaultState().with(YOLKS, 1));
    }

    public static void decrementYolkLevel(BlockState state, World world, BlockPos pos) {
        int i = state.get(YOLKS) - 1;
        world.setBlockState(pos, state.with(YOLKS, Math.max(i, 0)));
    }

    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(LEVEL) + state.get(YOLKS);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(YOLKS);
    }
}
