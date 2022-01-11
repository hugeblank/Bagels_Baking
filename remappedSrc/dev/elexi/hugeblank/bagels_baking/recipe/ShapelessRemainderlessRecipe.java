package dev.elexi.hugeblank.bagels_baking.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class ShapelessRemainderlessRecipe extends ShapelessRecipe {
    public static final dev.elexi.hugeblank.bagels_baking.recipe.ShapelessRemainderlessRecipe.Serializer SERIALIZER = new dev.elexi.hugeblank.bagels_baking.recipe.ShapelessRemainderlessRecipe.Serializer();
    public static final Identifier ID = new Identifier(Baking.ID, "crafting_shapeless_remainderless");
    public static final RecipeType<ShapelessRemainderlessRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ID, new RecipeType<ShapelessRemainderlessRecipe>() {
        public String toString() {
            return ID.toString();
        }
    });

    final String group;
    final ItemStack output;
    final DefaultedList<Ingredient> input;

    public ShapelessRemainderlessRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input) {
        super(id, group, output, input);
        this.group = group;
        this.output = output;
        this.input = input;
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(CraftingInventory inventory) {
        // Cancel recipe remainders for special circumstances (crafting bottled item -> bottled item)
        return DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
    }

    public static class Serializer implements RecipeSerializer<ShapelessRemainderlessRecipe> {
        // Clone of the ShapelessRecipe Serializer class
        public Serializer() {
        }

        public ShapelessRemainderlessRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            DefaultedList<Ingredient> defaultedList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
            if (defaultedList.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (defaultedList.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            } else {
                ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
                return new ShapelessRemainderlessRecipe(identifier, string, itemStack, defaultedList);
            }
        }

        private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.of();

            for(int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i));
                if (!ingredient.isEmpty()) {
                    defaultedList.add(ingredient);
                }
            }

            return defaultedList;
        }

        public ShapelessRemainderlessRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);

            for(int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.fromPacket(packetByteBuf));
            }

            ItemStack itemStack = packetByteBuf.readItemStack();
            return new ShapelessRemainderlessRecipe(identifier, string, itemStack, defaultedList);
        }

        public void write(PacketByteBuf packetByteBuf, ShapelessRemainderlessRecipe recipe) {
            packetByteBuf.writeString(recipe.group);
            packetByteBuf.writeVarInt(recipe.input.size());

            for (Ingredient ingredient : recipe.input) {
                ingredient.write(packetByteBuf);
            }

            packetByteBuf.writeItemStack(recipe.output);
        }
    }
}

