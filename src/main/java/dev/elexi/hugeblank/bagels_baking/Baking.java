package dev.elexi.hugeblank.bagels_baking;


import dev.elexi.hugeblank.bagels_baking.block.*;
import dev.elexi.hugeblank.bagels_baking.block.cauldron.BakingCauldronBehavior;
import dev.elexi.hugeblank.bagels_baking.block.cauldron.CheeseCauldronBlock;
import dev.elexi.hugeblank.bagels_baking.block.cauldron.SeparatorCauldron;
import dev.elexi.hugeblank.bagels_baking.block.entity.FermenterBlockEntity;
import dev.elexi.hugeblank.bagels_baking.block.entity.IceBoxBlockEntity;
import dev.elexi.hugeblank.bagels_baking.compat.terrablender.BakingRegion;
import dev.elexi.hugeblank.bagels_baking.entity.BakingVillagerProfessions;
import dev.elexi.hugeblank.bagels_baking.entity.BakingVillagerTrades;
import dev.elexi.hugeblank.bagels_baking.entity.TomatoEntity;
import dev.elexi.hugeblank.bagels_baking.item.*;
import dev.elexi.hugeblank.bagels_baking.mixin.entity.DamageSourceAccessor;
import dev.elexi.hugeblank.bagels_baking.recipe.FermentingRecipe;
import dev.elexi.hugeblank.bagels_baking.recipe.FreezingRecipe;
import dev.elexi.hugeblank.bagels_baking.recipe.MillingRecipe;
import dev.elexi.hugeblank.bagels_baking.recipe.ShapelessRemainderlessRecipe;
import dev.elexi.hugeblank.bagels_baking.screen.MillScreenHandler;
import dev.elexi.hugeblank.bagels_baking.util.WoodType;
import dev.elexi.hugeblank.bagels_baking.world.biome.BakingBiomes;
import dev.elexi.hugeblank.bagels_baking.world.gen.*;
import dev.elexi.hugeblank.bagels_baking.world.gen.placer.BakingPlacers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

import java.util.Random;

public class Baking implements ModInitializer, TerraBlenderApi {

	public static final String ID = "bagels_baking";

	// Beautification Functions

	private static FoodComponent.Builder foodComponent(int hunger, float saturationModifier) {
		return new FoodComponent.Builder().hunger(hunger).saturationModifier(saturationModifier);
	}

	private static Item basicFood(FoodComponent.Builder fc) {
		return new Item(new Item.Settings().maxCount(64).group(ItemGroup.FOOD).food(fc.build()));
	}

	private static Item basicFood(int hunger, float saturation) {
		return basicFood(foodComponent(hunger, saturation));
	}

	private static Item basicIngredient() {
			return new Item(new Item.Settings().group(ItemGroup.MATERIALS).maxCount(64));
	}

	private static StewItem basicBowlFood(int hunger, float saturation) {
		return new StewItem(new Item.Settings().group(ItemGroup.FOOD).recipeRemainder(Items.BOWL).maxCount(16).food(foodComponent(hunger, saturation).build()));
	}

	private static BottledItem basicBottleDrink(int hunger, float saturation, SoundEvent drinkSound) {
		return new BottledItem(new Item.Settings().group(ItemGroup.FOOD).recipeRemainder(Items.GLASS_BOTTLE).maxCount(16).food(foodComponent(hunger, saturation).build()), drinkSound);
	}

	private static BottledItem basicBottleDrink(int hunger, float saturation) {
		return new BottledItem(new Item.Settings().group(ItemGroup.FOOD).recipeRemainder(Items.GLASS_BOTTLE).maxCount(16).food(foodComponent(hunger, saturation).build()));
	}

	private static BasicDrink basicBucketDrink(int hunger, float saturation) {
		return new BasicDrink(new Item.Settings().group(ItemGroup.FOOD).recipeRemainder(Items.BUCKET).maxCount(1), foodComponent(hunger, saturation).build(), Items.BUCKET);
	}

	private static BasicDrink basicCupDrink() {
		return new BasicDrink(new Item.Settings().group(ItemGroup.FOOD).recipeRemainder(Baking.CUP).maxCount(16), Baking.CUP);
	}

	private static BasicDrink basicCupDrink(SoundEvent sound) {
		return new BasicDrink(new Item.Settings().group(ItemGroup.FOOD).recipeRemainder(Baking.CUP).maxCount(16), Baking.CUP, sound);
	}

	private static BasicDrink basicCupDrink(int hunger, float saturation, SoundEvent sound) {
		return new BasicDrink(new Item.Settings().group(ItemGroup.FOOD).recipeRemainder(Baking.CUP).maxCount(16), foodComponent(hunger, saturation).build(), Baking.CUP, sound);
	}

	private static BasicDrink basicCupDrink(int hunger, float saturation) {
		return new BasicDrink(new Item.Settings().group(ItemGroup.FOOD).recipeRemainder(Baking.CUP).maxCount(16), foodComponent(hunger, saturation).build(), Baking.CUP);
	}

