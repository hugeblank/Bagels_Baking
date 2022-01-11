package dev.elexi.hugeblank.bagels_baking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class BasicCandleCakeBlock extends CandleCakeBlock {
    private final BasicCakeBlock cake;
    private static final Map<BasicCakeBlock, Map<CandleBlock, BasicCandleCakeBlock>> COMBINER = new HashMap<>();

    public BasicCandleCakeBlock(Block candle, BasicCakeBlock cake, Settings settings) {
        super(candle, settings);
        this.cake = cake;
        if (COMBINER.containsKey(cake)) {
            COMBINER.get(cake).put((CandleBlock) candle, this);
        } else {
            Map<CandleBlock, BasicCandleCakeBlock> candleCakeBlockMap = new HashMap<>();
            candleCakeBlockMap.put((CandleBlock) candle, this);
            COMBINER.put(cake, candleCakeBlockMap);
        }

    }

    public static BlockState getCandleCakeFromCandle(BasicCakeBlock cake, CandleBlock candle) {
        return COMBINER.get(cake).get(candle).getDefaultState();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isOf(Items.FLINT_AND_STEEL) && !itemStack.isOf(Items.FIRE_CHARGE)) {
            if (isHittingCandle(hit) && player.getStackInHand(hand).isEmpty() && state.get(LIT)) {
                extinguish(player, state, world, pos);
                return ActionResult.success(world.isClient);
            } else {
                ActionResult actionResult = BasicCakeBlock.tryEat(world, pos, cake.getDefaultState(), player);
                if (actionResult.isAccepted()) {
                    dropStacks(state, world, pos);
                }

                return actionResult;
            }
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(cake);
    }

    protected static boolean isHittingCandle(BlockHitResult hitResult) {
        return hitResult.getPos().y - (double)hitResult.getBlockPos().getY() > 0.5D;
    }
}
