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
import net.minecraft.item.ItemUsage;
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
    Map<Item, Item> BATTER_ITEMS = new HashMap<>();
    Map<Block, Item> CUP_FLUIDS = new HashMap<>();
    Map<Item, CauldronBehavior> BATTER_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> COFFEE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> TEA_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> CREAMY_COFFEE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> CREAMY_TEA_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> LIQUID_CHEESE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> SOLID_CHEESE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();

    CauldronBehavior APPLY_BATTER_TO_ITEM = (state, world, pos, player, hand, stack) -> {
        Item item = stack.getItem();
        if (!BATTER_ITEMS.containsKey(item)) {
            return ActionResult.PASS;
        } else {
            Item output = BATTER_ITEMS.get(item);
            giveItem(output, player);
            player.incrementStat(Stats.USE_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            if (world.getRandom().nextBoolean()) {
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
            }
            world.playSound(null, pos, SoundEvents.BLOCK_SHROOMLIGHT_STEP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return ActionResult.success(world.isClient);
        }
    };

    CauldronBehavior FILL_CAULDRON_CUP = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            Item item = stack.getItem();
            player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Baking.CUP)));
            player.incrementStat(Stats.FILL_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            if (state.get(LeveledCauldronBlock.LEVEL) < 3) {
                world.setBlockState(pos, state.with(LeveledCauldronBlock.LEVEL, state.get(LeveledCauldronBlock.LEVEL) + 1));
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.5F);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            } else {
                return ActionResult.PASS;
            }
        }
        return ActionResult.success(world.isClient());
    };

    CauldronBehavior DRAIN_CAULDRON_CUP = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            Block cauldron = state.getBlock();
            if (CUP_FLUIDS.containsKey(cauldron)) {
                giveItem(CUP_FLUIDS.get(cauldron), player);
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
        // Empty to 1 batter layer
        EMPTY_CAULDRON_BEHAVIOR.put(Baking.BATTER, (state, world, pos, player, hand, stack) -> {
            if (stack.getItem() != Baking.BATTER) {
                return ActionResult.PASS;
            } else {
                if (!world.isClient) {
                    giveItem(Items.BOWL, player);
                    player.incrementStat(Stats.USE_CAULDRON);
                    player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                    world.setBlockState(pos, Baking.BATTER_CAULDRON.getDefaultState());
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                }
                return ActionResult.success(world.isClient);
            }
        });

        // Batter level increase
        BATTER_CAULDRON_BEHAVIOR.put(Baking.BATTER, (state, world, pos, player, hand, stack) -> {
            if (state.get(LeveledCauldronBlock.LEVEL) != 3 && stack.getItem() == Baking.BATTER) {
                if (!world.isClient) {
                    giveItem(Items.BOWL, player);
                    player.incrementStat(Stats.USE_CAULDRON);
                    player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                    world.setBlockState(pos, state.cycle(LeveledCauldronBlock.LEVEL));
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                }
                return ActionResult.success(world.isClient);
            } else {
                return ActionResult.PASS;
            }
        });

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
                giveItem(Baking.CHEESE_BLOCK.asItem(), player);
                player.incrementStat(Stats.USE_CAULDRON);
                world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }

            return ActionResult.success(world.isClient);
        });

        // Coffee/Tea level increase/decrease
        registerCauldronCupFluid(COFFEE_CAULDRON_BEHAVIOR, Baking.COFFEE_CAULDRON, Baking.COFFEE_CUP);
        registerCauldronCupFluid(TEA_CAULDRON_BEHAVIOR, Baking.TEA_CAULDRON, Baking.TEA_CUP);
        registerCauldronCupFluid(CREAMY_COFFEE_CAULDRON_BEHAVIOR, Baking.CREAMY_COFFEE_CAULDRON, Baking.COFFEE_W_CREAMER);
        registerCauldronCupFluid(CREAMY_TEA_CAULDRON_BEHAVIOR, Baking.CREAMY_TEA_CAULDRON, Baking.TEA_W_CREAMER);

        // X to Creamy X conversion
        COFFEE_CAULDRON_BEHAVIOR.put(Baking.CREAMER_CUP, (state, world, pos, player, hand, stack) -> fillCauldronCup(world, pos, player, hand, stack, Baking.CREAMY_COFFEE_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, state.get(LeveledCauldronBlock.LEVEL)), SoundEvents.ITEM_BUCKET_EMPTY));
        TEA_CAULDRON_BEHAVIOR.put(Baking.CREAMER_CUP, (state, world, pos, player, hand, stack) -> fillCauldronCup(world, pos, player, hand, stack, Baking.CREAMY_TEA_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, state.get(LeveledCauldronBlock.LEVEL)), SoundEvents.ITEM_BUCKET_EMPTY));

        // Map batter cauldron recipes
        registerBatterItem(Items.CHICKEN, Baking.BATTERED_CHICKEN);
        registerBatterItem(Baking.CALAMARI, Baking.BATTERED_CALAMARI);
        registerBatterItem(Baking.CHICKEN_NUGGETS, Baking.BATTERED_CHICKEN_NUGGETS);
    }

    static void registerBatterItem(Item input, Item output) {
        BATTER_CAULDRON_BEHAVIOR.put(input, APPLY_BATTER_TO_ITEM);
        BATTER_ITEMS.put(input, output);
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
            player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Baking.CUP)));
            player.incrementStat(Stats.FILL_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            world.setBlockState(pos, state);
            world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.5F);
            world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
        }

        return ActionResult.success(world.isClient);
    }

    private static void giveItem(Item item, PlayerEntity player) {
        ItemStack stack = new ItemStack(item, 1);
        if (!player.getInventory().insertStack(stack)) {
            player.dropItem(stack, false);
        }
    }
}
