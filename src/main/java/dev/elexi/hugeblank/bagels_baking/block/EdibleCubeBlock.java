package dev.elexi.hugeblank.bagels_baking.block;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class EdibleCubeBlock extends Block {

    protected static final VoxelShape[] SHAPES_FOR_STATE;
    public static final IntProperty BITES;

    public EdibleCubeBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(BITES, 4));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        player.getHungerManager().add(5, 0.4f);
        int bites = state.get(BITES)-1;
        if (bites == 0) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        } else {
            world.setBlockState(pos, state.with(BITES, bites));
        }
        world.emitGameEvent(player, GameEvent.EAT, pos);
        return ActionResult.SUCCESS;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES_FOR_STATE[state.get(BITES)-1];
    }


    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    static {
        BITES = IntProperty.of("bites", 1, 4);

        SHAPES_FOR_STATE = new VoxelShape[]{
                Block.createCuboidShape(8.0D, 0.0D, 4.0D, 12.0D, 8.0D, 8.0D),
                Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 8.0D),
                VoxelShapes.combineAndSimplify(
                        Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 8.0D),
                        Block.createCuboidShape(4.0D, 0.0D, 8.0D, 8.0D, 8.0D, 12.0D),
                        BooleanBiFunction.OR),
                Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D)
        };
    }
}
