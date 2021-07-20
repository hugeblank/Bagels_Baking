package dev.elexi.hugeblank.bagels_baking;

import dev.elexi.hugeblank.bagels_baking.block.*;
import dev.elexi.hugeblank.bagels_baking.block.cauldron.BakingCauldronBehavior;
import dev.elexi.hugeblank.bagels_baking.block.cauldron.LiquidCheeseCauldronBlock;
import dev.elexi.hugeblank.bagels_baking.block.cauldron.SeparatorCauldron;
import dev.elexi.hugeblank.bagels_baking.block.sign.SignTypeRegistry;
import dev.elexi.hugeblank.bagels_baking.entity.BakingVillagerTrades;
import dev.elexi.hugeblank.bagels_baking.entity.TomatoEntity;
import dev.elexi.hugeblank.bagels_baking.entity.boat.BasicBoatRegistry;
import dev.elexi.hugeblank.bagels_baking.item.*;
import dev.elexi.hugeblank.bagels_baking.recipe.MillingRecipe;
import dev.elexi.hugeblank.bagels_baking.screen.MillScreenHandler;
import dev.elexi.hugeblank.bagels_baking.world.BakingConfiguredFeatures;
import dev.elexi.hugeblank.bagels_baking.world.biome.BakingBiomes;
import dev.elexi.hugeblank.bagels_baking.world.tree.CherrySaplingGenerator;
import dev.elexi.hugeblank.bagels_baking.world.tree.LemonSaplingGenerator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class Baking implements ModInitializer {

	public static final String ID = "bagels_baking";

	// Beautification Functions

	private static FoodComponent.Builder foodComponent(int hunger, float saturation) {
		return new FoodComponent.Builder().hunger(hunger).saturationModifier((saturation/hunger)/2.0f);
	}

	private static Item basicFood(FoodComponent.Builder fc, int maxStack) {
		return new Item(new Item.Settings().maxCount(maxStack).group(ItemGroup.FOOD).food(fc.build()));
	}

	private static Item basicFood(FoodComponent.Builder fc) {
		return basicFood(fc, 64);
	}

	private static Item basicFood(int hunger, float saturation) {
		return basicFood(foodComponent(hunger, saturation), 64);
	}

	private static Item basicIngredient() {
			return new Item(new Item.Settings().group(ItemGroup.MATERIALS).maxCount(64));
	}

	private static MushroomStewItem basicBowlFood(int hunger, float saturation) {
		return new MushroomStewItem(new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(foodComponent(hunger, saturation).build()));
	}

	private static BottledItem basicJam(int hunger, float saturation, SoundEvent drinkSound) {
		return new BottledItem(new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(foodComponent(hunger, saturation).build()), drinkSound);
	}


	private static void registerItem(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(ID, name), item);
	}

	private static void registerBlock(String name, Block block, ItemGroup group) {
		Identifier id = new Identifier(ID, name);
		Registry.register(Registry.BLOCK, id, block);
		if (group != null) {
			Item item = new BlockItem(block, new Item.Settings().group(group));
			Registry.register(Registry.ITEM, id, item);
		}
	}

	private static void registerBlock(String name, Block block) {
		Identifier id = new Identifier(ID, name);
		Registry.register(Registry.BLOCK, id, block);
	}

	private static void registerBlock(String name, Block block, BlockItem item) {
		Identifier id = new Identifier(ID, name);
		Registry.register(Registry.BLOCK, id, block);
		Registry.register(Registry.ITEM, id, item);
	}

	private static void registerWoodType(String name, Block log) {
		// Stripping
		AbstractBlock.Settings settings = FabricBlockSettings.copy(Blocks.OAK_PLANKS);
		Block strippedLog = new BasicLogBlock(() -> log);
		Block wood = new BasicLogBlock();
		Block strippedWood = new BasicLogBlock(() -> wood);
		Block planks = new Block(settings);

		// Registering of all wood related things - pain.
		registerBlock(name + "_log", log, ItemGroup.BUILDING_BLOCKS);
		registerBlock(name + "_wood", wood, ItemGroup.BUILDING_BLOCKS);
		registerBlock("stripped_" + name + "_log", strippedLog, ItemGroup.BUILDING_BLOCKS);
		registerBlock("stripped_" + name + "_wood", strippedWood, ItemGroup.BUILDING_BLOCKS);
		registerBlock(name + "_planks", planks, ItemGroup.BUILDING_BLOCKS);
		registerBlock(name + "_pressure_plate", new BasicPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, settings), ItemGroup.REDSTONE);
		registerBlock(name + "_button", new BasicWoodenButtonBlock(settings), ItemGroup.REDSTONE);
		registerBlock(name + "_door", new BasicDoorBlock(settings), ItemGroup.DECORATIONS);
		registerBlock(name + "_fence_gate", new FenceGateBlock(settings), ItemGroup.DECORATIONS);
		registerBlock(name + "_fence", new FenceBlock(settings), ItemGroup.DECORATIONS);
		registerBlock(name + "_slab", new SlabBlock(settings), ItemGroup.BUILDING_BLOCKS);
		registerBlock(name + "_stairs", new StairBlock(log.getDefaultState(), settings), ItemGroup.BUILDING_BLOCKS);
		registerBlock(name + "_trapdoor", new BasicTrapdoorBlock(settings), ItemGroup.DECORATIONS);
		// BOAT
		BasicBoatRegistry.register(name, planks.asItem());

		// SIGN
		SignType type = SignTypeRegistry.register(name);
		AbstractBlock.Settings signSettings = FabricBlockSettings.copy(Blocks.OAK_SIGN);
		SignBlock sign = new SignBlock(signSettings, type);
		WallSignBlock wall_sign = new WallSignBlock(signSettings, type);

		registerBlock(name + "_sign", sign);
		registerBlock(name + "_wall_sign", wall_sign);
		registerItem(name + "_sign", new SignItem(new FabricItemSettings().group(ItemGroup.DECORATIONS).maxCount(16), sign, wall_sign));
	}

	private static void registerCakeType(String name, BasicCakeBlock cake) {
		registerBlock(name, cake, ItemGroup.FOOD);
		registerBlock("candle_" + name, new BasicCandleCakeBlock(Blocks.CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CAKE).luminance((state) -> (Boolean)state.get(Properties.LIT) ? 3 : 0)));
		registerBlock("white_candle_" + name, new BasicCandleCakeBlock(Blocks.WHITE_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("orange_candle_" + name, new BasicCandleCakeBlock(Blocks.ORANGE_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("magenta_candle_" + name, new BasicCandleCakeBlock(Blocks.MAGENTA_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("light_blue_candle_" + name, new BasicCandleCakeBlock(Blocks.LIGHT_BLUE_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("yellow_candle_" + name, new BasicCandleCakeBlock(Blocks.YELLOW_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("lime_candle_" + name, new BasicCandleCakeBlock(Blocks.LIME_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("pink_candle_" + name, new BasicCandleCakeBlock(Blocks.PINK_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("gray_candle_" + name, new BasicCandleCakeBlock(Blocks.GRAY_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("light_gray_candle_" + name, new BasicCandleCakeBlock(Blocks.LIGHT_GRAY_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("cyan_candle_" + name, new BasicCandleCakeBlock(Blocks.CYAN_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("purple_candle_" + name, new BasicCandleCakeBlock(Blocks.PURPLE_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("blue_candle_" + name, new BasicCandleCakeBlock(Blocks.BLUE_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("brown_candle_" + name, new BasicCandleCakeBlock(Blocks.BROWN_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("green_candle_" + name, new BasicCandleCakeBlock(Blocks.GREEN_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("red_candle_" + name, new BasicCandleCakeBlock(Blocks.RED_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
		registerBlock("black_candle_" + name, new BasicCandleCakeBlock(Blocks.BLACK_CANDLE, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE)));
	}

	public static boolean never(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	// Sandwiches - Gives 2 items
	public static final Item STEAK_SANDWICH = basicFood(9, 12.4f);
	public static final Item CHICKEN_SANDWICH = basicFood(8, 9.6f);
	public static final Item PORK_SANDWICH = basicFood(9, 12.4f);
	public static final Item MUTTON_SANDWICH = basicFood(8, 10.8f);
	public static final Item FISH_SANDWICH = basicFood(7, 9f);
	public static final Item RABBIT_SANDWICH = basicFood(7, 9f);
	public static final Item CHEESE_SANDWICH = basicFood(3, 2.5f);
	public static final Item GRILLED_CHEESE_SANDWICH = basicFood(6, 5.5f);
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
	public static final Item STEAK_CHEESEBURGER = basicFood(9, 12.9f);
	public static final Item CHICKEN_CHEESEBURGER = basicFood(8, 10.1f);
	public static final Item PORK_CHEESEBURGER = basicFood(9, 12.9f);
	public static final Item MUTTON_CHEESEBURGER = basicFood(8, 11.3f);
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

	// Halite Blocks
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

	// Raw/Cooked goods - Give 1 item
	public static final Item CALAMARI = basicFood(2, 1.2f);
	public static final Item COOKED_CALAMARI = basicFood(foodComponent(6, 8.5f).meat());
	public static final Item BATTERED_CALAMARI = basicFood(3, 1.8f);
	public static final Item FRIED_CALAMARI = basicFood(8, 10.2f);
	public static final Item CHICKEN_NUGGETS = new Item( new Item.Settings().group(ItemGroup.FOOD).food(foodComponent(2, 0.6f)
			.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*10), .3f).build()));
	public static final Item COOKED_CHICKEN_NUGGETS = basicFood(3, 3.2f);
	public static final Item BATTERED_CHICKEN_NUGGETS = basicFood(foodComponent(3, 0.9f)
			.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*10), .3f));
	public static final Item FRIED_CHICKEN_NUGGETS = basicFood(4, 4.8f);
	public static final Item BATTERED_CHICKEN = basicFood(foodComponent(3, 1.5f)
			.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*10), .3f));
	public static final Item FRIED_CHICKEN = basicFood(7, 8.5f);
	public static final Item FRENCH_FRIES = basicFood(1, 0.6f);
	public static final Item COOKED_FRENCH_FRIES = basicFood(2, 1.8f);
	public static final Item EGG_WHITES = new BottledItem( new Item.Settings().group(ItemGroup.FOOD).food(foodComponent(1, 0.2f)
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20*10), .1f).build()), SoundEvents.ENTITY_WITCH_DRINK, true);
	public static final Item MERINGUE = basicJam(1, 0.7f, SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK);
	public static final Item EGG_YOLK = new BottledItem( new Item.Settings().group(ItemGroup.FOOD).food(foodComponent(1, 0.1f).snack()
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20*10), .1f).build()), SoundEvents.ENTITY_WITCH_DRINK, true);
	public static final Item MAYONNAISE = basicJam(1, 0.5f, SoundEvents.ENTITY_WITCH_DRINK);
	public static final Item PIZZA = basicFood(2, 2.5f);
	public static final Item COOKED_PIZZA = basicFood(5, 7.8f);
	public static final Item BACON = basicFood(3, 1.8f);
	public static final Item SMOKED_BACON = basicFood(8, 13.8f);
	public static final Item JERKY = basicFood(3, 1.8f);
	public static final Item SMOKED_JERKY = basicFood(8, 13.8f);
	public static final Item CUT_SALMON = basicFood(2, 0.4f);
	public static final Item SMOKED_SALMON = basicFood(6, 10.5f);
	public static final Item WILD_RICE_BALL = basicFood(1, 0.3f);
	public static final Item RICE_BALL = basicFood(1, 0.3f);
	public static final Item COOKED_WILD_RICE_BALL = basicFood(2, 0.7f);
	public static final Item COOKED_RICE_BALL = basicFood(2, 0.7f);

	// Mill
	public static final Identifier MILL_ID = new Identifier(ID, "mill");
	public static final Block MILL = new Mill(FabricBlockSettings.copy(Blocks.STONECUTTER));
	public static final BlockItem MILL_ITEM = new BlockItem(MILL, new Item.Settings().group(ItemGroup.DECORATIONS));
	private static final String mill_stat = "interact_with_mill";
	public static final Identifier INTERACT_WITH_MILL = new Identifier(ID, mill_stat);
	public static ScreenHandlerType<MillScreenHandler> MILL_SCREEN;

	// Cauldron
	public static final Block BATTER_CAULDRON = new LeveledCauldronBlock(FabricBlockSettings.copy(Blocks.WATER_CAULDRON), (precipitation) -> false, BakingCauldronBehavior.BATTER_CAULDRON_BEHAVIOR);
	public static final Block LIQUID_CHEESE_CAULDRON = new LiquidCheeseCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON), CauldronBehavior.createMap()); // do nothing
	public static final Block SOLID_CHEESE_CAULDRON = new LiquidCheeseCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON), BakingCauldronBehavior.SOLID_CHEESE_CAULDRON_BEHAVIOR);
	public static final Block COFFEE_CAULDRON = new LeveledCauldronBlock(FabricBlockSettings.copy(Blocks.WATER_CAULDRON), (precipitation) -> false, BakingCauldronBehavior.COFFEE_CAULDRON_BEHAVIOR);
	public static final Block TEA_CAULDRON = new LeveledCauldronBlock(FabricBlockSettings.copy(Blocks.WATER_CAULDRON), (precipitation) -> false, BakingCauldronBehavior.TEA_CAULDRON_BEHAVIOR);
	public static final Block CREAMY_COFFEE_CAULDRON = new LeveledCauldronBlock(FabricBlockSettings.copy(Blocks.WATER_CAULDRON), (precipitation) -> false, BakingCauldronBehavior.CREAMY_COFFEE_CAULDRON_BEHAVIOR);
	public static final Block CREAMY_TEA_CAULDRON = new LeveledCauldronBlock(FabricBlockSettings.copy(Blocks.WATER_CAULDRON), (precipitation) -> false, BakingCauldronBehavior.CREAMY_TEA_CAULDRON_BEHAVIOR);
	public static final Block SEPARATOR_CAULDRON = new SeparatorCauldron(FabricBlockSettings.copy(Blocks.WATER_CAULDRON), BakingCauldronBehavior.SEPARATOR_CAULDRON_BEHAVIOR);

	// Ingredients
	public static final Item SALT = basicIngredient();
	public static final Item FLOUR = basicIngredient();
	public static final Item CORN_MEAL = basicIngredient();
	public static final Item COCOA_POWDER = basicIngredient();
	public static final Item GROUND_COFFEE = basicIngredient();
	public static final Item GROUND_TEA = basicIngredient();
	public static final Item BATTER = basicBowlFood(1, 0.1f);
	public static final Item BACON_BITS = basicFood(2, 5.2f);
	public static final Item MASHED_POTATOES = basicFood(4, 4.5f);
	public static final Item DOUGH_INGREDIENTS = basicIngredient();
	public static final Item DOUGH = basicIngredient(); //  Henry - The inspiration behind the code, my rock and my brain - Redeemed
	public static final Item PASTA_DOUGH_INGREDIENTS = basicIngredient();
	public static final Item PASTA_DOUGH = basicIngredient();
	public static final Item LINGUINE = basicIngredient();
	public static final Item MACARONI = basicIngredient();
	public static final Item CHEESE = new BasicDrink(Items.BUCKET, 2, 1.0f);
	public static final Item TOMATO_SAUCE = basicIngredient();
	public static final Item WHEAT_CEREAL = basicIngredient();
	public static final Item CORN_CEREAL = basicIngredient();
	public static final Item RICE_CEREAL = basicIngredient();
	// hunt was here <3 - redeemed by rrricohu on 3/21/21
	// Pedrospeeder - redeemed on 3/22/21
	// <コ:彡 LOOK FELLOW PROGRAMMER I'M CATHULO - redeemed by Pedrospeeder on 3/22/21
	/*
	fatmanchummy
	fatmanchummy
	fatmanchummy
	fatmanchummy
	fatmanchummy
	fatmanchummy
	redeemed on 3/22/21
	*/

	// Cups
	public static final Item CUP = new CupItem(new Item.Settings().group(ItemGroup.MISC).maxCount(16));
	public static final Item MILK_CUP = new MilkCupItem();
	public static final Item WATER_CUP = new BasicDrink(CUP, true);
	public static final Item CHOCOLATE_MILK = new BasicDrink(CUP, 1, 1.0f); // frick i would like some choccy milk rn - redeemed by rrricohu on 3/21/21
	public static final Item CREAMER_CUP = new BasicDrink(CUP, 0, 0.1f);
	public static final Item COFFEE_CUP = new BasicDrink(2, 1.5f, new StatusEffectInstance(StatusEffects.SPEED, 20*15, 1));
	public static final Item COFFEE_W_CREAMER = new BasicDrink(2, 1.5f, new StatusEffectInstance(StatusEffects.SPEED, 20*30));
	public static final Item TEA_CUP = new BasicDrink(2, 1.8f, new StatusEffectInstance(StatusEffects.SPEED, 20*10));
	public static final Item TEA_W_CREAMER = new BasicDrink(2, 1.8f, new StatusEffectInstance(StatusEffects.SPEED, 20*20));

	// Sodie Pop
	public static final Item SUGAR_WATER = new BasicDrink(CUP, true);
	public static final Item CLUB_SODA = new BasicDrink(CUP, 1, 1f, true); // coal
	public static final Item ROOT_BEER = new BasicDrink(CUP, 2, 1.8f); // beetUwUoot
	public static final Item COLA = new BasicDrink(CUP, 2, 1.8f); // cocoa beans
	public static final Item FRUITY_SODA = new BasicDrink(CUP, 2, 1.8f); // wild berries
	public static final Item MOUNTAIN_FOUNTAIN = new BasicDrink(CUP, 2, 1.8f); // melon
	public static final Item CACTUS_CHILLER = new BasicDrink(CUP, 2, 1.8f); // cactus
	public static final Item GRAPE_SODA = new BasicDrink(CUP, 2, 1.8f); // chorus fruit

	// Plates & Meals
	public static final Item UNFIRED_PLATE = basicIngredient();
	public static final Item PLATE = basicIngredient();
	public static final Item STEAK_MEAL = new PlatedItem(17, 21.1f);
	public static final Item PORK_MEAL = new PlatedItem(23, 22.3f);
	public static final Item CHICKEN_MEAL = new PlatedItem(12, 12.5f);
	public static final Item MUTTON_MEAL = new PlatedItem(17, 18.3f);
	public static final Item RABBIT_MEAL = new PlatedItem(22, 28.1f);

	// Crops - Here's to v0.3!
	public static final Block COFFEE = new CocoaBlock(FabricBlockSettings.copy(Blocks.COCOA));
	public static final BlockItem COFFEE_BEANS = new BlockItem(COFFEE, new Item.Settings().group(ItemGroup.MATERIALS));
	public static final Block TEA = new TeaTreeBlock(FabricBlockSettings.copy(Blocks.SWEET_BERRY_BUSH));
	public static final BlockItem TEA_SEEDS = new BlockItem(TEA, new Item.Settings().group(ItemGroup.MATERIALS));
	public static final Item TEA_LEAVES = basicIngredient();
	public static final Item DRIED_TEA_LEAVES = basicIngredient();
	public static final Block COMPRESSED_TEA_BLOCK = new Block(FabricBlockSettings.copy(Blocks.DRIED_KELP_BLOCK));
	public static final DamageSource TEA_TREE_DMGSRC = new DamageSource("tea_tree");
	public static final Block TOMATO_PLANT = new BasicCropBlock(FabricBlockSettings.copy(Blocks.WHEAT));
	public static final Item TOMATO = new TomatoItem(TOMATO_PLANT, new Item.Settings().group(ItemGroup.FOOD).food(
			foodComponent(3, 3.2f).build()
	));
	public static final EntityType<TomatoEntity> TOMATO_THROWABLE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ID, "tomato"),
			FabricEntityTypeBuilder.<TomatoEntity>create()
					.spawnGroup(SpawnGroup.MISC)
					.dimensions(new EntityDimensions(0.25f, 0.25f, true))
					.trackedUpdateRate(10)
					.trackRangeChunks(4)
					.build());
	public static final Block RICE_PLANT = new BasicCropBlock(FabricBlockSettings.copy(Blocks.WHEAT));
	public static final Item WILD_RICE = new BlockItem(RICE_PLANT, new Item.Settings().group(ItemGroup.MISC));
	public static final Item RICE = basicIngredient();
	public static final Block CORN_STALK = new DoubleCropBlock(FabricBlockSettings.copy(Blocks.WHEAT));
	public static final Item CORN = basicFood(3, 2.4f);
	public static final Item COOKED_CORN = basicFood(5, 3.8f);
	public static final Item CORN_SEEDS = new BlockItem(CORN_STALK, new Item.Settings().group(ItemGroup.MISC));
	// deathypooh - requested on 3/22/21
	// solcatowo - requested on 3/22/21

	// Trees - Here's to v0.4!
	public static final Block CHERRY_SAPLING = new BasicSaplingBlock(new CherrySaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING));
	public static final Block POTTED_CHERRY_SAPLING = new FlowerPotBlock(CHERRY_SAPLING, FabricBlockSettings.copy(Blocks.FLOWER_POT));
	public static final Block CHERRY_LOG = new BasicLogBlock();
	public static final Item CHERRIES = basicFood(2, 1.8f);
	public static final Block CHERRY_LEAVES = new BasicLeavesBlock(CHERRIES);
	public static final Block LEMON_SAPLING = new BasicSaplingBlock(new LemonSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING));
	public static final Block POTTED_LEMON_SAPLING = new FlowerPotBlock(LEMON_SAPLING, FabricBlockSettings.copy(Blocks.FLOWER_POT));
	public static final Block LEMON_LOG = new BasicLogBlock();
	public static final Item LEMON = basicFood(1, 0.8f);
	public static final Block LEMON_LEAVES = new BasicLeavesBlock(LEMON);

	// Cheese
	public static final Block CHEESE_BLOCK = new Block(FabricBlockSettings.copy(Blocks.HONEY_BLOCK).sounds(BlockSoundGroup.CANDLE)); // TODO: play with sounds!
	public static final Block CHEESE_LAYER = new BasicLayerBlock(FabricBlockSettings.copy(Blocks.SNOW));
	public static final Item CHEESE_SLICE = new BlockItem(CHEESE_LAYER, new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(0).saturationModifier(0.3f).build()));

	// Misc
	public static final Item BAGEL = basicFood(7, 6.5f);
	public static final Item DONUT = basicFood(7, 7f);
	public static final Item BROWNIE = basicFood(2, 2.6f);
	public static final Item PEPPERONI = basicFood(4, 3.1f);
	public static final Item COOKED_EGG = basicFood(2, 3.3f);
	public static final Item MACARONI_N_CHEESE = basicBowlFood(4, 6.5f);
	public static final Item BACON_MACARONI_N_CHEESE = basicBowlFood(6, 11.2f);
	public static final Item TOMATO_SOUP = basicBowlFood(12, 14.0f);
	public static final Item SALMON_SUSHI = basicFood(4, 6.5f);
	public static final Item SQUID_SUSHI = basicFood(4, 4.7f);
	public static final Item LOADED_FRIES = basicFood(3, 3.5f);
	public static final Item LOADED_POTATO = basicFood(8, 10.2f);
	public static final MushroomStewItem VEGGIE_MEDLEY = basicBowlFood(11, 11.4f);
	public static final MushroomStewItem FRUIT_SALAD = basicBowlFood(8, 4f);
	public static final MidasSaladItem MIDAS_SALAD = new MidasSaladItem( new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(foodComponent(14, 35f).alwaysEdible() // Give 1 item
			.statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 20*120), 1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20*20, 1), 1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20*60), 1f).build()));
	public static final Item DISGUSTING_DISH = new MushroomStewItem( new Item.Settings().group(ItemGroup.FOOD).maxCount(16).food(foodComponent(2, 0.5f).alwaysEdible() // Give 1 item
			.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*30, 2), 1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20*30), 1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.POISON, 20*90, 3), 1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20*30), 1f).build()));
	public static final MushroomStewItem WHEAT_CEREAL_BOWL = basicBowlFood(2, 1.5f);
	public static final MushroomStewItem CORN_CEREAL_BOWL = basicBowlFood(2, 1.7f);
	public static final MushroomStewItem RICE_CEREAL_BOWL = basicBowlFood(2, 1.3f);

	// Stats
	public static final Identifier DAY_OF_WEEK = new Identifier(ID, "day_of_week");
	public static final Identifier TOMATO_KILLS = new Identifier(ID, "tomato_kills");

	@Override
	public void onInitialize() {

		// Sandwiches
		registerItem("steak_sandwich", STEAK_SANDWICH);
		registerItem("chicken_sandwich", CHICKEN_SANDWICH);
		registerItem("pork_sandwich", PORK_SANDWICH);
		registerItem("mutton_sandwich", MUTTON_SANDWICH);
		registerItem("fish_sandwich", FISH_SANDWICH);
		registerItem("rabbit_sandwich", RABBIT_SANDWICH);
		registerItem("berry_jam_sandwich", BERRY_JAM_SANDWICH);
		registerItem("apple_jam_sandwich", APPLE_JAM_SANDWICH);
		registerItem("cheese_sandwich", CHEESE_SANDWICH);
		registerItem("grilled_cheese_sandwich", GRILLED_CHEESE_SANDWICH);

		// Pockets
		registerItem("steak_pocket", STEAK_POCKET);
		registerItem("chicken_pocket", CHICKEN_POCKET);
		registerItem("pork_pocket", PORK_POCKET);
		registerItem("mutton_pocket", MUTTON_POCKET);
		registerItem("fish_pocket", FISH_POCKET);
		registerItem("rabbit_pocket", RABBIT_POCKET);

		// Stews
		registerItem("steak_stew", STEAK_STEW);
		registerItem("chicken_stew", CHICKEN_STEW);
		registerItem("pork_stew", PORK_STEW);
		registerItem("mutton_stew", MUTTON_STEW);
		registerItem("fish_stew", FISH_STEW);
		// No need to add rabbit stew!

		// Tacos
		registerItem("steak_taco", STEAK_TACO);
		registerItem("chicken_taco", CHICKEN_TACO);
		registerItem("pork_taco", PORK_TACO);
		registerItem("mutton_taco", MUTTON_TACO);
		registerItem("fish_taco", FISH_TACO);
		registerItem("rabbit_taco", RABBIT_TACO);

		// Cakes
		registerCakeType("carrot_cake", (BasicCakeBlock) CARROT_CAKE);
		registerCakeType("chocolate_cake", (BasicCakeBlock) CHOCOLATE_CAKE);
		registerCakeType("red_velvet_cake", (BasicCakeBlock) RED_VELVET_CAKE);

		// Halite & Salt
		registerBlock("halite", HALITE, ItemGroup.BUILDING_BLOCKS);
		registerBlock("halite_stairs", HALITE_STAIR, ItemGroup.BUILDING_BLOCKS);
		registerBlock("halite_slab", HALITE_SLAB, ItemGroup.BUILDING_BLOCKS);
		registerBlock("halite_wall", HALITE_WALL, ItemGroup.BUILDING_BLOCKS);
		registerBlock("polished_halite", POLISHED_HALITE, ItemGroup.BUILDING_BLOCKS);
		registerBlock("polished_halite_stairs", POLISHED_HALITE_STAIR, ItemGroup.BUILDING_BLOCKS);
		registerBlock("polished_halite_slab", POLISHED_HALITE_SLAB, ItemGroup.BUILDING_BLOCKS);
		registerBlock("polished_halite_wall", POLISHED_HALITE_WALL, ItemGroup.BUILDING_BLOCKS);
		registerItem("salt", SALT);

		// Cheese Burgers
		registerItem("steak_cheeseburger", STEAK_CHEESEBURGER);
		registerItem("chicken_cheeseburger", CHICKEN_CHEESEBURGER);
		registerItem("pork_cheeseburger", PORK_CHEESEBURGER);
		registerItem("mutton_cheeseburger", MUTTON_CHEESEBURGER);
		registerItem("fish_cheeseburger", FISH_CHEESEBURGER);
		registerItem("rabbit_cheeseburger", RABBIT_CHEESEBURGER);

		// Jams
		registerItem("berry_jam", BERRY_JAM);
		registerItem("apple_jam", APPLE_JAM);

		// Pies
		registerItem("shepherds_pie", SHEPHERDS_PIE);
		registerItem("berry_pie", BERRY_PIE);
		registerItem("apple_pie", APPLE_PIE);

		// Ingredients
		registerItem("flour", FLOUR);
		registerItem("corn_meal", CORN_MEAL);
		registerItem("ground_coffee", GROUND_COFFEE);
		registerItem("ground_tea", GROUND_TEA);
		registerItem("cocoa_powder", COCOA_POWDER);
		registerItem("batter", BATTER);
		registerItem("bacon_bits", BACON_BITS);
		registerItem("dough", DOUGH);
		registerItem("dough_ingredients", DOUGH_INGREDIENTS);
		registerItem("pasta_dough", PASTA_DOUGH);
		registerItem("pasta_dough_ingredients", PASTA_DOUGH_INGREDIENTS);
		registerItem("linguine", LINGUINE);
		registerItem("macaroni", MACARONI);
		registerItem("cheese", CHEESE);
		registerItem("tomato_sauce", TOMATO_SAUCE);
		registerItem("wheat_cereal", WHEAT_CEREAL);
		registerItem("corn_cereal", CORN_CEREAL);
		registerItem("rice_cereal", RICE_CEREAL);

		// Raw/Cooked Goods
		registerItem("calamari", CALAMARI);
		registerItem("cooked_calamari", COOKED_CALAMARI);
		registerItem("battered_calamari", BATTERED_CALAMARI);
		registerItem("fried_calamari", FRIED_CALAMARI);
		registerItem("chicken_nuggets", CHICKEN_NUGGETS);
		registerItem("cooked_chicken_nuggets", COOKED_CHICKEN_NUGGETS);
		registerItem("battered_chicken_nuggets", BATTERED_CHICKEN_NUGGETS);
		registerItem("fried_chicken_nuggets", FRIED_CHICKEN_NUGGETS);
		registerItem("battered_chicken", BATTERED_CHICKEN);
		registerItem("fried_chicken", FRIED_CHICKEN);
		registerItem("french_fries", FRENCH_FRIES);
		registerItem("cooked_french_fries", COOKED_FRENCH_FRIES);
		registerItem("pizza", PIZZA);
		registerItem("cooked_pizza", COOKED_PIZZA);
		registerItem("jerky", JERKY);
		registerItem("smoked_jerky", SMOKED_JERKY);
		registerItem("bacon", BACON);
		registerItem("smoked_bacon", SMOKED_BACON);
		registerItem("cut_salmon", CUT_SALMON);
		registerItem("smoked_salmon", SMOKED_SALMON);
		registerItem("egg_yolk", EGG_YOLK);
		registerItem("mayonnaise", MAYONNAISE);
		registerItem("egg_whites", EGG_WHITES);
		registerItem("meringue", MERINGUE);
		registerItem("wild_rice_ball", WILD_RICE_BALL);
		registerItem("rice_ball", RICE_BALL);
		registerItem("cooked_wild_rice_ball", COOKED_WILD_RICE_BALL);
		registerItem("cooked_rice_ball", COOKED_RICE_BALL);

		// Mill
		Registry.register(Registry.RECIPE_SERIALIZER, MillingRecipe.ID, MillingRecipe.SERIALIZER);
		Registry.register(Registry.CUSTOM_STAT, mill_stat, INTERACT_WITH_MILL);
		Stats.CUSTOM.getOrCreateStat(INTERACT_WITH_MILL, StatFormatter.DEFAULT);
		Registry.register(Registry.BLOCK, MILL_ID, MILL);
		Registry.register(Registry.ITEM, MILL_ID, MILL_ITEM);
		MILL_SCREEN = ScreenHandlerRegistry.registerSimple(Baking.MILL_ID, MillScreenHandler::new);

		// Cups
		registerItem("cup", CUP);
		registerItem("milk_cup", MILK_CUP);
		registerItem("water_cup", WATER_CUP);
		registerItem("chocolate_milk_cup", CHOCOLATE_MILK);
		registerItem("creamer_cup", CREAMER_CUP);
		registerItem("coffee_cup", COFFEE_CUP);
		registerItem("creamy_coffee_cup", COFFEE_W_CREAMER);
		registerItem("tea_cup", TEA_CUP);
		registerItem("creamy_tea_cup", TEA_W_CREAMER);

		// Sodie Pop
		registerItem("sugar_water", SUGAR_WATER);
		registerItem("club_soda", CLUB_SODA);
		registerItem("root_beer", ROOT_BEER);
		registerItem("cola", COLA);
		registerItem("fruity_soda", FRUITY_SODA);
		registerItem("mountain_fountain", MOUNTAIN_FOUNTAIN);
		registerItem("cactus_chiller", CACTUS_CHILLER);
		registerItem("grape_soda", GRAPE_SODA);

		// Plates & Meals
		registerItem("unfired_plate", UNFIRED_PLATE);
		registerItem("plate", PLATE);
		registerItem("steak_meal", STEAK_MEAL);
		registerItem("pork_meal", PORK_MEAL);
		registerItem("chicken_meal", CHICKEN_MEAL);
		registerItem("mutton_meal", MUTTON_MEAL);
		registerItem("rabbit_meal", RABBIT_MEAL);

		// Crops
		registerBlock("coffee", COFFEE);
		registerItem("coffee_beans", COFFEE_BEANS);
		registerBlock("tea", TEA, TEA_SEEDS);
		registerItem("tea_leaves", TEA_LEAVES);
		registerItem("dried_tea_leaves", DRIED_TEA_LEAVES);
		registerBlock("compressed_tea_block", COMPRESSED_TEA_BLOCK, ItemGroup.BUILDING_BLOCKS);
		registerBlock("tomato", TOMATO_PLANT, (BlockItem) TOMATO);
		((BasicCropBlock)TOMATO_PLANT).setSeed(TOMATO);
		registerBlock("wild_rice", RICE_PLANT, (BlockItem) WILD_RICE);
		((BasicCropBlock)RICE_PLANT).setSeed(WILD_RICE);
		registerItem("rice", RICE);
		registerBlock("corn", CORN_STALK);
		((BasicCropBlock)CORN_STALK).setSeed(CORN_SEEDS);
		registerItem("corn", CORN);
		registerItem("cooked_corn", COOKED_CORN);
		registerItem("corn_seeds", CORN_SEEDS);
		// dreamwastaken my beloved <3 - redeemed by KoritsiAlogo on 3/22/21

		// Trees
		registerBlock("cherry_sapling", CHERRY_SAPLING, ItemGroup.DECORATIONS);
		registerBlock("potted_cherry_sapling", POTTED_CHERRY_SAPLING);
		registerWoodType("cherry", CHERRY_LOG);
		registerBlock("cherry_leaves", CHERRY_LEAVES, ItemGroup.DECORATIONS);
		registerItem("cherries", CHERRIES);
		registerBlock("lemon_sapling", LEMON_SAPLING, ItemGroup.DECORATIONS);
		registerBlock("potted_lemon_sapling", POTTED_LEMON_SAPLING);
		registerWoodType("lemon", LEMON_LOG);
		registerBlock("lemon_leaves", LEMON_LEAVES, ItemGroup.DECORATIONS);
		registerItem("lemon", LEMON);

		// Cheese
		registerBlock("cheese_slice", CHEESE_LAYER, (BlockItem) CHEESE_SLICE);
		registerBlock("cheese_block", CHEESE_BLOCK, ItemGroup.BUILDING_BLOCKS);

		// Misc
		registerItem("bagel", BAGEL);
		registerItem("donut", DONUT);
		registerItem("brownie", BROWNIE);
		registerItem("cooked_egg", COOKED_EGG);
		registerItem("pepperoni", PEPPERONI);
		registerItem("salmon_sushi", SALMON_SUSHI);
		registerItem("squid_sushi", SQUID_SUSHI);
		registerItem("macaroni_n_cheese", MACARONI_N_CHEESE);
		registerItem("bacon_macaroni_n_cheese", BACON_MACARONI_N_CHEESE);
		registerItem("tomato_soup", TOMATO_SOUP);
		registerItem("loaded_fries", LOADED_FRIES);
		registerItem("loaded_potato", LOADED_POTATO);
		registerItem("mashed_potatoes", MASHED_POTATOES);
		registerItem("veggie_medley", VEGGIE_MEDLEY);
		registerItem("fruit_salad", FRUIT_SALAD);
		registerItem("midas_salad", MIDAS_SALAD);
		registerItem("disgusting_dish", DISGUSTING_DISH);
		registerItem("wheat_cereal_bowl", WHEAT_CEREAL_BOWL);
		registerItem("corn_cereal_bowl", CORN_CEREAL_BOWL);
		registerItem("rice_cereal_bowl", RICE_CEREAL_BOWL);

		// Trades
		BakingVillagerTrades.init();

		// Configured Features
		BakingConfiguredFeatures.init();

		// Biomes
		BakingBiomes.init();

		// Cauldrons
		BakingCauldronBehavior.registerBehaviors();
		registerBlock("batter_cauldron", BATTER_CAULDRON);
		registerBlock("liquid_cheese_cauldron", LIQUID_CHEESE_CAULDRON);
		registerBlock("solid_cheese_cauldron", SOLID_CHEESE_CAULDRON);
		registerBlock("coffee_cauldron", COFFEE_CAULDRON);
		registerBlock("tea_cauldron", TEA_CAULDRON);
		registerBlock("creamy_coffee_cauldron", CREAMY_COFFEE_CAULDRON);
		registerBlock("creamy_tea_cauldron", CREAMY_TEA_CAULDRON);
		registerBlock("separator_cauldron", SEPARATOR_CAULDRON);

		// Stats
		Registry.register(Registry.CUSTOM_STAT, "day_of_week", DAY_OF_WEEK);
		Stats.CUSTOM.getOrCreateStat(DAY_OF_WEEK, StatFormatter.DEFAULT);
		Registry.register(Registry.CUSTOM_STAT, "tomato_kills", TOMATO_KILLS);
		Stats.CUSTOM.getOrCreateStat(TOMATO_KILLS, StatFormatter.DEFAULT);
	}
}
