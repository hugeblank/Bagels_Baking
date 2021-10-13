package dev.elexi.hugeblank.bagels_baking.mixin.world;

import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.shape.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructurePoolBasedGenerator.ShapedPoolStructurePiece.class)
public interface ShapedPoolStructurePieceAccessor {
    @Invoker("<init>")
    static StructurePoolBasedGenerator.ShapedPoolStructurePiece createShapedPoolStructurePiece(PoolStructurePiece piece, MutableObject<VoxelShape> pieceShape, int minY, int currentSize) {
        throw new UnsupportedOperationException();
    }
}
