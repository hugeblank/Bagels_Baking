package dev.elexi.hugeblank.bagels_baking.world.biome;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class BakingBiomes {

    public static final Biome CHERRY_ORCHARD = createCherryOrchard();

    private static int getSkyColor(float temperature) {
        float f = temperature / 3.0F;
        f = MathHelper.clamp(f, -1.0F, 1.0F);
        return MathHelper.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
    }

    public static Biome createCherryOrchard() {
        SpawnSettings.Builder builder = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(builder);
        builder.playerSpawnFriendly();

        GenerationSettings.Builder builder2 = (new GenerationSettings.Builder()).surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        builder2.structureFeature(ConfiguredStructureFeatures.VILLAGE_PLAINS).structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);

        DefaultBiomeFeatures.addDefaultUndergroundStructures(builder2);
        builder2.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
        DefaultBiomeFeatures.addLandCarvers(builder2);
        DefaultBiomeFeatures.addDefaultLakes(builder2);
        DefaultBiomeFeatures.addAmethystGeodes(builder2);
        DefaultBiomeFeatures.addDungeons(builder2);
        DefaultBiomeFeatures.addPlainsTallGrass(builder2);

        DefaultBiomeFeatures.addMineables(builder2);
        DefaultBiomeFeatures.addDefaultOres(builder2);
        DefaultBiomeFeatures.addDefaultDisks(builder2);
        DefaultBiomeFeatures.addPlainsFeatures(builder2);
        builder2.feature(GenerationStep.Feature.VEGETAL_DECORATION, Baking.CHERRY_TREES);

        DefaultBiomeFeatures.addDefaultMushrooms(builder2);
        DefaultBiomeFeatures.addDefaultVegetation(builder2);

        DefaultBiomeFeatures.addSprings(builder2);
        DefaultBiomeFeatures.addFrozenTopLayer(builder2);
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
                            .grassColor(0x99215b)
                            .moodSound(BiomeMoodSound.CAVE)
                            .build())
                    .spawnSettings(builder.build())
                    .generationSettings(builder2.build())
                    .build();
    }
}
