package dev.elexi.hugeblank.bagels_baking.world.biome;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.world.gen.BakingPlacedFeatures;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
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
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public class BakingBiomes {

    public static final Biome CHERRY_ORCHARD = register("cherry_orchard", createCherryOrchard());
    public static final Biome LEMON_GROVE = register("lemon_grove", createLemonGrove());
    public static final Biome JUNIPER_TAIGA = register("juniper_taiga", createJuniperTaiga(false));
    public static final Biome SNOWY_JUNIPER_TAIGA = register("snowy_juniper_taiga", createJuniperTaiga(true));


    private static int getSkyColor(float temperature) {
        float f = temperature / 3.0F;
        f = MathHelper.clamp(f, -1.0F, 1.0F);
        return MathHelper.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
    }

    public static void addCustomForestFeatures(GenerationSettings.Builder builder) {
        DefaultBiomeFeatures.addLandCarvers(builder);
        DefaultBiomeFeatures.addAmethystGeodes(builder);
        DefaultBiomeFeatures.addDungeons(builder);
        DefaultBiomeFeatures.addMineables(builder);
        DefaultBiomeFeatures.addSprings(builder);
        DefaultBiomeFeatures.addFrozenTopLayer(builder);
        
        DefaultBiomeFeatures.addForestFlowers(builder);

        DefaultBiomeFeatures.addDefaultOres(builder);
        DefaultBiomeFeatures.addDefaultDisks(builder);

        builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BakingPlacedFeatures.TREES_BIRCH_AND_OAK_SPARSE);
        DefaultBiomeFeatures.addForestGrass(builder);

        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }

    public static Biome createJuniperTaiga(boolean snowy) {
        SpawnSettings.Builder builder = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(builder);
        builder
                .spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.WOLF, 8, 4, 4))
                .spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.RABBIT, 4, 2, 3))
                .spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.FOX, 8, 2, 4));


        DefaultBiomeFeatures.addBatsAndMonsters(builder);
        float f = snowy ? -0.5F : 0.25F;
        GenerationSettings.Builder builder2 = (new GenerationSettings.Builder());

        builder2.feature(
                GenerationStep.Feature.VEGETAL_DECORATION,
                snowy ? BakingPlacedFeatures.TREES_GROVE : BakingPlacedFeatures.TREES_TAIGA
        );
        DefaultBiomeFeatures.addLandCarvers(builder2);
        DefaultBiomeFeatures.addAmethystGeodes(builder2);
        DefaultBiomeFeatures.addDungeons(builder2);
        DefaultBiomeFeatures.addLargeFerns(builder2);
        DefaultBiomeFeatures.addMineables(builder2);
        DefaultBiomeFeatures.addDefaultOres(builder2);
        DefaultBiomeFeatures.addDefaultDisks(builder2);
        //DefaultBiomeFeatures.addTaigaTrees(builder2);
        DefaultBiomeFeatures.addDefaultFlowers(builder2);
        DefaultBiomeFeatures.addTaigaGrass(builder2);
        DefaultBiomeFeatures.addDefaultMushrooms(builder2);
        DefaultBiomeFeatures.addDefaultVegetation(builder2);
        DefaultBiomeFeatures.addSprings(builder2);
        if (snowy) {
            DefaultBiomeFeatures.addSweetBerryBushesSnowy(builder2);
        } else {
            DefaultBiomeFeatures.addSweetBerryBushes(builder2);
        }

        DefaultBiomeFeatures.addFrozenTopLayer(builder2);
        return (new Biome.Builder())
                .precipitation(snowy ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN)
                .category(Biome.Category.TAIGA)
                .temperature(f)
                .downfall(snowy ? 0.4F : 0.8F)
                .effects((new net.minecraft.world.biome.BiomeEffects.Builder())
                        .waterColor(snowy ? 4020182 : 4159204)
                        .waterFogColor(329011)
                        .fogColor(12638463)
                        .skyColor(getSkyColor(f))
                        .moodSound(BiomeMoodSound.CAVE)
                        .build())
                .spawnSettings(builder.build())
                .generationSettings(builder2.build())
                .build();
    }

    public static Biome createCherryOrchard() {
        SpawnSettings.Builder builder = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(builder);

        GenerationSettings.Builder builder2 = (new GenerationSettings.Builder());
        addCustomForestFeatures(builder2);
        builder2.feature(GenerationStep.Feature.VEGETAL_DECORATION, BakingPlacedFeatures.TREES_ORCHARD);

        return createBiome(builder, builder2);
    }

    public static Biome createLemonGrove() {
        SpawnSettings.Builder builder = new SpawnSettings.Builder();

        GenerationSettings.Builder builder2 = (new GenerationSettings.Builder());
        addCustomForestFeatures(builder2);
        DefaultBiomeFeatures.addForestTrees(builder2);
        DefaultBiomeFeatures.addDefaultFlowers(builder2);
        builder2.feature(GenerationStep.Feature.VEGETAL_DECORATION, BakingPlacedFeatures.TREES_GROVE);
        return createBiome(builder, builder2);
    }

    private static Biome createBiome(SpawnSettings.Builder builder, GenerationSettings.Builder builder2) {
        return (new Biome.Builder())
                .precipitation(Biome.Precipitation.RAIN)
                .category(Biome.Category.FOREST)
                .temperature(0.7F)
                .downfall(0.8F)
                .effects((new net.minecraft.world.biome.BiomeEffects.Builder())
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12638463)
                        .skyColor(getSkyColor(0.7F))
                        .moodSound(BiomeMoodSound.CAVE)
                        .build())
                .spawnSettings(builder.build())
                .generationSettings(builder2.build())
                .build();
    }

    public static void init() {}

    public static Biome register(String name, Biome biome) {
        RegistryKey<Biome> KEY = RegistryKey.of(Registry.BIOME_KEY, new Identifier(Baking.ID, name));
        return Registry.register(BuiltinRegistries.BIOME, KEY.getValue(), biome);
    }
}
