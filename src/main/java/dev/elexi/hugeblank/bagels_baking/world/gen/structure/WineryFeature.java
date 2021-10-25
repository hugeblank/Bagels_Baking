package dev.elexi.hugeblank.bagels_baking.world.gen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class WineryFeature extends StructureFeature<DefaultFeatureConfig> {
    // see: IglooFeature.class
    private final Type type;
    public WineryFeature(Codec<DefaultFeatureConfig> codec, Type type) {
        super(codec);
        this.type = type;
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return ((feature, pos, references, seed) -> new Start(feature, pos, references, seed, type));
    }

    public static class Start extends StructureStart<DefaultFeatureConfig> {
        private final Type type;
        public Start(StructureFeature<DefaultFeatureConfig> structureFeature, ChunkPos chunkPos, int i, long l, Type type) {
            super(structureFeature, chunkPos, i, l);
            this.type = type;
        }

        public void init(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, DefaultFeatureConfig defaultFeatureConfig, HeightLimitView heightLimitView) {
            BlockPos blockPos = new BlockPos(chunkPos.getStartX(), chunkGenerator.getHeightOnGround(chunkPos.getStartX(), chunkPos.getStartZ(), Heightmap.Type.WORLD_SURFACE_WG, heightLimitView)-1, chunkPos.getStartZ());
            BlockRotation blockRotation = BlockRotation.random(this.random);
            WineryGenerator.generate(structureManager, blockPos, blockRotation, this, this.random, type);
        }
    }

    public enum Type {
        PLAINS("plains"),
        SAVANNA("savanna");

        final String type;
        Type(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Type{" +
                    "type='" + type + '\'' +
                    '}';
        }
    }
}
