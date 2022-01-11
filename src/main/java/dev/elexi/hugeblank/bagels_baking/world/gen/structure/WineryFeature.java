package dev.elexi.hugeblank.bagels_baking.world.gen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class WineryFeature extends StructureFeature<DefaultFeatureConfig> {
    // see: IglooFeature.class
    public WineryFeature(Codec<DefaultFeatureConfig> codec, Type type) {
        super(codec, StructureGeneratorFactory.simple(StructureGeneratorFactory.checkForBiomeOnTop(Heightmap.Type.WORLD_SURFACE_WG), (collector, context) -> {
            int y = context.chunkGenerator().getHeightOnGround(context.chunkPos().getStartX(), context.chunkPos().getStartZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world());
            BlockPos blockPos = new BlockPos(context.chunkPos().getStartX(), y, context.chunkPos().getStartZ());
            BlockRotation blockRotation = BlockRotation.random(context.random());
            WineryGenerator.generate(context.structureManager(), blockPos, blockRotation, collector, type);
        }));
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
