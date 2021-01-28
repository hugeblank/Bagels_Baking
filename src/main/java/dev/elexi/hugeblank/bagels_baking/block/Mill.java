package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.screen.MillScreenHandler;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Mill extends StonecutterBlock {
    private static final Text TITLE = new TranslatableText("container.mill");
    private static final VoxelShape TOP_SHAPE;
    private static final VoxelShape MIDDLE_SHAPE;
    private static final VoxelShape OUTSIDE_SHAPE;
    private static final VoxelShape INSIDE_SHAPE;
    public static final VoxelShape NS_HOPPER_SHAPE;
    public static final VoxelShape EW_HOPPER_SHAPE;
    public static final VoxelShape NS_BOARD_SHAPE;
    public static final VoxelShape EW_BOARD_SHAPE;
    public static final VoxelShape NORTH_HANDLE_SHAPE;
    public static final VoxelShape SOUTH_HANDLE_SHAPE;
    public static final VoxelShape EAST_HANDLE_SHAPE;
    public static final VoxelShape WEST_HANDLE_SHAPE;
    public static final VoxelShape NORTH_SHAPE;
    public static final VoxelShape SOUTH_SHAPE;
    public static final VoxelShape EAST_SHAPE;
    public static final VoxelShape WEST_SHAPE;

    public Mill(Settings settings) {
        super(settings);

    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch(state.get(FACING)) {
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
            case EAST:
                return EAST_SHAPE;
            default:
                return NORTH_SHAPE;
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            // TheNoahA - Redeemed
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            player.incrementStat(Baking.INTERACT_WITH_MILL);
            return ActionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) ->
            new MillScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos)), TITLE);
    }

    static {
        TOP_SHAPE = Block.createCuboidShape(2.0D, 9.0D, 2.0D, 14.0D, 14.0D, 14.0D);
        MIDDLE_SHAPE = Block.createCuboidShape(5.0D, 5.0D, 5.0D, 11.0D, 9.0D, 11.0D);

        OUTSIDE_SHAPE = VoxelShapes.union(MIDDLE_SHAPE, TOP_SHAPE);
        INSIDE_SHAPE = Block.createCuboidShape(4.0D, 10.0D, 4.0D, 12.0D, 14.0D, 12.0D);
        NS_HOPPER_SHAPE = VoxelShapes.union(VoxelShapes.combineAndSimplify(OUTSIDE_SHAPE, INSIDE_SHAPE, BooleanBiFunction.ONLY_FIRST),
                Block.createCuboidShape(6.0D, 2.0D, 5.0D, 10.0D, 5.0D, 11.0D));
        EW_HOPPER_SHAPE = VoxelShapes.union(VoxelShapes.combineAndSimplify(OUTSIDE_SHAPE, INSIDE_SHAPE, BooleanBiFunction.ONLY_FIRST),
                Block.createCuboidShape(5.0D, 2.0D, 6.0D, 11.0D, 5.0D, 10.0D));

        NS_BOARD_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 2.0D, 16.0D, 2.0D, 14.0D);
        EW_BOARD_SHAPE = Block.createCuboidShape(2.0D, 0.0D, 0.0D, 14.0D, 2.0D, 16.0D);

        NORTH_HANDLE_SHAPE = VoxelShapes.union(
                Block.createCuboidShape(8.0D, 3.0D, 2.0D, 9.0D, 4.0D, 5.0D),
                Block.createCuboidShape(11.0D, 3.0D, 2.0D, 8.0D, 4.0D, 3.0D),
                Block.createCuboidShape(11.0D, 3.0D, 2.0D, 10.0D, 4.0D, 0.0D));
        SOUTH_HANDLE_SHAPE = VoxelShapes.union(
                Block.createCuboidShape(8.0D, 3.0D, 14.0D, 7.0D, 4.0D, 11.0D),
                Block.createCuboidShape(5.0D, 3.0D, 14.0D, 8.0D, 4.0D, 13.0D),
                Block.createCuboidShape(6.0D, 3.0D, 14.0D, 5.0D, 4.0D, 16.0D));
        WEST_HANDLE_SHAPE = VoxelShapes.union(
                Block.createCuboidShape(2.0D, 3.0D, 7.0D, 5.0D, 4.0D, 8.0D),
                Block.createCuboidShape(2.0D, 3.0D, 8.0D, 3.0D, 4.0D, 5.0D),
                Block.createCuboidShape(2.0D, 3.0D, 6.0D, 0.0D, 4.0D, 5.0D));
        EAST_HANDLE_SHAPE = VoxelShapes.union(
                Block.createCuboidShape(14.0D, 3.0D, 9.0D, 11.0D, 4.0D, 8.0D),
                Block.createCuboidShape(14.0D, 3.0D, 8.0D, 13.0D, 4.0D, 11.0D),
                Block.createCuboidShape(14.0D, 3.0D, 11.0D,  16.0D, 4.0D, 10.0D));

        // soapteasammi - Redeemed
        // soapteasammi - Redeemed
        // soapteasammi - Redeemed
        // soapteasammi - Redeemed
        // soapteasammi - Redeemed

        NORTH_SHAPE = VoxelShapes.union(NS_HOPPER_SHAPE, NS_BOARD_SHAPE, NORTH_HANDLE_SHAPE);
        SOUTH_SHAPE = VoxelShapes.union(NS_HOPPER_SHAPE, NS_BOARD_SHAPE, SOUTH_HANDLE_SHAPE);
        EAST_SHAPE = VoxelShapes.union(EW_HOPPER_SHAPE, EW_BOARD_SHAPE, EAST_HANDLE_SHAPE);
        WEST_SHAPE = VoxelShapes.union(EW_HOPPER_SHAPE, EW_BOARD_SHAPE, WEST_HANDLE_SHAPE);
    }
}