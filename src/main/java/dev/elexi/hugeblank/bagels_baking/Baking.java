package dev.elexi.hugeblank.bagels_baking;

import dev.elexi.hugeblank.bagels_baking.block.BasicCakeBlock;
import dev.elexi.hugeblank.bagels_baking.item.MayonnaiseItem;
import dev.elexi.hugeblank.bagels_baking.item.MidasSaladItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Baking implements ModInitializer {

	private static final String ID = "bagels_baking";

	// Sandwiches - Gives 2 items
	public static final Item STEAK_SANDWICH = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(9).saturationModifier(12.4f).build()));
	public static final Item CHICKEN_SANDWICH = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(8).saturationModifier(9.6f).build()));
	public static final Item PORK_SANDWICH = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(9).saturationModifier(12.4f).build()));
	public static final Item MUTTON_SANDWICH = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(8).saturationModifier(10.8f).build()));
	public static final Item FISH_SANDWICH = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(7).saturationModifier(9f).build()));
	public static final Item RABBIT_SANDWICH = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(7).saturationModifier(9f).build()));

	// Dinner - Gives 1 item
	public static final Item STEAK_DINNER = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(13).saturationModifier(18.8f).build()));
	public static final Item CHICKEN_DINNER = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(11).saturationModifier(13.2f).build()));
	public static final Item PORK_DINNER = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(13).saturationModifier(18.8f).build()));
	public static final Item MUTTON_DINNER = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(11).saturationModifier(15.6f).build()));
	public static final Item FISH_DINNER = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(10).saturationModifier(12f).build()));
	public static final Item RABBIT_DINNER = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(10).saturationModifier(12f).build()));

	// Pocket - Gives 2 items
	public static final Item STEAK_POCKET = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(11).saturationModifier(12.9f).build()));
	public static final Item CHICKEN_POCKET = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(10).saturationModifier(10.1f).build()));
	public static final Item PORK_POCKET = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(11).saturationModifier(12.9f).build()));
	public static final Item MUTTON_POCKET = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(10).saturationModifier(11.3f).build()));
	public static final Item FISH_POCKET = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(9).saturationModifier(9.5f).build()));
	public static final Item RABBIT_POCKET = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(9).saturationModifier(9.5f).build()));

	// The rest of the stews - Gives 1 item
	public static final MushroomStewItem STEAK_STEW = new MushroomStewItem( new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(14).saturationModifier(18.8f).build()));
	public static final MushroomStewItem CHICKEN_STEW = new MushroomStewItem( new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(11).saturationModifier(13.2f).build()));
	public static final MushroomStewItem PORK_STEW = new MushroomStewItem( new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(14).saturationModifier(18.8f).build()));
	public static final MushroomStewItem MUTTON_STEW = new MushroomStewItem( new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(11).saturationModifier(15.6f).build()));
	public static final MushroomStewItem FISH_STEW = new MushroomStewItem( new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(10).saturationModifier(12f).build()));
	// No need to add Rabbit Stew!

	// Tacos - Gives 2 Items
	public static final Item STEAK_TACO = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(7).saturationModifier(9.4f).build()));
	public static final Item CHICKEN_TACO = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(6).saturationModifier(6.6f).build()));
	public static final Item PORK_TACO = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(7).saturationModifier(9.4f).build()));
	public static final Item MUTTON_TACO = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(6).saturationModifier(7.8f).build()));
	public static final Item FISH_TACO = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(5).saturationModifier(6f).build()));
	public static final Item RABBIT_TACO = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(5).saturationModifier(6f).build()));

	// Cakes
	public static final Block CARROT_CAKE = new BasicCakeBlock();
	public static final BlockItem CARROT_CAKE_ITEM = new BlockItem(CARROT_CAKE, new Item.Settings().group(ItemGroup.FOOD).maxCount(1));
	public static final Block CHOCOLATE_CAKE = new BasicCakeBlock();
	public static final BlockItem CHOCOLATE_CAKE_ITEM = new BlockItem(CHOCOLATE_CAKE, new Item.Settings().group(ItemGroup.FOOD).maxCount(1));
	public static final Block RED_VELVET_CAKE = new BasicCakeBlock();
	public static final BlockItem RED_VELVET_CAKE_ITEM = new BlockItem(RED_VELVET_CAKE, new Item.Settings().group(ItemGroup.FOOD).maxCount(1));

	// Cheese Burgers - Gives 2 Items
	public static final Item STEAK_CHEESEBURGER = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(10).saturationModifier(14.9f).build()));
	public static final Item CHICKEN_CHEESEBURGER = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(9).saturationModifier(12.1f).build()));
	public static final Item PORK_CHEESEBURGER = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(10).saturationModifier(14.9f).build()));
	public static final Item MUTTON_CHEESEBURGER = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(9).saturationModifier(13.3f).build()));
	public static final Item FISH_CHEESEBURGER = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(8).saturationModifier(11.5f).build()));
	public static final Item RABBIT_CHEESEBURGER = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(8).saturationModifier(11.5f).build()));

	// Misc - Item amt listed individually
	public static final Item SURF_N_TURF = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(7).saturationModifier(14f).build())); // Give 2 items
	public static final MushroomStewItem VEGGIE_MEDLEY = new MushroomStewItem( new Item.Settings().group(ItemGroup.FOOD).maxCount(16).recipeRemainder(Items.BOWL).food(new FoodComponent.Builder().hunger(9).saturationModifier(7f).build())); // Give 1 item
	public static final MushroomStewItem FRUIT_SALAD = new MushroomStewItem( new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(8).saturationModifier(4f).build())); // Give 1 item
	public static final MidasSaladItem MIDAS_SALAD = new MidasSaladItem( new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(14).saturationModifier(35f).alwaysEdible() // Give 1 item
			.statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 20*120), 1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20*20, 1), 1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20*60), 1f).build()));
	public static final Item BAGEL = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(7).saturationModifier(6.5f).build())); // Give 2 items
	public static final Item DONUT = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(7).saturationModifier(7f).build())); // Give 2 items
	public static final Item SHEPHERDS_PIE = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(11).saturationModifier(13.6f).build())); // Give 1 item
	public static final Item COOKED_EGG = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(5f).build())); // Give 1 item
	public static final Item MAYONNAISE = new MayonnaiseItem( new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(1).saturationModifier(3f).build())); // Don't Craft
	public static final Item EGG_WHITES = new MayonnaiseItem( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).saturationModifier(2f)
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20*10), .1f).build()));
	public static final Item CHEESE = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(5f).build())); // Give 1 item
	public static final Item BERRY_PIE = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(7).saturationModifier(6.5f).build()));

	private static void register(String name, Item item) { // Make looking at all the item registration a bit more tolerable
		Registry.register(Registry.ITEM, new Identifier(ID, name), item);
	}

	@Override
	public void onInitialize() {

		// Sandwiches
		register("steak_sandwich", STEAK_SANDWICH);
		register("chicken_sandwich", CHICKEN_SANDWICH);
		register("pork_sandwich", PORK_SANDWICH);
		register("mutton_sandwich", MUTTON_SANDWICH);
		register("fish_sandwich", FISH_SANDWICH);
		register("rabbit_sandwich", RABBIT_SANDWICH);

		// Dinners
		register("steak_dinner", STEAK_DINNER);
		register("chicken_dinner", CHICKEN_DINNER);
		register("pork_dinner", PORK_DINNER);
		register("mutton_dinner", MUTTON_DINNER);
		register("fish_dinner", FISH_DINNER);
		register("rabbit_dinner", RABBIT_DINNER);

		// Pockets
		register("steak_pocket", STEAK_POCKET);
		register("chicken_pocket", CHICKEN_POCKET);
		register("pork_pocket", PORK_POCKET);
		register("mutton_pocket", MUTTON_POCKET);
		register("fish_pocket", FISH_POCKET);
		register("rabbit_pocket", RABBIT_POCKET);

		// Stews
		register("steak_stew", STEAK_STEW);
		register("chicken_stew", CHICKEN_STEW);
		register("pork_stew", PORK_STEW);
		register("mutton_stew", MUTTON_STEW);
		register("fish_stew", FISH_STEW);
		// No need to add rabbit stew!

		// Tacos
		register("steak_taco", STEAK_TACO);
		register("chicken_taco", CHICKEN_TACO);
		register("pork_taco", PORK_TACO);
		register("mutton_taco", MUTTON_TACO);
		register("fish_taco", FISH_TACO);
		register("rabbit_taco", RABBIT_TACO);

		// Cakes
		final Identifier carrot_cake =  new Identifier(ID, "carrot_cake");
		final Identifier chocolate_cake =  new Identifier(ID, "chocolate_cake");
		final Identifier red_velvet_cake = new Identifier(ID, "red_velvet_cake");
		Registry.register(Registry.BLOCK, carrot_cake, CARROT_CAKE);
		Registry.register(Registry.BLOCK, chocolate_cake, CHOCOLATE_CAKE);
		Registry.register(Registry.BLOCK, red_velvet_cake, RED_VELVET_CAKE);
		Registry.register(Registry.ITEM, carrot_cake, CARROT_CAKE_ITEM);
		Registry.register(Registry.ITEM, chocolate_cake, CHOCOLATE_CAKE_ITEM);
		Registry.register(Registry.ITEM, red_velvet_cake, RED_VELVET_CAKE_ITEM);

		// Cheese Burgers
		register("steak_cheeseburger", STEAK_CHEESEBURGER);
		register("chicken_cheeseburger", CHICKEN_CHEESEBURGER);
		register("pork_cheeseburger", PORK_CHEESEBURGER);
		register("mutton_cheeseburger", MUTTON_CHEESEBURGER);
		register("fish_cheeseburger", FISH_CHEESEBURGER);
		register("rabbit_cheeseburger", RABBIT_CHEESEBURGER);

		// Misc
		register("surf_n_turf", SURF_N_TURF);
		register("veggie_medley", VEGGIE_MEDLEY);
		register("fruit_salad", FRUIT_SALAD);
		register("midas_salad", MIDAS_SALAD);
		register("bagel", BAGEL);
		register("donut", DONUT);
		register("shepherds_pie", SHEPHERDS_PIE);
		register("cooked_egg", COOKED_EGG);
		register("mayonnaise", MAYONNAISE);
		register("egg_whites", EGG_WHITES);
		register("cheese", CHEESE);
		register("berry_pie", BERRY_PIE);

	}
}
