package dev.elexi.hugeblank.bagels_baking.world.biome;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.world.BakingConfiguredFeatures;
import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class BakingBiomes {

    public static final Biome CHERRY_ORCHARD = register("cherry_orchard", createCherryOrchard(), OverworldClimate.TEMPERATE);
    public static final Biome LEMON_GROVE = register("lemon_grove", createLemonGrove(), OverworldClimate.TEMPERATE);

    private static int getSkyColor(float temperature) {
        float f = temperature / 3.0F;
        f = MathHelper.clamp(f, -1.0F, 1.0F);
        return MathHelper.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
    }

    public static void createDefaultFeatures(GenerationSettings.Builder builder) {
        builder.structureFeature(ConfiguredStructureFeatures.VILLAGE_PLAINS).structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);

        DefaultBiomeFeatures.addDefaultUndergroundStructures(builder);
        builder.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
        DefaultBiomeFeatures.addLandCarvers(builder);
        DefaultBiomeFeatures.addDefaultLakes(builder);
        DefaultBiomeFeatures.addAmethystGeodes(builder);
        DefaultBiomeFeatures.addDungeons(builder);
        DefaultBiomeFeatures.addPlainsTallGrass(builder);

        DefaultBiomeFeatures.addMineables(builder);
        DefaultBiomeFeatures.addDefaultOres(builder);
        DefaultBiomeFeatures.addDefaultDisks(builder);
        DefaultBiomeFeatures.addPlainsFeatures(builder);

        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);

        DefaultBiomeFeatures.addSprings(builder);
        DefaultBiomeFeatures.addFrozenTopLayer(builder);
    }

    public static void createDefaultSpawns(SpawnSettings.Builder builder) {

        DefaultBiomeFeatures.addFarmAnimals(builder);
        builder.playerSpawnFriendly();
    }

    public static Biome createCherryOrchard() {
        SpawnSettings.Builder builder = new SpawnSettings.Builder();
        createDefaultSpawns(builder);

        GenerationSettings.Builder builder2 = (new GenerationSettings.Builder()).surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        createDefaultFeatures(builder2);
        builder2.feature(GenerationStep.Feature.VEGETAL_DECORATION, BakingConfiguredFeatures.CHERRY_TREES);


            return (new Biome.Builder())
                    .precipitation(Biome.Precipitation.RAIN)
                    .category(Biome.Category.FOREST)
                    .depth(0.125F)
                    .scale(0.05F)
                    .temperature(0.8F)
                    .downfall(0.4F)
                    .effects((new net.minecraft.world.biome.BiomeEffects.Builder())
                            .waterColor(4159204)
                            .waterFogColor(329011)
                            .fogColor(12638463)
                            .skyColor(getSkyColor(0.8F))
                            .grassColor(0xfeb5c3)
                            .moodSound(BiomeMoodSound.CAVE)
                            .build())
                    .spawnSettings(builder.build())
                    .generationSettings(builder2.build())
                    .build();
    }

    public static Biome createLemonGrove() {
        SpawnSettings.Builder builder = new SpawnSettings.Builder();
        createDefaultSpawns(builder);

        GenerationSettings.Builder builder2 = (new GenerationSettings.Builder()).surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        createDefaultFeatures(builder2);
        builder2.feature(GenerationStep.Feature.VEGETAL_DECORATION, BakingConfiguredFeatures.LEMON_TREES);


        return (new Biome.Builder())
                .precipitation(Biome.Precipitation.RAIN)
                .category(Biome.Category.FOREST)
                .depth(0.125F)
                .scale(0.05F)
                .temperature(0.8F)
                .downfall(0.4F)
                .effects((new net.minecraft.world.biome.BiomeEffects.Builder())
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12638463)
                        .skyColor(getSkyColor(0.8F))
                        .grassColor(0x92c969)
                        .moodSound(BiomeMoodSound.CAVE)
                        .build())
                .spawnSettings(builder.build())
                .generationSettings(builder2.build())
                .build();
    }

    public static void init() {

    }

    public static Biome register(String name, Biome biome, OverworldClimate climate) {
        RegistryKey<Biome> KEY = RegistryKey.of(Registry.BIOME_KEY, new Identifier(Baking.ID, name));
        Biome out = Registry.register(BuiltinRegistries.BIOME, KEY.getValue(), biome);
        OverworldBiomes.addContinentalBiome(KEY, climate, 1d);
        return out;
    }
}