	private static BasicDrink basicSuperCupDrink(int hunger, float saturation, StatusEffectInstance effect) {
		return new BasicDrink(new Item.Settings().group(ItemGroup.FOOD).recipeRemainder(Baking.CUP).maxCount(16), foodComponent(hunger, saturation).statusEffect(effect, 1.0f).build(), Baking.CUP);
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

	private static void registerCakeType(String name, BasicCakeBlock cake) {
		registerBlock(name, cake, new BlockItem(cake, new Item.Settings().group(ItemGroup.FOOD).maxCount(1)));
		BakingCompostableItems.registerCompostableItem(1.0f, cake);
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
	public static final Item STEAK_SANDWICH = basicFood(9, 0.7f);
	public static final Item CHICKEN_SANDWICH = basicFood(8, 0.6f);
	public static final Item PORK_SANDWICH = basicFood(9, 0.7f);
	public static final Item MUTTON_SANDWICH = basicFood(8, 0.7f);
	public static final Item FISH_SANDWICH = basicFood(8, 0.7f);
	public static final Item RABBIT_SANDWICH = basicFood(8, 0.6f);
	public static final Item CHEESE_SANDWICH = basicFood(3, 0.1f);
	public static final Item GRILLED_CHEESE_SANDWICH = basicFood(5, 0.6f);
	public static final Item SWEET_BERRY_JAM_SANDWICH = basicFood(7, 0.4f);
	public static final Item APPLE_JAM_SANDWICH = basicFood(8, 0.5f);
	public static final Item GLOW_BERRY_JAM_SANDWICH = basicFood(7, 0.4f);
	public static final Item GRAPE_JAM_SANDWICH = basicFood(6, 0.4f);
	public static final Item CHERRY_JAM_SANDWICH = basicFood(7, 0.4f);
	public static final Item KATSU_SANDWICH = basicFood(13, 0.7f);

	// Pocket - Gives 2 items
	public static final Item STEAK_POCKET = basicFood(12, 0.3f);
	public static final Item CHICKEN_POCKET = basicFood(11, 0.2f);
	public static final Item PORK_POCKET = basicFood(12, 0.3f);
	public static final Item MUTTON_POCKET = basicFood(11, 0.3f);
	public static final Item FISH_POCKET = basicFood(11, 0.3f);
	public static final Item RABBIT_POCKET = basicFood(11, 0.2f);

	// Tacos - Gives 2 Items
	public static final Item STEAK_TACO = basicFood(7, 0.4f);
	public static final Item CHICKEN_TACO = basicFood(6, 0.3f);
	public static final Item PORK_TACO = basicFood(7, 0.4f);
	public static final Item MUTTON_TACO = basicFood(6, 0.4f);
	public static final Item FISH_TACO = basicFood(6, 0.3f);
	public static final Item RABBIT_TACO = basicFood(6, 0.3f);

	// Cheese Burgers - Gives 2 Items
	public static final Item STEAK_CHEESEBURGER = basicFood(9, 0.6f);
	public static final Item CHICKEN_CHEESEBURGER = basicFood(8, 0.5f);
	public static final Item PORK_CHEESEBURGER = basicFood(9, 0.6f);
	public static final Item MUTTON_CHEESEBURGER = basicFood(8, 0.6f);
	public static final Item FISH_CHEESEBURGER = basicFood(8, 0.6f);
	public static final Item RABBIT_CHEESEBURGER = basicFood(8, 0.5f);

	// The rest of the stews - Gives 1 item
	public static final StewItem STEAK_STEW = basicBowlFood(12, 0.6f);
	public static final StewItem CHICKEN_STEW = basicBowlFood(11, 0.6f);
	public static final StewItem PORK_STEW = basicBowlFood(12, 0.6f);
	public static final StewItem MUTTON_STEW = basicBowlFood(10, 0.6f);
	public static final StewItem FISH_STEW = basicBowlFood(10, 0.6f);
	// No need to add Rabbit Stew!

	// Jams - Gives 1 Item
	public static final Item SWEET_BERRY_JAM = basicBottleDrink(3, 0.1f, SoundEvents.ITEM_HONEY_BOTTLE_DRINK);
	public static final Item APPLE_JAM = basicBottleDrink(5, 0.3f, SoundEvents.ITEM_HONEY_BOTTLE_DRINK);
	public static final Item GLOW_BERRY_JAM = basicBottleDrink(3, 0.1f, SoundEvents.ITEM_HONEY_BOTTLE_DRINK);
	public static final Item GRAPE_JAM = basicBottleDrink(2, 0.1f, SoundEvents.ITEM_HONEY_BOTTLE_DRINK);
	public static final Item CHERRY_JAM = basicBottleDrink(3, 0.1f, SoundEvents.ITEM_HONEY_BOTTLE_DRINK);
	public static final Item TONKATSU_SAUCE = basicBowlFood(6, 0.6f);


	// Pies - Gives 1 Item
	public static final Item SHEPHERDS_PIE = basicFood(15, 0.7f);
	public static final Item SWEET_BERRY_PIE = basicFood(8, 0.3f);
	public static final Item APPLE_PIE = basicFood(9, 0.3f);
	public static final Item LEMON_MERINGUE_PIE = basicFood(9, 0.2f);
	public static final Item CHERRY_MERINGUE_PIE = basicFood(9, 0.3f);
	public static final Item PEACH_COBBLER = basicFood(9, 0.3f);

	// Cakes - Give 1 item
	public static final BasicCakeBlock CARROT_CAKE = new BasicCakeBlock(FabricBlockSettings.copy(Blocks.CAKE));
	public static final BasicCakeBlock CHOCOLATE_CAKE = new BasicCakeBlock(FabricBlockSettings.copy(Blocks.CAKE));
	public static final BasicCakeBlock RED_VELVET_CAKE = new BasicCakeBlock(FabricBlockSettings.copy(Blocks.CAKE));
	public static final BasicCakeBlock LEMON_CAKE = new BasicCakeBlock(FabricBlockSettings.copy(Blocks.CAKE));
	public static final BasicCakeBlock PEACH_CAKE = new BasicCakeBlock(FabricBlockSettings.copy(Blocks.CAKE));

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
	public static final Item CALAMARI = basicFood(2, 0.1f);
	public static final Item COOKED_CALAMARI = basicFood(foodComponent(6, 0.6f).meat());
	public static final Item BATTERED_CALAMARI = basicFood(3, 0.1f);
	public static final Item FRIED_CALAMARI = basicFood(8, 0.6f);
	public static final Item CHICKEN_NUGGETS = new Item( new Item.Settings().group(ItemGroup.FOOD).food(foodComponent(1, 0.1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*10), .3f).build()));
	public static final Item COOKED_CHICKEN_NUGGETS = basicFood(2, 0.4f);
	public static final Item BATTERED_CHICKEN_NUGGETS = basicFood(foodComponent(1, 0.1f)
			.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*10), .3f));
	public static final Item FRIED_CHICKEN_NUGGETS = basicFood(4, 0.4f);
	public static final Item BATTERED_CHICKEN = basicFood(foodComponent(3, 0.2f)
			.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*10), .3f));
	public static final Item FRIED_CHICKEN = basicFood(8, 0.6f);
	public static final Item BATTERED_PORKCHOP = basicFood(4, 0.2f);
	public static final Item FRIED_PORKCHOP = basicFood(10, 0.8f);
	public static final Item FRENCH_FRIES = basicFood(1, 0.1f);
	public static final Item COOKED_FRENCH_FRIES = basicFood(2, 0.6f);
	public static final Item EGG_WHITES = new BottledItem( new Item.Settings().group(ItemGroup.FOOD).food(foodComponent(0, 0f)
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20*10), .1f).build()), SoundEvents.ENTITY_WITCH_DRINK);
	public static final Item MERINGUE = basicBottleDrink(1, 0.2f, SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK);
	public static final Item EGG_YOLK = new BottledItem( new Item.Settings().group(ItemGroup.FOOD).food(foodComponent(0, 0f).snack()
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20*10), .1f).build()), SoundEvents.ENTITY_WITCH_DRINK);
	public static final Item MAYONNAISE = basicBottleDrink(1, 0.2f, SoundEvents.ENTITY_WITCH_DRINK);
	public static final Item PIZZA = basicFood(1, 0.4f);
	public static final Item COOKED_PIZZA = basicFood(5, 0.8f);
	public static final Item BACON = basicFood(3, 0.4f);
	public static final Item SMOKED_BACON = basicFood(10, 0.9f);
	public static final Item JERKY = basicFood(3, 0.4f);
	public static final Item SMOKED_JERKY = basicFood(10, 0.9f);
	public static final Item CUT_SALMON = basicFood(2, 0.2f);
	public static final Item SMOKED_SALMON = basicFood(6, 0.9f);
	public static final Item WILD_RICE_BALL = basicFood(1, 0.3f);
	public static final Item RICE_BALL = basicFood(1, 0.2f);
	public static final Item COOKED_WILD_RICE_BALL = basicFood(4, 0.5f);
	public static final Item COOKED_RICE_BALL = basicFood(4, 0.4f);
	public static final Item COOKED_CORN = basicFood(5, 0.6f);
	public static final Item POPCORN = basicFood(1, 0.3f);
	public static final Item ONION_RINGS = basicFood(1, 0.1f);
	public static final Item BATTERED_ONION_RINGS = basicFood(2, 0.1f);
	public static final Item FRIED_ONION_RINGS = basicFood(3, 0.3f);

	// Mill
	public static final Identifier MILL_ID = new Identifier(ID, "mill");
	public static final Block MILL = new MillBlock(FabricBlockSettings.copy(Blocks.STONECUTTER));
	public static final BlockItem MILL_ITEM = new BlockItem(MILL, new Item.Settings().group(ItemGroup.DECORATIONS));
	public static ScreenHandlerType<MillScreenHandler> MILL_SCREEN;

	// Ice Box
	public static final Identifier ICE_BOX_ID = new Identifier(ID, "ice_box");
	public static BlockEntityType<IceBoxBlockEntity> ICE_BOX_ENTITY_TYPE;
	public static final Block ICE_BOX = new IceBoxBlock(FabricBlockSettings.copy(Blocks.CHEST).sounds(BlockSoundGroup.METAL).luminance((state) -> state.get(IceBoxBlock.LIT) ? 15 : 0), () -> ICE_BOX_ENTITY_TYPE);
	public static final BlockItem ICE_BOX_ITEM = new BlockItem(ICE_BOX, new Item.Settings().group(ItemGroup.DECORATIONS));
	public static ScreenHandlerType<GenericContainerScreenHandler> ICE_BOX_9X3;
	public static ScreenHandlerType<GenericContainerScreenHandler> ICE_BOX_9X6;

	// Fermenter
	public static final Identifier FERMENTER_ID = new Identifier(ID, "fermenter");
	public static BlockEntityType<FermenterBlockEntity> FERMENTER_ENTITY_TYPE;
	public static final Block FERMENTER = new FermenterBlock(FabricBlockSettings.copy(Blocks.STONECUTTER));
	public static final BlockItem FERMENTER_ITEM = new BlockItem(FERMENTER, new Item.Settings().group(ItemGroup.DECORATIONS));


	// Cauldron
	public static final Block LIQUID_CHEESE_CAULDRON = new CheeseCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON), CauldronBehavior.createMap()); // do nothing
	public static final Block SOLID_CHEESE_CAULDRON = new CheeseCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON), BakingCauldronBehavior.SOLID_CHEESE_CAULDRON_BEHAVIOR);
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
	public static final Item BACON_BITS = basicFood(3, 0.9f);
	public static final Item MASHED_POTATOES = basicFood(6, 0.6f);
	public static final Item DOUGH = basicIngredient(); //  Henry - The inspiration behind the code, my rock and my brain - Redeemed
	public static final Item PASTA_DOUGH = basicIngredient();
	public static final Item LINGUINE = basicIngredient();
	public static final Item MACARONI = basicIngredient();
	public static final Item CHEESE = basicBucketDrink(2, 0.1f);
	public static final Item TOMATO_SAUCE = basicIngredient();
	public static final Item WHEAT_CEREAL = basicIngredient();
	public static final Item CORN_CEREAL = basicIngredient();
	public static final Item RICE_CEREAL = basicIngredient();
	public static final Item CINNAMON_BARK = basicIngredient();
	public static final Item CINNAMON_POWDER = basicIngredient();
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
	public static final Item WATER_CUP = basicCupDrink();
	public static final Item CHOCOLATE_MILK = basicCupDrink(2, 0.1f); // frick i would like some choccy milk rn - redeemed by rrricohu on 3/21/21
	public static final Item CREAMER_CUP = basicCupDrink();
	public static final Item COFFEE_CUP = basicSuperCupDrink(2, 0.3f, new StatusEffectInstance(StatusEffects.SPEED, 20*15, 1));
	public static final Item COFFEE_W_CREAMER = basicSuperCupDrink(2, 0.2f, new StatusEffectInstance(StatusEffects.SPEED, 20*30));
	public static final Item TEA_CUP = basicSuperCupDrink(2, 0.2f, new StatusEffectInstance(StatusEffects.SPEED, 20*10));
	public static final Item TEA_W_CREAMER = basicSuperCupDrink(2, 0.1f, new StatusEffectInstance(StatusEffects.SPEED, 20*20));
	public static final Item MOLASSES = basicCupDrink(SoundEvents.ENTITY_WITCH_DRINK);
	public static final Item PLAIN_MILKSHAKE = basicCupDrink(1, 0.1f, SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK);
	public static final Item CHOCOLATE_MILKSHAKE = basicCupDrink(2, 0.2f, SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK);
	public static final Item PLAIN_ICE_CREAM = basicCupDrink(1, 0.1f, SoundEvents.ENTITY_GENERIC_EAT);
	public static final Item CHOCOLATE_ICE_CREAM = basicCupDrink(2, 0.2f, SoundEvents.ENTITY_GENERIC_EAT);
	public static final Item SWEET_BERRY_SWIRL_CREAM = basicCupDrink(1, 0.1f);
	public static final Item GLOW_BERRY_SWIRL_CREAM = basicCupDrink(1, 0.1f);
	public static final Item GRAPE_SWIRL_CREAM = basicCupDrink(1, 0.1f);
	public static final Item APPLE_CINNAMON_CREAM = basicCupDrink(3, 0.3f);
	public static final Item SWEET_BERRY_SWIRL_ICE_CREAM = basicCupDrink(2, 0.2f, SoundEvents.ENTITY_GENERIC_EAT);
	public static final Item GLOW_BERRY_SWIRL_ICE_CREAM = basicCupDrink(2, 0.2f, SoundEvents.ENTITY_GENERIC_EAT);
	public static final Item GRAPE_SWIRL_ICE_CREAM = basicCupDrink(2, 0.2f, SoundEvents.ENTITY_GENERIC_EAT);
	public static final Item APPLE_CINNAMON_ICE_CREAM = basicCupDrink(4, 0.4f, SoundEvents.ENTITY_GENERIC_EAT);
	public static final Item SWEET_BERRY_SWIRL_MILKSHAKE = basicCupDrink(2, 0.2f, SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK);
	public static final Item GLOW_BERRY_SWIRL_MILKSHAKE = basicCupDrink(2, 0.2f, SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK);
	public static final Item GRAPE_SWIRL_MILKSHAKE = basicCupDrink(2, 0.2f, SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK);
	public static final Item APPLE_CINNAMON_MILKSHAKE = basicCupDrink(4, 0.4f, SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK);

	// Sodie Pop
	public static final Item SUGAR_WATER = basicCupDrink();
	public static final Item CLUB_SODA = basicCupDrink(1, 0.1f); // coal
	public static final Item ROOT_BEER = basicCupDrink(2, 0.2f); // beetUwUoot
	public static final Item COLA = basicCupDrink(2, 0.2f); // cocoa beans
	public static final Item FRUITY_SODA = basicCupDrink(2, 0.2f); // wild berries
	public static final Item MOUNTAIN_FOUNTAIN = basicCupDrink(2, 0.2f); // melon
	public static final Item CACTUS_CHILLER = basicCupDrink(2, 0.2f); // cactus
	public static final Item GRAPE_SODA = basicCupDrink(2, 0.2f); // grape

	// Slushees
	public static final Item ROOT_BEER_SLUSHEE = basicCupDrink(2, 0.3f );
	public static final Item COLA_SLUSHEE = basicCupDrink(2, 0.3f );
	public static final Item FRUITY_SODA_SLUSHEE = basicCupDrink(2, 0.3f );
	public static final Item MOUNTAIN_FOUNTAIN_SLUSHEE = basicCupDrink(2, 0.3f );
	public static final Item CACTUS_CHILLER_SLUSHEE = basicCupDrink(2, 0.3f );
	public static final Item GRAPE_SODA_SLUSHEE = basicCupDrink(2, 0.3f );

	// Plates & Meals
	public static final Item UNFIRED_PLATE = basicIngredient();
	public static final Item PLATE = basicIngredient();
	public static final Item STEAK_MEAL = new PlatedItem(19, 0.7f);
	public static final Item PORK_MEAL = new PlatedItem(25, 0.5f);
	public static final Item CHICKEN_MEAL = new PlatedItem(8, 0.5f);
	public static final Item MUTTON_MEAL = new PlatedItem(20, 0.6f);
	public static final Item RABBIT_MEAL = new PlatedItem(26, 0.7f);
	public static final Item SALMON_MEAL = new PlatedItem(18, 0.5f);

	// Crops - Here's to v0.3!
	public static final Block COFFEE = new CocoaBlock(FabricBlockSettings.copy(Blocks.COCOA));
	public static final BlockItem COFFEE_BEANS = new BlockItem(COFFEE, new Item.Settings().group(ItemGroup.MATERIALS));
	public static final Block TEA = new TeaTreeBlock(FabricBlockSettings.copy(Blocks.SWEET_BERRY_BUSH));
	public static final BlockItem TEA_SEEDS = new BlockItem(TEA, new Item.Settings().group(ItemGroup.MATERIALS));
	public static final Item TEA_LEAVES = basicIngredient();
	public static final Item DRIED_TEA_LEAVES = basicIngredient();
	public static final Block COMPRESSED_TEA_BLOCK = new Block(FabricBlockSettings.copy(Blocks.DRIED_KELP_BLOCK));
	public static final DamageSource TEA_TREE_DMGSRC = DamageSourceAccessor.newDamageSource("tea_tree");
	public static final Block TOMATO_PLANT = new BasicCropBlock(FabricBlockSettings.copy(Blocks.WHEAT));
	public static final Item TOMATO = new TomatoItem(TOMATO_PLANT, new Item.Settings().group(ItemGroup.FOOD).food(
			foodComponent(3, 0.6f).build()
	));
	public static final EntityType<TomatoEntity> TOMATO_THROWABLE = FabricEntityTypeBuilder.<TomatoEntity>create(SpawnGroup.MISC, (entity, world) -> new TomatoEntity(world))
			.dimensions(new EntityDimensions(0.25f, 0.25f, true))
			.trackedUpdateRate(10)
			.trackRangeChunks(4)
			.build();
	public static final Block RICE_PLANT = new BasicCropBlock(FabricBlockSettings.copy(Blocks.WHEAT));
	public static final Item WILD_RICE = new BlockItem(RICE_PLANT, new Item.Settings().group(ItemGroup.MISC));
	public static final Item RICE = basicIngredient();
	public static final Block CORN_STALK = new TallCropBlock(FabricBlockSettings.copy(Blocks.WHEAT));
	public static final Item CORN = basicFood(3, 0.3f);
	public static final Item CORN_SEEDS = new BlockItem(CORN_STALK, new Item.Settings().group(ItemGroup.MISC));
	public static final Block ONION_PLANT = new BasicCropBlock(FabricBlockSettings.copy(Blocks.WHEAT));
	public static final Item ONION = new BlockItem(ONION_PLANT, new Item.Settings().group(ItemGroup.FOOD).food(
			foodComponent(2, 0.1f).build()
	));
	// deathypooh - requested on 3/22/21
	// solcatowo - requested on 3/22/21

	// Vanilla Trellises
	public static final Block OAK_TRELLIS = new TrellisBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE).nonOpaque());
	public static final Block SPRUCE_TRELLIS = new TrellisBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE).nonOpaque());
	public static final Block BIRCH_TRELLIS = new TrellisBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE).nonOpaque());
	public static final Block JUNGLE_TRELLIS = new TrellisBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE).nonOpaque());
	public static final Block ACACIA_TRELLIS = new TrellisBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE).nonOpaque());
	public static final Block DARK_OAK_TRELLIS = new TrellisBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE).nonOpaque());
	public static final Block CRIMSON_TRELLIS = new TrellisBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE).nonOpaque());
	public static final Block WARPED_TRELLIS = new TrellisBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE).nonOpaque());

	// Wood Types & their fruits
	public static final Item LEMON = basicFood(1, 0.1f);
	public static final WoodType LEMON_WOOD_TYPE = new WoodType("lemon", LEMON);
	public static final Item CHERRIES = basicFood(2, 0.1f);
	public static final WoodType CHERRY_WOOD_TYPE = new WoodType("cherry", CHERRIES);
	public static final Item JUNIPER_BERRIES = basicFood(1, 0.1f);
	public static final WoodType JUNIPER_WOOD_TYPE = new WoodType("juniper", JUNIPER_BERRIES);
	public static final Item PEACH = basicFood(4, 0.3f);
	public static final WoodType PEACH_WOOD_TYPE = new WoodType("peach", PEACH);

	// Saplings
	public static final Block GRAPE_STEM = new GrapeStemBlock(FabricBlockSettings.copy(Blocks.VINE));
	public static final Block GRAPE_VINE = new GrapeVineBlock(FabricBlockSettings.copy(Blocks.VINE));
	public static final Item GRAPES = basicFood(1, 0.1f);

	public static final Block SMALL_CINNAMON_TREE = new TallPlantBlock(FabricBlockSettings.copy(Blocks.TALL_GRASS));
	public static final Block CINNAMON_TREE = new TriplePlantBlock(FabricBlockSettings.copy(Blocks.TALL_GRASS));
	public static final Block CINNAMON_SAPLING = new WildCardSaplingBlock(FabricBlockSettings.copy(Blocks.OAK_SAPLING), (random) -> (PlantBlock) (((Random) random).nextFloat() < 0.4 ? CINNAMON_TREE : SMALL_CINNAMON_TREE) );
	public static final Block POTTED_CINNAMON_SAPLING = new FlowerPotBlock(CINNAMON_SAPLING, FabricBlockSettings.copy(Blocks.FLOWER_POT));

	public static final Block CHERRY_SAPLING = new BasicSaplingBlock(new BakingSaplingGenerator(() -> BakingTreeConfiguredFeatures.CHERRY_TREE), FabricBlockSettings.copy(Blocks.OAK_SAPLING));
	public static final Block POTTED_CHERRY_SAPLING = new FlowerPotBlock(CHERRY_SAPLING, FabricBlockSettings.copy(Blocks.FLOWER_POT));
	public static final Block LEMON_SAPLING = new BasicSaplingBlock(new BakingSaplingGenerator(() -> BakingTreeConfiguredFeatures.LEMON_TREE), FabricBlockSettings.copy(Blocks.OAK_SAPLING));
	public static final Block POTTED_LEMON_SAPLING = new FlowerPotBlock(LEMON_SAPLING, FabricBlockSettings.copy(Blocks.FLOWER_POT));
	public static final Block JUNIPER_SAPLING = new BasicSaplingBlock(new BakingSaplingGenerator(() -> BakingTreeConfiguredFeatures.JUNIPER_TREE), FabricBlockSettings.copy(Blocks.OAK_SAPLING));
	public static final Block POTTED_JUNIPER_SAPLING = new FlowerPotBlock(JUNIPER_SAPLING, FabricBlockSettings.copy(Blocks.FLOWER_POT));
	public static final Block PEACH_SAPLING = new BasicSaplingBlock(new BakingSaplingGenerator(() -> BakingTreeConfiguredFeatures.PEACH_TREE), FabricBlockSettings.copy(Blocks.OAK_SAPLING));
	public static final Block POTTED_PEACH_SAPLING = new FlowerPotBlock(PEACH_SAPLING, FabricBlockSettings.copy(Blocks.FLOWER_POT));

	// Misc
	public static final Item BAGEL = basicFood(8, 0.6f);
	public static final Item DONUT = basicFood(8, 0.7f);
	public static final Item BROWNIE = basicFood(4, 0.5f);
	public static final Item SUGAR_COOKIE = basicFood(2, 0.1f);
	public static final Item SNICKERDOODLE_COOKIE = basicFood(2, 0.1f);
	public static final Item PEPPERONI = basicFood(2, 0.4f);
	public static final Item COOKED_EGG = basicFood(2, 0.4f);
	public static final Item MACARONI_N_CHEESE = basicBowlFood(3, 0.4f);
	public static final Item BACON_MACARONI_N_CHEESE = basicBowlFood(5, 0.7f);
	public static final Item TOMATO_SOUP = basicBowlFood(12, 0.6f);
	public static final Item SALMON_SUSHI = basicFood(4, 0.2f);
	public static final Item SQUID_SUSHI = basicFood(4, 0.2f);
	public static final Item LOADED_FRIES = basicFood(6, 0.7f);
	public static final Item LOADED_POTATO = basicFood(16, 0.8f);
	public static final StewItem VEGGIE_MEDLEY = basicBowlFood(11, 0.5f);
	public static final StewItem FRUIT_SALAD = basicBowlFood(13, 0.2f);
	public static final MidasSaladItem MIDAS_SALAD = new MidasSaladItem(
			new Item.Settings()
					.group(ItemGroup.FOOD)
					.rarity(Rarity.EPIC)
					.maxCount(16)
					.food(foodComponent(20, 1.2f)
							.alwaysEdible()
							.statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 20*120), 1f)
							.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20*20, 1), 1f)
							.statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20*60), 1f)
							.build()
					)
	);
	public static final Item DISGUSTING_DISH = new StewItem(
			new Item.Settings()
					.group(ItemGroup.FOOD)
					.maxCount(16)
					.food(foodComponent(4, 0.3f)
							.alwaysEdible()
							.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*30, 2), 1f)
							.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20*30), 1f)
							.statusEffect(new StatusEffectInstance(StatusEffects.POISON, 20*90, 3), 1f)
							.statusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20*30), 1f)
							.build()
					)
	);
	public static final StewItem WHEAT_CEREAL_BOWL = basicBowlFood(3, 0.3f);
	public static final StewItem CORN_CEREAL_BOWL = basicBowlFood(4, 0.3f);
	public static final StewItem RICE_CEREAL_BOWL = basicBowlFood(3, 0.3f);
	public static final Block CHEESE_BLOCK = new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT).sounds(BlockSoundGroup.CANDLE));
	public static final Block CHEESE_LAYER = new BasicLayerBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT).sounds(BlockSoundGroup.CANDLE));
	public static final Item CHEESE_SLICE = new BlockItem(CHEESE_LAYER, new FabricItemSettings().group(ItemGroup.FOOD).food(foodComponent(1, 0.0f).build()));
	public static final BottledItem LEMONADE = basicBottleDrink(2, 0.2f);
	public static final BottledItem FROZEN_LEMONADE = basicBottleDrink(2, 0.3f);
	public static final BottledItem SPICED_RUM = basicBottleDrink(1, 0.2f);
	public static final BottledItem FRUIT_MARTINI = basicBottleDrink(9, 0.2f);
	public static final Item CARAMEL = basicFood(1, 0.2f);
	public static final Item CARAMEL_APPLE = basicFood(5, 0.3f);
	public static final Block STEAK_AND_ALE_PUDDING = new EdibleCubeBlock(FabricBlockSettings.of(Material.CAKE).sounds(BlockSoundGroup.WOOL));
	public static final Item GOLDEN_BAGEL = basicFood(new FoodComponent.Builder()
			.hunger(8)
			.saturationModifier(1.2f)
			.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60, 1), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 60, 0), 1.0F)
			.alwaysEdible()
	);

	// Fermented Items
	public static final BottledItem MALT_VINEGAR = basicBottleDrink(0, 0f);
	public static final BottledItem RED_WINE = basicBottleDrink(1, 0.1f);
	public static final BottledItem WHISKEY = basicBottleDrink(3, 0.1f);
	public static final BottledItem BEER = basicBottleDrink(1, 0.2f);
	public static final BottledItem MEAD = basicBottleDrink(2, 0.1f);
	public static final BottledItem RUM = basicBottleDrink(1, 0.1f);
	public static final BottledItem GIN = basicBottleDrink(1, 0.1f);
	public static final BottledItem VODKA = basicBottleDrink(2, 0.3f);
	public static final BottledItem SAKE = basicBottleDrink(1, 0.1f);
	public static final BottledItem AMBROSIA = new BottledItem(
			new FabricItemSettings()
					.group(ItemGroup.FOOD)
					.rarity(Rarity.EPIC)
					.maxCount(1)
					.food(
							new FoodComponent.Builder()
									.hunger(6)
									.saturationModifier(1.2f)
									.alwaysEdible()
									.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1), 1.0F)
									.statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0), 1.0F)
									.statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000, 0), 1.0F)
									.statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3), 1.0F)
									.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200, 1), 1.0F)
									.statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 1200, 1), 1.0F)
									.statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 1200, 1), 1.0F)
									.build()
					)
	);

	// Stats
	private static final String day_stat = "day_of_week";
	public static final Identifier DAY_OF_WEEK = new Identifier(ID, day_stat);
	private static final String tomato_stat = "tomato_kills";
	public static final Identifier TOMATO_KILLS = new Identifier(ID, tomato_stat);
	private static final String mill_stat = "interact_with_mill";
	public static final Identifier INTERACT_WITH_MILL = new Identifier(ID, mill_stat);
	private static final String fermenter_stat = "interact_with_fermenter";
	public static final Identifier INTERACT_WITH_FERMENTER = new Identifier(ID, fermenter_stat);
	private static final String ice_box_stat = "open_ice_box";
	public static final Identifier OPEN_ICE_BOX = new Identifier(ID, ice_box_stat);

	// Sounds (How did I get here?)
	public static final Identifier ICE_BOX_OPEN_ID = new Identifier(ID, "ice_box_open");
	public static final SoundEvent ICE_BOX_OPEN = new SoundEvent(ICE_BOX_OPEN_ID);
	public static final Identifier ICE_BOX_CLOSE_ID = new Identifier(ID, "ice_box_close");
	public static final SoundEvent ICE_BOX_CLOSE = new SoundEvent(ICE_BOX_CLOSE_ID);
	public static final Identifier COLL_ID = new Identifier(ID, "music_coll");
	public static final SoundEvent COLL = new SoundEvent(COLL_ID);
	public static final Identifier CAFE_ID = new Identifier(ID, "music_cafe");
	public static final SoundEvent CAFE = new SoundEvent(CAFE_ID);
	public static final Identifier GOLDEN_BAGEL_ADVANCEMENT_ID = new Identifier(ID, "golden_bagel_advancement");
	public static final SoundEvent GOLDEN_BAGEL_ADVANCEMENT = new SoundEvent(GOLDEN_BAGEL_ADVANCEMENT_ID);

	// Music discs
	public static final Item COLL_DISC = new BasicMusicDiscItem(4, COLL, new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
	public static final Item CAFE_DISC = new BasicMusicDiscItem(5, CAFE, new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));

	@Override
	public void onInitialize() {

		// Sandwiches
		registerItem("steak_sandwich", STEAK_SANDWICH);
		registerItem("chicken_sandwich", CHICKEN_SANDWICH);
		registerItem("pork_sandwich", PORK_SANDWICH);
		registerItem("mutton_sandwich", MUTTON_SANDWICH);
		registerItem("fish_sandwich", FISH_SANDWICH);
		registerItem("rabbit_sandwich", RABBIT_SANDWICH);
		registerItem("sweet_berry_jam_sandwich", SWEET_BERRY_JAM_SANDWICH);
		registerItem("apple_jam_sandwich", APPLE_JAM_SANDWICH);
		registerItem("glow_berry_jam_sandwich", GLOW_BERRY_JAM_SANDWICH);
		registerItem("grape_jam_sandwich", GRAPE_JAM_SANDWICH);
		registerItem("cherry_jam_sandwich", CHERRY_JAM_SANDWICH);
		registerItem("cheese_sandwich", CHEESE_SANDWICH);
		registerItem("grilled_cheese_sandwich", GRILLED_CHEESE_SANDWICH);
		registerItem("katsu_sandwich", KATSU_SANDWICH);

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
		registerCakeType("carrot_cake", CARROT_CAKE);
		registerCakeType("chocolate_cake", CHOCOLATE_CAKE);
		registerCakeType("red_velvet_cake", RED_VELVET_CAKE);
		registerCakeType("lemon_cake", LEMON_CAKE);
		registerCakeType("peach_cake", PEACH_CAKE);

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
		registerItem("sweet_berry_jam", SWEET_BERRY_JAM);
		registerItem("apple_jam", APPLE_JAM);
		registerItem("glow_berry_jam", GLOW_BERRY_JAM);
		registerItem("grape_jam", GRAPE_JAM);
		registerItem("cherry_jam", CHERRY_JAM);
		registerItem("tonkatsu_sauce", TONKATSU_SAUCE);

		// Pies
		registerItem("shepherds_pie", SHEPHERDS_PIE);
		registerItem("sweet_berry_pie", SWEET_BERRY_PIE);
		registerItem("apple_pie", APPLE_PIE);
		registerItem("lemon_meringue_pie", LEMON_MERINGUE_PIE);
		registerItem("cherry_meringue_pie", CHERRY_MERINGUE_PIE);
		registerItem("peach_cobbler", PEACH_COBBLER);

		// Ingredients
		registerItem("flour", FLOUR);
		registerItem("corn_meal", CORN_MEAL);
		registerItem("ground_coffee", GROUND_COFFEE);
		registerItem("ground_tea", GROUND_TEA);
		registerItem("cocoa_powder", COCOA_POWDER);
		registerItem("batter", BATTER);
		registerItem("bacon_bits", BACON_BITS);
		registerItem("dough", DOUGH);
		registerItem("pasta_dough", PASTA_DOUGH);
		registerItem("linguine", LINGUINE);
		registerItem("macaroni", MACARONI);
		registerItem("cheese", CHEESE);
		registerItem("tomato_sauce", TOMATO_SAUCE);
		registerItem("wheat_cereal", WHEAT_CEREAL);
		registerItem("corn_cereal", CORN_CEREAL);
		registerItem("rice_cereal", RICE_CEREAL);
		registerItem("cinnamon_bark", CINNAMON_BARK);
		registerItem("cinnamon_powder", CINNAMON_POWDER);

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
		registerItem("battered_porkchop", BATTERED_PORKCHOP);
		registerItem("fried_porkchop", FRIED_PORKCHOP);
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
		registerItem("cooked_corn", COOKED_CORN);
		registerItem("popcorn", POPCORN);
		registerItem("onion_rings", ONION_RINGS);
		registerItem("battered_onion_rings", BATTERED_ONION_RINGS);
		registerItem("fried_onion_rings", FRIED_ONION_RINGS);

		// Mill
		Registry.register(Registry.BLOCK, MILL_ID, MILL);
		Registry.register(Registry.ITEM, MILL_ID, MILL_ITEM);
		MILL_SCREEN = ScreenHandlerRegistry.registerSimple(Baking.MILL_ID, MillScreenHandler::new);

		// Ice Box
		Registry.register(Registry.BLOCK, ICE_BOX_ID, ICE_BOX);
		Registry.register(Registry.ITEM, ICE_BOX_ID, ICE_BOX_ITEM);
		ICE_BOX_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, ICE_BOX_ID, FabricBlockEntityTypeBuilder.create(IceBoxBlockEntity::new, ICE_BOX).build());
		ICE_BOX_9X3 = ScreenHandlerRegistry.registerSimple(new Identifier(ID, "ice_box_9x3"), GenericContainerScreenHandler::createGeneric9x3);
		ICE_BOX_9X6 = ScreenHandlerRegistry.registerSimple(new Identifier(ID, "ice_box_9x6"), GenericContainerScreenHandler::createGeneric9x6);

		// Fermenter
		Registry.register(Registry.BLOCK, FERMENTER_ID, FERMENTER);
		Registry.register(Registry.ITEM, FERMENTER_ID, FERMENTER_ITEM);
		FERMENTER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, FERMENTER_ID, FabricBlockEntityTypeBuilder.create(FermenterBlockEntity::new, FERMENTER).build());

		// Recipe Serializers
		Registry.register(Registry.RECIPE_SERIALIZER, MillingRecipe.ID, MillingRecipe.SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, FermentingRecipe.ID, FermentingRecipe.SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, FreezingRecipe.ID, FreezingRecipe.SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, ShapelessRemainderlessRecipe.ID, ShapelessRemainderlessRecipe.SERIALIZER);

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
		registerItem("molasses", MOLASSES);
		registerItem("sweet_berry_swirl_cream", SWEET_BERRY_SWIRL_CREAM);
		registerItem("glow_berry_swirl_cream", GLOW_BERRY_SWIRL_CREAM);
		registerItem("grape_swirl_cream", GRAPE_SWIRL_CREAM);
		registerItem("apple_cinnamon_cream", APPLE_CINNAMON_CREAM);
		registerItem("plain_ice_cream", PLAIN_ICE_CREAM);
		registerItem("chocolate_ice_cream", CHOCOLATE_ICE_CREAM);
		registerItem("sweet_berry_swirl_ice_cream", SWEET_BERRY_SWIRL_ICE_CREAM);
		registerItem("glow_berry_swirl_ice_cream", GLOW_BERRY_SWIRL_ICE_CREAM);
		registerItem("grape_swirl_ice_cream", GRAPE_SWIRL_ICE_CREAM);
		registerItem("apple_cinnamon_ice_cream", APPLE_CINNAMON_ICE_CREAM);
		registerItem("plain_milkshake", PLAIN_MILKSHAKE);
		registerItem("chocolate_milkshake", CHOCOLATE_MILKSHAKE);
		registerItem("sweet_berry_swirl_milkshake", SWEET_BERRY_SWIRL_MILKSHAKE);
		registerItem("glow_berry_swirl_milkshake", GLOW_BERRY_SWIRL_MILKSHAKE);
		registerItem("grape_swirl_milkshake", GRAPE_SWIRL_MILKSHAKE);
		registerItem("apple_cinnamon_milkshake", APPLE_CINNAMON_MILKSHAKE);

		// Sodie Pop
		registerItem("sugar_water", SUGAR_WATER);
		registerItem("club_soda", CLUB_SODA);
		registerItem("root_beer", ROOT_BEER);
		registerItem("cola", COLA);
		registerItem("fruity_soda", FRUITY_SODA);
		registerItem("mountain_fountain", MOUNTAIN_FOUNTAIN);
		registerItem("cactus_chiller", CACTUS_CHILLER);
		registerItem("grape_soda", GRAPE_SODA);

		// Slushee
		registerItem("root_beer_slushee", ROOT_BEER_SLUSHEE);
		registerItem("cola_slushee", COLA_SLUSHEE);
		registerItem("fruity_soda_slushee", FRUITY_SODA_SLUSHEE);
		registerItem("mountain_fountain_slushee", MOUNTAIN_FOUNTAIN_SLUSHEE);
		registerItem("cactus_chiller_slushee", CACTUS_CHILLER_SLUSHEE);
		registerItem("grape_soda_slushee", GRAPE_SODA_SLUSHEE);

		// Plates & Meals
		registerItem("unfired_plate", UNFIRED_PLATE);
		registerItem("plate", PLATE);
		registerItem("steak_meal", STEAK_MEAL);
		registerItem("pork_meal", PORK_MEAL);
		registerItem("chicken_meal", CHICKEN_MEAL);
		registerItem("mutton_meal", MUTTON_MEAL);
		registerItem("rabbit_meal", RABBIT_MEAL);
		registerItem("salmon_meal", SALMON_MEAL);

		// Crops
		registerBlock("coffee", COFFEE);
		registerItem("coffee_beans", COFFEE_BEANS);
		registerBlock("tea", TEA, TEA_SEEDS);
		registerItem("tea_leaves", TEA_LEAVES);
		registerItem("dried_tea_leaves", DRIED_TEA_LEAVES);
		registerBlock("compressed_tea_block", COMPRESSED_TEA_BLOCK, ItemGroup.BUILDING_BLOCKS);
		Registry.register(Registry.ENTITY_TYPE, new Identifier(ID, "tomato"), TOMATO_THROWABLE);
		registerBlock("tomato", TOMATO_PLANT, (BlockItem) TOMATO);
		((BasicCropBlock)TOMATO_PLANT).setSeed(TOMATO);
		registerBlock("wild_rice", RICE_PLANT, (BlockItem) WILD_RICE);
		((BasicCropBlock)RICE_PLANT).setSeed(WILD_RICE);
		registerItem("rice", RICE);
		registerBlock("corn", CORN_STALK);
		((BasicCropBlock)CORN_STALK).setSeed(CORN_SEEDS);
		registerItem("corn", CORN);
		registerItem("corn_seeds", CORN_SEEDS);
		registerBlock("onion", ONION_PLANT, (BlockItem) ONION);
		((BasicCropBlock)ONION_PLANT).setSeed(ONION);
		// dreamwastaken my beloved <3 - redeemed by KoritsiAlogo on 3/22/21

		// Trees
		registerBlock("cherry_sapling", CHERRY_SAPLING, ItemGroup.DECORATIONS);
		registerBlock("potted_cherry_sapling", POTTED_CHERRY_SAPLING);
		CHERRY_WOOD_TYPE.init();
		registerItem("cherries", CHERRIES);
		registerBlock("lemon_sapling", LEMON_SAPLING, ItemGroup.DECORATIONS);
		registerBlock("potted_lemon_sapling", POTTED_LEMON_SAPLING);
		LEMON_WOOD_TYPE.init();
		registerItem("lemon", LEMON);
		registerBlock("grape_stem", GRAPE_STEM, ItemGroup.DECORATIONS);
		registerBlock("grape_vine", GRAPE_VINE, ItemGroup.DECORATIONS);
		registerItem("grapes", GRAPES);
		registerBlock("small_cinnamon_tree", SMALL_CINNAMON_TREE, ItemGroup.DECORATIONS);
		registerBlock("cinnamon_tree", CINNAMON_TREE, ItemGroup.DECORATIONS);
		registerBlock("cinnamon_sapling", CINNAMON_SAPLING, ItemGroup.DECORATIONS);
		registerBlock("potted_cinnamon_sapling", POTTED_CINNAMON_SAPLING);
		JUNIPER_WOOD_TYPE.init();
		registerBlock("juniper_sapling", JUNIPER_SAPLING, ItemGroup.DECORATIONS);
		registerBlock("potted_juniper_sapling", POTTED_JUNIPER_SAPLING);
		registerItem("juniper_berries", JUNIPER_BERRIES);
		PEACH_WOOD_TYPE.init();
		registerBlock("peach_sapling", PEACH_SAPLING, ItemGroup.DECORATIONS);
		registerBlock("potted_peach_sapling", POTTED_PEACH_SAPLING);
		registerItem("peach", PEACH);

		// Vanilla Trellises
		registerBlock("oak_trellis", OAK_TRELLIS, ItemGroup.DECORATIONS);
		registerBlock("spruce_trellis", SPRUCE_TRELLIS, ItemGroup.DECORATIONS);
		registerBlock("birch_trellis", BIRCH_TRELLIS, ItemGroup.DECORATIONS);
		registerBlock("jungle_trellis", JUNGLE_TRELLIS, ItemGroup.DECORATIONS);
		registerBlock("acacia_trellis", ACACIA_TRELLIS, ItemGroup.DECORATIONS);
		registerBlock("dark_oak_trellis", DARK_OAK_TRELLIS, ItemGroup.DECORATIONS);
		registerBlock("crimson_trellis", CRIMSON_TRELLIS, ItemGroup.DECORATIONS);
		registerBlock("warped_trellis", WARPED_TRELLIS, ItemGroup.DECORATIONS);

		// Misc
		registerItem("bagel", BAGEL);
		registerItem("donut", DONUT);
		registerItem("brownie", BROWNIE);
		registerItem("sugar_cookie", SUGAR_COOKIE);
		registerItem("snickerdoodle_cookie", SNICKERDOODLE_COOKIE);
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
		registerItem("lemonade", LEMONADE);
		registerItem("frozen_lemonade", FROZEN_LEMONADE);
		registerBlock("cheese_slice", CHEESE_LAYER, (BlockItem) CHEESE_SLICE);
		registerBlock("cheese_block", CHEESE_BLOCK, ItemGroup.BUILDING_BLOCKS);
		registerItem("spiced_rum", SPICED_RUM);
		registerItem("fruit_martini", FRUIT_MARTINI);
		registerItem("caramel", CARAMEL);
		registerItem("caramel_apple", CARAMEL_APPLE);
		registerBlock("steak_and_ale_pudding", STEAK_AND_ALE_PUDDING, ItemGroup.FOOD);
		registerItem("golden_bagel", GOLDEN_BAGEL);

		// Fermented Items
		registerItem("malt_vinegar", MALT_VINEGAR);
		registerItem("red_wine", RED_WINE);
		registerItem("whiskey", WHISKEY);
		registerItem("beer", BEER);
		registerItem("mead", MEAD);
		registerItem("rum", RUM);
		registerItem("gin", GIN);
		registerItem("vodka", VODKA);
		registerItem("sake", SAKE);
		registerItem("ambrosia", AMBROSIA);

		// Cauldrons
		BakingCauldronBehavior.registerBehaviors();
		registerBlock("liquid_cheese_cauldron", LIQUID_CHEESE_CAULDRON);
		registerBlock("solid_cheese_cauldron", SOLID_CHEESE_CAULDRON);
		registerBlock("coffee_cauldron", COFFEE_CAULDRON);
		registerBlock("tea_cauldron", TEA_CAULDRON);
		registerBlock("creamy_coffee_cauldron", CREAMY_COFFEE_CAULDRON);
		registerBlock("creamy_tea_cauldron", CREAMY_TEA_CAULDRON);
		registerBlock("separator_cauldron", SEPARATOR_CAULDRON);

		// Compostable Items
		BakingCompostableItems.init();

		// Trades
		BakingVillagerTrades.init();

		// Tree Configured/Placed Features
		BakingTreeConfiguredFeatures.init();
		BakingTreePlacedFeatures.init();
		BakingPlacers.init();

		// Advanced Configured/Placed Features
		BakingClusterConfiguredFeatures.init();
		BakingClusterPlacedFeatures.init();

		// Biomes
		BakingBiomes.init();

		// Villager Professions
		BakingVillagerProfessions.init();

		// Stats
		Registry.register(Registry.CUSTOM_STAT, day_stat, DAY_OF_WEEK); // Day of week - for Taco Tuesday advancement
		Stats.CUSTOM.getOrCreateStat(DAY_OF_WEEK, StatFormatter.DEFAULT);
		Registry.register(Registry.CUSTOM_STAT, tomato_stat, TOMATO_KILLS); // User kills with tomatoes - for Tomato Town advancement
		Stats.CUSTOM.getOrCreateStat(TOMATO_KILLS, StatFormatter.DEFAULT);
		Registry.register(Registry.CUSTOM_STAT, mill_stat, INTERACT_WITH_MILL); // Right clicks on mill
		Stats.CUSTOM.getOrCreateStat(INTERACT_WITH_MILL, StatFormatter.DEFAULT);
		Registry.register(Registry.CUSTOM_STAT, fermenter_stat, INTERACT_WITH_FERMENTER); // Right clicks on fermenter
		Stats.CUSTOM.getOrCreateStat(INTERACT_WITH_FERMENTER, StatFormatter.DEFAULT);
		Registry.register(Registry.CUSTOM_STAT, ice_box_stat, OPEN_ICE_BOX); // Right clicks on ice box
		Stats.CUSTOM.getOrCreateStat(OPEN_ICE_BOX, StatFormatter.DEFAULT);

		// Sounds
		Registry.register(Registry.SOUND_EVENT, ICE_BOX_OPEN_ID, ICE_BOX_OPEN);
		Registry.register(Registry.SOUND_EVENT, ICE_BOX_CLOSE_ID, ICE_BOX_CLOSE);
		Registry.register(Registry.SOUND_EVENT, COLL_ID, COLL);
		Registry.register(Registry.SOUND_EVENT, CAFE_ID, CAFE);
		Registry.register(Registry.SOUND_EVENT, GOLDEN_BAGEL_ADVANCEMENT_ID, GOLDEN_BAGEL_ADVANCEMENT);

		// Music Discs
		registerItem("music_disc_coll", COLL_DISC);
		registerItem("music_disc_cafe", CAFE_DISC);
	}

	@Override
	public void onTerraBlenderInitialized() {
		Regions.register(new BakingRegion(new Identifier(Baking.ID, "overworld"), 2));
	}
}
