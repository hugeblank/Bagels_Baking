package dev.elexi.hugeblank.bagels_baking.block.cauldron;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.world.event.GameEvent;

import java.util.HashMap;
import java.util.Map;

public interface BakingCauldronBehavior extends CauldronBehavior {
    Map<Item, CauldronBehavior> BATTER_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> LIQUID_CHEESE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> SOLID_CHEESE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, Item> BATTER_ITEMS = new HashMap<>();
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

    static void registerBehaviors() {
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

        EMPTY_CAULDRON_BEHAVIOR.put(Baking.CHEESE, (state, world, pos, player, hand, stack) ->
                CauldronBehavior.fillCauldron(world, pos, player, hand, stack, Baking.LIQUID_CHEESE_CAULDRON.getDefaultState(), SoundEvents.ITEM_BUCKET_EMPTY_LAVA)
        );

        LIQUID_CHEESE_CAULDRON_BEHAVIOR.put(Items.BUCKET, (state, world, pos, player, hand, stack) ->
                CauldronBehavior.emptyCauldron(state, world, pos, player, hand, stack, new ItemStack(Baking.CHEESE), (statex) -> true, SoundEvents.ITEM_BUCKET_FILL_LAVA)
        );

        SOLID_CHEESE_CAULDRON_BEHAVIOR.put(Items.AIR, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                Item item = stack.getItem();
                giveItem(Baking.CHEESE_BLOCK.asItem(), player);
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }

            return ActionResult.success(world.isClient);
        });

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

        registerBatterItem(Items.CHICKEN, Baking.BATTERED_CHICKEN);
        registerBatterItem(Baking.CALAMARI, Baking.BATTERED_CALAMARI);
        registerBatterItem(Baking.CHICKEN_NUGGETS, Baking.BATTERED_CHICKEN_NUGGETS);
    }

    static void registerBatterItem(Item input, Item output) {
        BATTER_CAULDRON_BEHAVIOR.put(input, APPLY_BATTER_TO_ITEM);
        BATTER_ITEMS.put(input, output);
    }

    private static void giveItem(Item item, PlayerEntity player) {
        ItemStack stack = new ItemStack(item, 1);
        if (!player.getInventory().insertStack(stack)) {
            player.dropItem(stack, false);
        }
    }
}
