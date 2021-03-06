package dev.elexi.hugeblank.bagels_baking;

import dev.elexi.hugeblank.bagels_baking.block.BasicCakeBlock;
import dev.elexi.hugeblank.bagels_baking.block.Mill;
import dev.elexi.hugeblank.bagels_baking.block.StairBlock;
import dev.elexi.hugeblank.bagels_baking.item.*;
import dev.elexi.hugeblank.bagels_baking.recipe.MillingRecipe;
import dev.elexi.hugeblank.bagels_baking.screen.MillScreen;
import dev.elexi.hugeblank.bagels_baking.screen.MillScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.BlockView;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.function.Predicate;

public class Baking implements ModInitializer {

	private static final String ID = "bagels_baking";

	// Beautification Functions
	private static Item basicFood(int hunger, float saturation) {
		return basicFood(hunger, saturation, 64);
	}

	private static Item basicFood(int hunger, float saturation, int maxStack) {
		return new Item(new Item.Settings().maxCount(maxStack).group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation).build()));
	}

	private static Item basicIngredient() { return basicIngredient(64); }

	private static Item basicIngredient(int maxStack) {
		return new Item(new Item.Settings().group(ItemGroup.MATERIALS).maxCount(maxStack));
	}

	private static MushroomStewItem basicBowlFood(int hunger, float saturation) {
		return new MushroomStewItem(new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation).build()));
	}

	private static BottledItem basicJam(int hunger, float saturation, SoundEvent drinkSound) {
		return new BottledItem(new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation).build()), drinkSound);
	}


	private static void register(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(ID, name), item);
	}

	private static void registerBlock(String name, Block block, ItemGroup group) {
		Identifier id = new Identifier(ID, name);
		Registry.register(Registry.BLOCK, id, block);
		Item item = new BlockItem(block, new Item.Settings().group(group));
		Registry.register(Registry.ITEM, id, item);
	}

	private static boolean never(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	// Sandwiches - Gives 2 items
	public static final Item STEAK_SANDWICH = basicFood(9, 12.4f);
	public static final Item CHICKEN_SANDWICH = basicFood(8, 9.6f);
	public static final Item PORK_SANDWICH = basicFood(9, 12.4f);
	public static final Item MUTTON_SANDWICH = basicFood(8, 10.8f);
	public static final Item FISH_SANDWICH = basicFood(7, 9f);
	public static final Item RABBIT_SANDWICH = basicFood(7, 9f);
	public static final Item BERRY_JAM_SANDWICH = basicFood(4, 4.3f);
	public static final Item APPLE_JAM_SANDWICH = basicFood(6, 5.1f);

	// Pocket - Gives 2 items
	public static final Item STEAK_POCKET = basicFood(11, 10.9f);
	public static final Item CHICKEN_POCKET = basicFood(10, 10f);
	public static final Item PORK_POCKET = basicFood(11, 10.9f);
	public static final Item MUTTON_POCKET = basicFood(10, 9.8f);
	public static final Item FISH_POCKET = basicFood(9, 8.5f);
	public static final Item RABBIT_POCKET = basicFood(9, 9.5f);

	// Tacos - Gives 2 Items
	public static final Item STEAK_TACO = basicFood(7, 9.4f);
	public static final Item CHICKEN_TACO = basicFood(6, 6.6f);
	public static final Item PORK_TACO = basicFood(7, 9.4f);
	public static final Item MUTTON_TACO = basicFood(6, 7.8f);
	public static final Item FISH_TACO = basicFood(5, 6f);
	public static final Item RABBIT_TACO = basicFood(5, 6f);

	// Cheese Burgers - Gives 2 Items
	public static final Item STEAK_CHEESEBURGER = basicFood(9, 12.6f);
	public static final Item CHICKEN_CHEESEBURGER = basicFood(8, 10.2f);
	public static final Item PORK_CHEESEBURGER = basicFood(9, 12.6f);
	public static final Item MUTTON_CHEESEBURGER = basicFood(8, 11.6f);
	public static final Item FISH_CHEESEBURGER = basicFood(7, 9.5f);
	public static final Item RABBIT_CHEESEBURGER = basicFood(7, 9.5f);

	// The rest of the stews - Gives 1 item
	public static final MushroomStewItem STEAK_STEW = basicBowlFood(14, 15.8f);
	public static final MushroomStewItem CHICKEN_STEW = basicBowlFood(11, 11.7f);
	public static final MushroomStewItem PORK_STEW = basicBowlFood(14, 15.8f);
	public static final MushroomStewItem MUTTON_STEW = basicBowlFood(11, 15.6f);
	public static final MushroomStewItem FISH_STEW = basicBowlFood(10, 10.2f);
	// No need to add Rabbit Stew!

	// Jams - Gives 1 Item
	public static final Item BERRY_JAM = basicJam(2, 1.5f, SoundEvents.ITEM_HONEY_BOTTLE_DRINK);
	public static final Item APPLE_JAM = basicJam(5, 4.6f, SoundEvents.ITEM_HONEY_BOTTLE_DRINK);

	// Pies - Gives 1 Item
	public static final Item SHEPHERDS_PIE = basicFood(9, 10.6f);
	public static final Item BERRY_PIE = basicFood(6, 4.5f);
	public static final Item APPLE_PIE = basicFood(4, 3.4f);

	// Cakes - Give 1 item
	public static final Block CARROT_CAKE = new BasicCakeBlock();
	public static final Block CHOCOLATE_CAKE = new BasicCakeBlock();
	public static final Block RED_VELVET_CAKE = new BasicCakeBlock();
	public static final Item CARROT_CAKE_ITEM = new BlockItem(CARROT_CAKE, new Item.Settings().group(ItemGroup.FOOD));
	public static final Item CHOCOLATE_CAKE_ITEM = new BlockItem(CHOCOLATE_CAKE, new Item.Settings().group(ItemGroup.FOOD));
	public static final Item RED_VELVET_CAKE_ITEM = new BlockItem(RED_VELVET_CAKE, new Item.Settings().group(ItemGroup.FOOD));

	// Halite and Salt
	public static final Block HALITE = new GlassBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(1.25F, 4.2F)
			.sounds(BlockSoundGroup.BASALT).nonOpaque().solidBlock(Baking::never).blockVision(Baking::never));
	public static final Block HALITE_STAIR = new StairBlock(HALITE.getDefaultState(), FabricBlockSettings.copy(HALITE));
	public static final Block HALITE_SLAB = new SlabBlock(FabricBlockSettings.copy(HALITE));
	public static final Block HALITE_WALL = new WallBlock(FabricBlockSettings.copy(HALITE));
	public static final Block POLISHED_HALITE = new GlassBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(1.25F, 4.2F)
			.sounds(BlockSoundGroup.BASALT).nonOpaque().solidBlock(Baking::never).blockVision(Baking::never));
	public static final Block POLISHED_HALITE_STAIR = new StairBlock(POLISHED_HALITE.getDefaultState(), FabricBlockSettings.copy(POLISHED_HALITE));
	public static final Block POLISHED_HALITE_SLAB = new SlabBlock(FabricBlockSettings.copy(POLISHED_HALITE));
	public static final Block POLISHED_HALITE_WALL = new WallBlock(FabricBlockSettings.copy(POLISHED_HALITE));
	public static final Item SALT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
	private static final ConfiguredFeature<?, ?> HALITE_DESERT = Feature.ORE
			.configure(new OreFeatureConfig(
					OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
					HALITE.getDefaultState(),
					33)) // vein size
			.decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(
					0, // bottom offset
					0, // min y level
					79))) // max y level
			.spreadHorizontally()
			.repeat(10); // number of veins per chunk

	// Raw/Cooked goods - Give 1 item
	public static final Item EGG_WHITES = new BottledItem( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.2f)
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20*10), .1f).build()), SoundEvents.ENTITY_WITCH_DRINK, true);
	public static final Item EGG_YOLK = new BottledItem( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.1f).snack()
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20*10), .1f).build()), SoundEvents.ENTITY_WITCH_DRINK);
	public static final Item CHICKEN_NUGGETS = new Item( new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.6f)
			.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*10), .3f).build()));
	public static final Item FRENCH_FRIES = basicFood(1, 0.6f);
	public static final Item BACON = basicFood(3, 1.8f);
	public static final Item JERKY = basicFood(3, 1.8f);
	public static final Item CUT_SALMON = basicFood(2, 0.4f);
	public static final Item COOKED_CHICKEN_NUGGETS = basicFood(3, 3.2f);
	public static final Item COOKED_FRENCH_FRIES = basicFood(2, 1.8f);
	public static final Item COOKED_EGG = basicFood(2, 3.3f, 16);
	public static final Item MERINGUE = basicJam(1, 0.7f, SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK);
	public static final Item MAYONNAISE = basicJam(1, 0.5f, SoundEvents.ENTITY_WITCH_DRINK);
	// Smoker & Campfire Exclusive
	public static final Item SMOKED_BACON = basicFood(8, 13.8f);
	public static final Item SMOKED_JERKY = basicFood(8, 13.8f);
	public static final Item SMOKED_SALMON = basicFood(6, 10.5f);

	// Mill
	public static final Block MILL = new Mill(FabricBlockSettings.copy(Blocks.STONECUTTER));
	public static final BlockItem MILL_ITEM = new BlockItem(MILL, new Item.Settings().group(ItemGroup.DECORATIONS));
	private static final String mill_stat = "interact_with_mill";
	private static final String mill_rtype_id = "milling";
	public static final Identifier INTERACT_WITH_MILL = new Identifier(ID, mill_stat);
	public static ScreenHandlerType<MillScreenHandler> MILL_SCREEN;
	public static RecipeType<MillingRecipe> MILLING;
	public static RecipeSerializer<MillingRecipe> MILLING_SERIALIZER = RecipeSerializer.register(mill_rtype_id, new CuttingRecipe.Serializer(MillingRecipe::new));

	// Ingredients
	public static final Item FLOUR = basicIngredient();
	public static final Item COCOA_POWDER = basicIngredient();
	public static final Item BACON_BITS = basicFood(2, 5.2f);
	public static final Item DOUGH = basicIngredient(); //  Henry - The inspiration behind the code, my rock and my brain - Redeemed
	public static final Item PASTA_DOUGH = basicIngredient();
	public static final Item LINGUINE = basicIngredient();
	public static final Item MACARONI = basicIngredient();

	// Cups
	public static final Item CUP = new CupItem(new Item.Settings().group(ItemGroup.MISC).maxCount(16));
	public static final Item MILK_CUP = new MilkCupItem();
	public static final Item WATER_CUP = new BasicDrink(CUP);
	public static final Item CHEESE_CUP = new BasicDrink(CUP, 0, 0.3f);
	public static final Item CHOCOLATE_MILK = new BasicDrink(CUP, 1, 1.0f);

	// Misc - Item amt listed individually
	public static final Item BAGEL = basicFood(7, 6.5f);
	public static final Item DONUT = basicFood(7, 7f);
	public static final Item BROWNIE = basicFood(2, 2.6f);
	public static final Item CHEESE = new BasicDrink(Items.BUCKET, 2, 3.5f);
	public static final Item MACARONI_N_CHEESE = basicBowlFood(4, 6.5f);
	public static final Item BACON_MACARONI_N_CHEESE = basicBowlFood(6, 11.2f);
	public static final Item LOADED_FRIES = basicFood(3, 3.5f);
	public static final Item LOADED_POTATO = basicFood(8, 10.2f);
	public static final Item MASHED_POTATOES = basicFood(4, 4.5f);
	public static final MushroomStewItem VEGGIE_MEDLEY = basicBowlFood(9, 7f);
	public static final MushroomStewItem FRUIT_SALAD = basicBowlFood(8, 4f);
	public static final MidasSaladItem MIDAS_SALAD = new MidasSaladItem( new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(14).saturationModifier(35f).alwaysEdible() // Give 1 item
			.statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 20*120), 1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20*20, 1), 1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20*60), 1f).build()));
	public static final Item DISGUSTING_DISH = new MushroomStewItem( new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.5f).alwaysEdible() // Give 1 item
			.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*30, 2), 1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20*30), 1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.POISON, 20*90, 3), 1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20*30), 1f).build()));

	@Override
	public void onInitialize() {

		// Sandwiches
		register("steak_sandwich", STEAK_SANDWICH);
		register("chicken_sandwich", CHICKEN_SANDWICH);
		register("pork_sandwich", PORK_SANDWICH);
		register("mutton_sandwich", MUTTON_SANDWICH);
		register("fish_sandwich", FISH_SANDWICH);
		register("rabbit_sandwich", RABBIT_SANDWICH);
		register("berry_jam_sandwich", BERRY_JAM_SANDWICH);
		register("apple_jam_sandwich", APPLE_JAM_SANDWICH);

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
		registerBlock("carrot_cake", CARROT_CAKE, ItemGroup.FOOD);
		registerBlock("chocolate_cake", CHOCOLATE_CAKE, ItemGroup.FOOD);
		registerBlock("red_velvet_cake", RED_VELVET_CAKE, ItemGroup.FOOD);

		// Halite & Salt
		registerBlock("halite", HALITE, ItemGroup.BUILDING_BLOCKS);
		registerBlock("halite_stairs", HALITE_STAIR, ItemGroup.BUILDING_BLOCKS);
		registerBlock("halite_slab", HALITE_SLAB, ItemGroup.BUILDING_BLOCKS);
		registerBlock("halite_wall", HALITE_WALL, ItemGroup.BUILDING_BLOCKS);
		registerBlock("polished_halite", POLISHED_HALITE, ItemGroup.BUILDING_BLOCKS);
		registerBlock("polished_halite_stairs", POLISHED_HALITE_STAIR, ItemGroup.BUILDING_BLOCKS);
		registerBlock("polished_halite_slab", POLISHED_HALITE_SLAB, ItemGroup.BUILDING_BLOCKS);
		registerBlock("polished_halite_wall", POLISHED_HALITE_WALL, ItemGroup.BUILDING_BLOCKS);
		register("salt", SALT);
		RegistryKey<ConfiguredFeature<?, ?>> haliteDesert = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
				new Identifier(ID, "halite_desert"));
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, haliteDesert.getValue(), HALITE_DESERT);
		Predicate<BiomeSelectionContext> selector = BiomeSelectors.includeByKey( // Deserts, Oceans, and Rivers
				BiomeKeys.DESERT, BiomeKeys.DESERT_HILLS, BiomeKeys.DESERT_LAKES, BiomeKeys.BADLANDS,
				BiomeKeys.BADLANDS_PLATEAU, BiomeKeys.MODIFIED_BADLANDS_PLATEAU, BiomeKeys.ERODED_BADLANDS,
				BiomeKeys.MODIFIED_WOODED_BADLANDS_PLATEAU, BiomeKeys.WOODED_BADLANDS_PLATEAU, BiomeKeys.OCEAN,
				BiomeKeys.COLD_OCEAN, BiomeKeys.DEEP_COLD_OCEAN, BiomeKeys.DEEP_FROZEN_OCEAN,
				BiomeKeys.DEEP_LUKEWARM_OCEAN, BiomeKeys.DEEP_OCEAN, BiomeKeys.DEEP_WARM_OCEAN, BiomeKeys.FROZEN_OCEAN,
				BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.WARM_OCEAN, BiomeKeys.RIVER, BiomeKeys.FROZEN_RIVER
		);
		BiomeModifications.addFeature(selector, GenerationStep.Feature.UNDERGROUND_ORES, haliteDesert);

		// Cheese Burgers
		register("steak_cheeseburger", STEAK_CHEESEBURGER);
		register("chicken_cheeseburger", CHICKEN_CHEESEBURGER);
		register("pork_cheeseburger", PORK_CHEESEBURGER);
		register("mutton_cheeseburger", MUTTON_CHEESEBURGER);
		register("fish_cheeseburger", FISH_CHEESEBURGER);
		register("rabbit_cheeseburger", RABBIT_CHEESEBURGER);

		// Jams
		register("berry_jam", BERRY_JAM);
		register("apple_jam", APPLE_JAM);

		// Pies
		register("shepherds_pie", SHEPHERDS_PIE);
		register("berry_pie", BERRY_PIE);
		register("apple_pie", APPLE_PIE);

		// Ingredients
		register("flour", FLOUR);
		register("cocoa_powder", COCOA_POWDER);
		register("bacon_bits", BACON_BITS);
		register("dough", DOUGH);
		register("pasta_dough", PASTA_DOUGH);
		register("linguine", LINGUINE);
		register("macaroni", MACARONI);
		register("cheese", CHEESE);

		// Stone cut Goods
		register("chicken_nuggets", CHICKEN_NUGGETS);
		register("french_fries", FRENCH_FRIES);
		register("bacon", BACON);
		register("jerky", JERKY);
		register("cut_salmon", CUT_SALMON);

		// Raw/Cooked Goods
		register("egg_whites", EGG_WHITES);
		register("egg_yolk", EGG_YOLK);
		register("cooked_chicken_nuggets", COOKED_CHICKEN_NUGGETS);
		register("cooked_french_fries", COOKED_FRENCH_FRIES);
		register("cooked_egg", COOKED_EGG);
		register("smoked_jerky", SMOKED_JERKY);
		register("smoked_bacon", SMOKED_BACON);
		register("smoked_salmon", SMOKED_SALMON);
		register("mayonnaise", MAYONNAISE);
		register("meringue", MERINGUE);

		// Mill
		Identifier mill_id = new Identifier(ID, "mill");
		MILLING = RecipeType.register(mill_rtype_id);
		Registry.register(Registry.CUSTOM_STAT, mill_stat, INTERACT_WITH_MILL);
		Stats.CUSTOM.getOrCreateStat(INTERACT_WITH_MILL, StatFormatter.DEFAULT);
		MILL_SCREEN = ScreenHandlerRegistry.registerSimple(mill_id, MillScreenHandler::new);
		ScreenRegistry.register(MILL_SCREEN, MillScreen::new);
		Registry.register(Registry.BLOCK, mill_id, MILL);
		Registry.register(Registry.ITEM, mill_id, MILL_ITEM);

		// Cups
		register("cup", CUP);
		register("cup_of_milk", MILK_CUP);
		register("cup_of_water", WATER_CUP);
		register("cup_of_cheese", CHEESE_CUP);
		register("chocolate_milk", CHOCOLATE_MILK);

		// Misc
		register("bagel", BAGEL);
		register("donut", DONUT);
		register("brownie", BROWNIE);
		register("macaroni_n_cheese", MACARONI_N_CHEESE);
		register("bacon_macaroni_n_cheese", BACON_MACARONI_N_CHEESE);
		register("loaded_fries", LOADED_FRIES);
		register("loaded_potato", LOADED_POTATO);
		register("mashed_potatoes", MASHED_POTATOES);
		register("veggie_medley", VEGGIE_MEDLEY);
		register("fruit_salad", FRUIT_SALAD);
		register("midas_salad", MIDAS_SALAD);
		register("disgusting_dish", DISGUSTING_DISH);

	}
}
