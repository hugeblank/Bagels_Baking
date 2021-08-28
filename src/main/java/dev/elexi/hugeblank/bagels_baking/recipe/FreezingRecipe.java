package dev.elexi.hugeblank.bagels_baking.recipe;

import com.google.gson.JsonObject;
import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

// There is no such thing as a coincidence. The stonecutter recipe methodology is clearly superior.
public class FreezingRecipe implements Recipe<Inventory> {
    public static final RecipeSerializer<FreezingRecipe> SERIALIZER = new FreezingRecipe.Serializer<>(FreezingRecipe::new);
    public static final Identifier ID = new Identifier(Baking.ID, "freezing");
    public static final RecipeType<FreezingRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ID, new RecipeType<FreezingRecipe>() {
        public String toString() {
            return ID.toString();
        }
    });

    protected final Ingredient input;
    protected final ItemStack output;
    protected final Identifier id;
    protected final String group;
    protected final Random random = new Random();

    public FreezingRecipe(Identifier id, String group, Ingredient input, ItemStack output) {
        this.input = input;
        this.output = output;
        this.id = id;
        this.group = group;
    }

    public ItemStack createIcon() {
        return new ItemStack(Baking.ICE_BOX);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }


    public ItemStack craft(Inventory inv) {
        ArrayList<Integer> slots = new ArrayList<>();
        for (int i = 0; i < inv.size(); i++) {
            if (input.test(inv.getStack(i))) {
                slots.add(i);
            }
        }

        Integer slot = slots.size() > 1 ? slots.get(random.nextInt(slots.size() - 1)) : slots.get(0);
        if (slot != null) {
            int count = inv.removeStack(slot).getCount();
            ItemStack out = new ItemStack(output.getItem(), count);
            inv.setStack(slot, out);
            return out;
        }
        return null;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.input);
        return list;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        ItemStack[] items = input.getMatchingStacks();
        HashSet<Item> set = new HashSet<>();
        for (ItemStack itemStack : items) {
            set.add(itemStack.getItem());
        }
        return inv.containsAny(set);
    }

    protected record Serializer<T extends FreezingRecipe>(FreezingRecipe.Serializer.RecipeFactory<T> recipeFactory) implements RecipeSerializer<T> {

        public T read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            Ingredient ingredient2;
            if (JsonHelper.hasArray(jsonObject, "ingredient")) {
                ingredient2 = Ingredient.fromJson(JsonHelper.getArray(jsonObject, "ingredient"));
            } else {
                ingredient2 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient"));
            }

            String string2 = JsonHelper.getString(jsonObject, "result");
            ItemStack itemStack = new ItemStack(Registry.ITEM.get(new Identifier(string2)));
            return this.recipeFactory.create(identifier, string, ingredient2, itemStack);
        }

        public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            ItemStack itemStack = packetByteBuf.readItemStack();
            return this.recipeFactory.create(identifier, string, ingredient, itemStack);
        }

        public void write(PacketByteBuf packetByteBuf, T cuttingRecipe) {
            packetByteBuf.writeString(cuttingRecipe.group);
            cuttingRecipe.input.write(packetByteBuf);
            packetByteBuf.writeItemStack(cuttingRecipe.output);
        }

        public interface RecipeFactory<T extends FreezingRecipe> {
            T create(Identifier id, String group, Ingredient input, ItemStack output);
        }
    }
}
