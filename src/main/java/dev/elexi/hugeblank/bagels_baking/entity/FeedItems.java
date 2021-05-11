package dev.elexi.hugeblank.bagels_baking.entity;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class FeedItems {
    public static Item[] COW = {Baking.CORN};
    public static Item[] PIG = {Baking.CORN, Baking.TOMATO};
    public static Item[] CHICKEN = {Baking.CORN_MEAL, Baking.CORN_SEEDS, Baking.WILD_RICE, Baking.RICE};

    public static ArrayList<ItemStack> getList(Object[] objs) {
        Stream<Object> items = Arrays.stream(objs);
        ArrayList<ItemStack> stacks = new ArrayList<>();
        items.forEach((Object item) -> {
            stacks.add(((Item)item).getDefaultStack());
        });
        return stacks;
    }

    public static Ingredient set(Item[] a, Object[] b) {
        ArrayList<ItemStack> out = getList(a);
        out.addAll(getList(b));

        return Ingredient.ofStacks(out.stream());
    }
}

