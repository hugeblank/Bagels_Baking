package dev.elexi.hugeblank.bagels_baking.block.cauldron;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.HashMap;
import java.util.Map;

public interface BakingCauldronBehavior extends CauldronBehavior {
    Map<Block, Item> CUP_FLUIDS = new HashMap<>();
    Map<Item, CauldronBehavior> COFFEE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> TEA_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> CREAMY_COFFEE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> CREAMY_TEA_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> LIQUID_CHEESE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> SOLID_CHEESE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> SEPARATOR_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();

    CauldronBehavior FILL_CAULDRON_CUP = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient()) {
            if (state.get(LeveledCauldronBlock.LEVEL) < 3) {
                swapItem(stack, player, Baking.CUP.getDefaultStack());
                player.incrementStat(Stats.FILL_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                world.setBlockState(pos, state.with(LeveledCauldronBlock.LEVEL, state.get(LeveledCauldronBlock.LEVEL) + 1));
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.5F);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                return ActionResult.success(world.isClient());
            }
        }
        return ActionResult.PASS;
    };

    CauldronBehavior DRAIN_CAULDRON_CUP = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            Block cauldron = state.getBlock();
            if (CUP_FLUIDS.containsKey(cauldron)) {
                swapItem(stack, player, CUP_FLUIDS.get(cauldron).getDefaultStack());
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.5F);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            } else {
                return ActionResult.PASS;
            }
        }
        return ActionResult.success(world.isClient);
    };

    static void registerBehaviors() {

        // Empty to liquid cheese
        EMPTY_CAULDRON_BEHAVIOR.put(Baking.CHEESE, (state, world, pos, player, hand, stack) ->
                CauldronBehavior.fillCauldron(world, pos, player, hand, stack, Baking.LIQUID_CHEESE_CAULDRON.getDefaultState(), SoundEvents.ITEM_BUCKET_EMPTY_LAVA)
        );

        // Liquid cheese to empty
        LIQUID_CHEESE_CAULDRON_BEHAVIOR.put(Items.BUCKET, (state, world, pos, player, hand, stack) ->
                CauldronBehavior.emptyCauldron(state, world, pos, player, hand, stack, new ItemStack(Baking.CHEESE), (statex) -> true, SoundEvents.ITEM_BUCKET_FILL_LAVA)
        );

        // Cheese block to empty
        SOLID_CHEESE_CAULDRON_BEHAVIOR.put(Items.AIR, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                swapItem(stack, player, Baking.CHEESE_BLOCK.asItem().getDefaultStack());
                player.incrementStat(Stats.USE_CAULDRON);
                world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }

            return ActionResult.success(world.isClient);
        });

        // Yolk/White level decrease
        SEPARATOR_CAULDRON_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient && state.getBlock() instanceof SeparatorCauldron) {
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                if (state.get(SeparatorCauldron.YOLKS) > 0) {
                    swapItem(stack, player, new ItemStack(Baking.EGG_YOLK));
                    SeparatorCauldron.decrementYolkLevel(state, world, pos);
                } else {
                    swapItem(stack, player, new ItemStack(Baking.EGG_WHITES));
                    LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                }
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ActionResult.success(world.isClient);
        });

        // Empty to Separator cauldron
        EMPTY_CAULDRON_BEHAVIOR.put(Items.EGG, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                stack.decrement(1);
                world.setBlockState(pos, Baking.SEPARATOR_CAULDRON.getDefaultState());
                world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 1.0F, 1.5F);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ActionResult.success(world.isClient);
        });

        // Increase yolk/white level
        SEPARATOR_CAULDRON_BEHAVIOR.put(Items.EGG, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient && state.get(SeparatorCauldron.LEVEL) < 3) {
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                stack.decrement(1);
                int yolk = state.get(SeparatorCauldron.YOLKS)+1;
                int white = state.get(LeveledCauldronBlock.LEVEL)+1;
                world.setBlockState(pos, state.with(SeparatorCauldron.YOLKS, yolk).with(LeveledCauldronBlock.LEVEL, white));
                world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 1.0F, 1.5F);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ActionResult.success(world.isClient);
        });

        // Coffee/Tea/Water level increase/decrease
        registerCauldronCupFluid(CauldronBehavior.WATER_CAULDRON_BEHAVIOR, Blocks.WATER_CAULDRON, Baking.WATER_CUP);
        registerCauldronCupFluid(COFFEE_CAULDRON_BEHAVIOR, Baking.COFFEE_CAULDRON, Baking.COFFEE_CUP);
        registerCauldronCupFluid(TEA_CAULDRON_BEHAVIOR, Baking.TEA_CAULDRON, Baking.TEA_CUP);
        registerCauldronCupFluid(CREAMY_COFFEE_CAULDRON_BEHAVIOR, Baking.CREAMY_COFFEE_CAULDRON, Baking.COFFEE_W_CREAMER);
        registerCauldronCupFluid(CREAMY_TEA_CAULDRON_BEHAVIOR, Baking.CREAMY_TEA_CAULDRON, Baking.TEA_W_CREAMER);

        // X to Creamy X conversion
        COFFEE_CAULDRON_BEHAVIOR.put(Baking.CREAMER_CUP, (state, world, pos, player, hand, stack) -> fillCauldronCup(world, pos, player, hand, stack, Baking.CREAMY_COFFEE_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, state.get(LeveledCauldronBlock.LEVEL)), SoundEvents.ITEM_BUCKET_EMPTY));
        TEA_CAULDRON_BEHAVIOR.put(Baking.CREAMER_CUP, (state, world, pos, player, hand, stack) -> fillCauldronCup(world, pos, player, hand, stack, Baking.CREAMY_TEA_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, state.get(LeveledCauldronBlock.LEVEL)), SoundEvents.ITEM_BUCKET_EMPTY));
    }

    static void registerCauldronCupFluid(Map<Item, CauldronBehavior> behavior, Block cauldron, Item output) {
        EMPTY_CAULDRON_BEHAVIOR.put(output, (state, world, pos, player, hand, stack) ->
                fillCauldronCup(world, pos, player, hand, stack, cauldron.getDefaultState(), SoundEvents.ITEM_BUCKET_EMPTY)
        );
        behavior.put(output, FILL_CAULDRON_CUP);
        CUP_FLUIDS.put(cauldron, output);
        behavior.put(Baking.CUP, DRAIN_CAULDRON_CUP);
    }

    static ActionResult fillCauldronCup(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, BlockState state, SoundEvent soundEvent) {
        if (!world.isClient) {
            Item item = stack.getItem();
            swapItem(stack, player, new ItemStack(Baking.CUP));
            player.incrementStat(Stats.FILL_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            world.setBlockState(pos, state);
            world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.5F);
            world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
        }

        return ActionResult.success(world.isClient);
    }

    static void swapItem(ItemStack inputStack, PlayerEntity player, ItemStack outputStack) {
        inputStack.decrement(1);
        if (!player.getInventory().insertStack(outputStack)) {
            player.dropItem(outputStack, false);
        }
    }
}
