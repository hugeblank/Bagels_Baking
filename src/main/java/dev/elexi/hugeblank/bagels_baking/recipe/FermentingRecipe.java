package dev.elexi.hugeblank.bagels_baking.recipe;

import com.google.gson.JsonObject;
import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.block.entity.FermenterBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Random;

public class FermentingRecipe implements Recipe<FermenterBlockEntity> {
    public static final RecipeSerializer<FermentingRecipe> SERIALIZER = new FermentingRecipe.Serializer<>(FermentingRecipe::new);
    public static final Identifier ID = new Identifier(Baking.ID, "fermenting");
    public static final RecipeType<FermentingRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ID, new RecipeType<FermentingRecipe>() {
        public String toString() {
            return ID.toString();
        }
    });

    protected final Ingredient input;
    protected final ItemStack collector;
    protected final ItemStack output;
    protected final Identifier id;
    protected final String group;
    protected final Random random = new Random();

    public FermentingRecipe(Identifier id, String group, Ingredient input, ItemStack collector, ItemStack output) {
        this.input = input;
        this.collector = collector;
        this.output = output;
        this.id = id;
        this.group = group;
    }

    public ItemStack createIcon() {
        return new ItemStack(Baking.FERMENTER);
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

    public ItemStack craft(FermenterBlockEntity entity) {
        return this.output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    public ItemStack getCollector() {
        return collector;
    }

    @Override
    public boolean matches(FermenterBlockEntity entity, World world) {
        return entity.getContent() == this.output.getItem();
    }

    protected record Serializer<T extends FermentingRecipe>(
            FermentingRecipe.Serializer.RecipeFactory<T> recipeFactory) implements RecipeSerializer<T> {

        public T read(Identifier identifier, JsonObject jsonObject) {
            String group = JsonHelper.getString(jsonObject, "group", "");
            Ingredient ingredient;
            if (JsonHelper.hasArray(jsonObject, "ingredient")) {
                ingredient = Ingredient.fromJson(JsonHelper.getArray(jsonObject, "ingredient"));
            } else {
                ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient"));
            }

            String result = JsonHelper.getString(jsonObject, "result");
            ItemStack resultItem = Registry.ITEM.get(new Identifier(result)).getDefaultStack();
            ItemStack collectorItem;
            if (resultItem.getItem().getRecipeRemainder() != null) {
                collectorItem = resultItem.getItem().getRecipeRemainder().getDefaultStack();
            } else {
                collectorItem = ItemStack.EMPTY;
            }
            return this.recipeFactory.create(identifier, group, ingredient, collectorItem, resultItem);
        }

        public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String group = packetByteBuf.readString();
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            ItemStack resultItem = packetByteBuf.readItemStack();
            ItemStack collectorItem;
            if (resultItem.getItem().getRecipeRemainder() != null) {
                collectorItem = resultItem.getItem().getRecipeRemainder().getDefaultStack();
            } else {
                collectorItem = ItemStack.EMPTY;
            }
            return this.recipeFactory.create(identifier, group, ingredient, collectorItem, resultItem);
        }

        public void write(PacketByteBuf packetByteBuf, T cuttingRecipe) {
            packetByteBuf.writeString(cuttingRecipe.group);
            cuttingRecipe.input.write(packetByteBuf);
            packetByteBuf.writeItemStack(cuttingRecipe.output);
        }

        public interface RecipeFactory<T extends FermentingRecipe> {
            T create(Identifier id, String group, Ingredient input, ItemStack collector, ItemStack output);
        }
    }
}
