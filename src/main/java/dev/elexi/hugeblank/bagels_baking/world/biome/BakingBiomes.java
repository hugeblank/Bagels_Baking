package dev.elexi.hugeblank.bagels_baking.world.biome;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.world.gen.BakingClusterPlacedFeatures;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

public class BakingBiomes {
    public static final RegistryKey<Biome> CHERRY_ORCHARD;
    public static final RegistryKey<Biome> LEMON_GROVE;
    public static final RegistryKey<Biome> JUNIPER_TAIGA;
    public static final RegistryKey<Biome> SNOWY_JUNIPER_TAIGA;

    static {
        CHERRY_ORCHARD = registerBiome("cherry_orchard", BakingBiomes.createCherryOrchard());
        LEMON_GROVE = registerBiome("lemon_grove", BakingBiomes.createLemonGrove()); // This is really awkward.
        // Lemon groves were called lemon groves before we knew 1.18 would have grove biomes that were snowy.
        JUNIPER_TAIGA = registerBiome("juniper_taiga", BakingBiomes.createJuniperTaiga(false));
        SNOWY_JUNIPER_TAIGA = registerBiome("snowy_juniper_taiga", BakingBiomes.createJuniperTaiga(true));
    }

    private static int getSkyColor(float temperature) {
        float f = temperature / 3.0F;
        f = MathHelper.clamp(f, -1.0F, 1.0F);
        return MathHelper.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
    }

    public static Biome createJuniperTaiga(boolean cold) {
        SpawnSettings.Builder builder = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(builder);
        builder
                .spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.WOLF, 8, 4, 4))
                .spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.RABBIT, 4, 2, 3))
                .spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.FOX, 8, 2, 4));


        DefaultBiomeFeatures.addBatsAndMonsters(builder);
        float f = cold ? -0.5F : 0.25F;
        GenerationSettings.Builder builder2 = (new GenerationSettings.Builder());

        builder2.feature(
                GenerationStep.Feature.VEGETAL_DECORATION, BakingClusterPlacedFeatures.TREES_TAIGA
        );

        addBasicFeatures(builder2);

        DefaultBiomeFeatures.addLargeFerns(builder2);
        DefaultBiomeFeatures.addDefaultOres(builder2);
        DefaultBiomeFeatures.addDefaultDisks(builder2);
        DefaultBiomeFeatures.addDefaultFlowers(builder2);
        DefaultBiomeFeatures.addTaigaGrass(builder2);
        DefaultBiomeFeatures.addDefaultMushrooms(builder2);
        DefaultBiomeFeatures.addDefaultVegetation(builder2);
        if (cold) {
            DefaultBiomeFeatures.addSweetBerryBushesSnowy(builder2);
        } else {
            DefaultBiomeFeatures.addSweetBerryBushes(builder2);
        }
        return (new Biome.Builder())
                .precipitation(cold ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN)
                .category(Biome.Category.TAIGA)
                .temperature(f)
                .downfall(cold ? 0.4F : 0.8F)
                .effects((new net.minecraft.world.biome.BiomeEffects.Builder())
                        .waterColor(cold ? 4020182 : 4159204)
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
        return createCustomForestBiome(BakingClusterPlacedFeatures.TREES_ORCHARD);
    }

    public static Biome createLemonGrove() {
        return createCustomForestBiome(BakingClusterPlacedFeatures.TREES_GROVE);
    }

    private static void addBasicFeatures(GenerationSettings.Builder generationSettings) {
        DefaultBiomeFeatures.addLandCarvers(generationSettings);
        DefaultBiomeFeatures.addAmethystGeodes(generationSettings);
        DefaultBiomeFeatures.addDungeons(generationSettings);
        DefaultBiomeFeatures.addMineables(generationSettings);
        DefaultBiomeFeatures.addSprings(generationSettings);
        DefaultBiomeFeatures.addFrozenTopLayer(generationSettings);
    }

    private static Biome createCustomForestBiome(RegistryEntry<PlacedFeature> customTrees) {
        SpawnSettings.Builder builder = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(builder);
        DefaultBiomeFeatures.addBatsAndMonsters(builder);

        GenerationSettings.Builder builder2 = (new GenerationSettings.Builder());

        addBasicFeatures(builder2);

        DefaultBiomeFeatures.addForestFlowers(builder2);

        DefaultBiomeFeatures.addDefaultOres(builder2);
        DefaultBiomeFeatures.addDefaultDisks(builder2);

        builder2.feature(GenerationStep.Feature.VEGETAL_DECORATION, BakingClusterPlacedFeatures.TREES_BIRCH_AND_OAK_SPARSE);
        builder2.feature(GenerationStep.Feature.VEGETAL_DECORATION, customTrees);
        DefaultBiomeFeatures.addForestGrass(builder2);

        DefaultBiomeFeatures.addDefaultMushrooms(builder2);
        DefaultBiomeFeatures.addDefaultVegetation(builder2);
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

    public static RegistryKey<Biome> registerBiome(String name, Biome biome) {
        RegistryKey<Biome> KEY = RegistryKey.of(Registry.BIOME_KEY, new Identifier(Baking.ID, name));
        Registry.register(BuiltinRegistries.BIOME, KEY.getValue(), biome);
        return KEY;
    }

}
