package dev.elexi.hugeblank.bagels_baking.world.gen.structure;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class BakingConfiguredStructures {
    // fear
    public static final StructurePieceType WINERY_PIECE_TYPE = WineryGenerator.Piece::new;
    public static final StructureFeature<DefaultFeatureConfig> PLAINS_WINERY_FEATURE = new WineryFeature(DefaultFeatureConfig.CODEC, WineryFeature.Type.PLAINS);
    public static final ConfiguredStructureFeature<DefaultFeatureConfig, ? extends StructureFeature<DefaultFeatureConfig>> PLAINS_WINERY = PLAINS_WINERY_FEATURE.configure(DefaultFeatureConfig.INSTANCE);
    public static final StructureFeature<DefaultFeatureConfig> SAVANNA_WINERY_FEATURE = new WineryFeature(DefaultFeatureConfig.CODEC, WineryFeature.Type.SAVANNA);
    public static final ConfiguredStructureFeature<DefaultFeatureConfig, ? extends StructureFeature<DefaultFeatureConfig>> SAVANNA_WINERY = SAVANNA_WINERY_FEATURE.configure(DefaultFeatureConfig.INSTANCE);



    public static void init() {
        Registry.register(Registry.STRUCTURE_PIECE, new Identifier(Baking.ID, "winery_piece"), WINERY_PIECE_TYPE);

        FabricStructureBuilder.create(new Identifier(Baking.ID, "plains_winery"), PLAINS_WINERY_FEATURE)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                .defaultConfig(32, 8, 6969) // True freedom is changing the salt value
                .register();
        RegistryKey<ConfiguredStructureFeature<?, ?>> plains_key = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(Baking.ID, "plains_winery"));
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, plains_key.getValue(), PLAINS_WINERY);
        BiomeModifications.addStructure(BiomeSelectors.includeByKey(BiomeKeys.PLAINS), plains_key);

        FabricStructureBuilder.create(new Identifier(Baking.ID, "savanna_winery"), SAVANNA_WINERY_FEATURE)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                .defaultConfig(32, 8, 420420)
                .register();
        RegistryKey<ConfiguredStructureFeature<?, ?>> savanna_key = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(Baking.ID, "savanna_winery"));
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, savanna_key.getValue(), SAVANNA_WINERY);
        BiomeModifications.addStructure(BiomeSelectors.includeByKey(BiomeKeys.SAVANNA), savanna_key);
    }
}
