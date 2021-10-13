package dev.elexi.hugeblank.bagels_baking.mixin.world;

import net.minecraft.block.JigsawBlock;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(StructurePoolBasedGenerator.StructurePoolGenerator.class)
public class StructurePoolGeneratorMixin {
    @Shadow @Final private int maxSize;
    @Shadow @Final private StructurePoolBasedGenerator.PieceFactory pieceFactory;
    @Shadow @Final private ChunkGenerator chunkGenerator;
    @Shadow @Final private StructureManager structureManager;
    @Shadow @Final private List<? super PoolStructurePiece> children;
    @Shadow @Final private Random random;
    @Shadow @Final Deque<StructurePoolBasedGenerator.ShapedPoolStructurePiece> structurePieces;

    // Override the ENTIRE structure generation algorithm just so we can plop down our fat village structures. THIS IS WHAT INSANITY LOOKS LIKE ~ hugeblank 10/15/21
    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z", ordinal = 2), method = "generatePiece(Lnet/minecraft/structure/PoolStructurePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IIZLnet/minecraft/world/HeightLimitView;)V", cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void fuckThisMethodInParticular(PoolStructurePiece piece, MutableObject<VoxelShape> pieceShape, int minY, int currentSize, boolean modifyBoundingBox, HeightLimitView world, CallbackInfo ci, StructurePoolElement structurePoolElement, BlockPos blockPos, BlockRotation blockRotation, StructurePool.Projection projection, boolean bl, MutableObject mutableObject, BlockBox blockBox, int i, Iterator var15, Structure.StructureBlockInfo structureBlockInfo, Direction direction, BlockPos blockPos2, BlockPos blockPos3, int j, int k, Identifier identifier, Optional optional, Identifier identifier2, Optional optional2, MutableObject mutableObject3, int m, List list, Iterator var30, StructurePoolElement structurePoolElement2, Iterator var32) {
        if (structurePoolElement2 instanceof SinglePoolElement singlePoolElement && singlePoolElement.toString().contains("minecraft:village/savanna/houses/savanna_winery")) {
            // blockPos2 = streetJigsawPos
            while (var32.hasNext()) { // iterate over all rotations (randomly?)
                BlockRotation selectedRotation = (BlockRotation) var32.next(); // blockRotation2
                List<Structure.StructureBlockInfo> jigsaws = structurePoolElement2.getStructureBlockInfos(this.structureManager, BlockPos.ORIGIN, selectedRotation, this.random);

                // Tests for rotation alignment, skips this iteration if there's no jigsaw that matches.
                Structure.StructureBlockInfo matchingJigsaw = null;
                for (Structure.StructureBlockInfo jigsawInfo : jigsaws) {
                    if (JigsawBlock.attachmentMatches(structureBlockInfo, jigsawInfo)) {
                        matchingJigsaw = jigsawInfo;
                    }
                }
                if (matchingJigsaw == null) continue;

                BlockPos houseJigsawPos = matchingJigsaw.pos; // blockPos4
                BlockPos jigsawTouchingPos = blockPos3.subtract(houseJigsawPos); // blockPos5
                BlockBox houseBoundingBoxInWorld = structurePoolElement2.getBoundingBox(this.structureManager, jigsawTouchingPos, selectedRotation); // blockBox3
                int lowestYValueForHouseInWorld = houseBoundingBoxInWorld.getMinY(); // p
                StructurePool.Projection streetProjection = structurePoolElement2.getProjection(); // projection2
                boolean isRigid = streetProjection == StructurePool.Projection.RIGID; // bl3
                int jigsawPosY = houseJigsawPos.getY(); // q
                int jigsawOffsetY = j - jigsawPosY + JigsawBlock.getFacing(structureBlockInfo.state).getOffsetY(); // j = streetJigsawY // r
                int surfaceY; // t
                if (bl && isRigid) {
                    surfaceY = i + jigsawOffsetY;
                } else {
                    if (k == -1) {
                        k = this.chunkGenerator.getHeightOnGround(blockPos2.getX(), blockPos2.getZ(), Heightmap.Type.WORLD_SURFACE_WG, world);
                    }

                    surfaceY = k - jigsawPosY;
                }
                int surfaceYOffset = surfaceY - lowestYValueForHouseInWorld; // u
                BlockBox finalHouseBoundingBox = houseBoundingBoxInWorld.offset(0, surfaceYOffset, 0); // blockBox4
                BlockPos finalHouseJigsawPos = jigsawTouchingPos.add(0, surfaceYOffset, 0); // blockPos6

                if (!VoxelShapes.matchesAnywhere((VoxelShape)mutableObject3.getValue(), VoxelShapes.cuboid(Box.from(finalHouseBoundingBox).withMinY(surfaceY).contract(0.5D)), BooleanBiFunction.ONLY_SECOND)) {
                    mutableObject3.setValue(VoxelShapes.combine((VoxelShape) mutableObject3.getValue(), VoxelShapes.cuboid(Box.from(finalHouseBoundingBox)), BooleanBiFunction.ONLY_FIRST));
                    int streetPieceGroundLevelOffset = piece.getGroundLevelDelta(); // w
                    int housePieceGroundLevelOffset; // y
                    if (isRigid) {
                        housePieceGroundLevelOffset = streetPieceGroundLevelOffset - jigsawOffsetY;
                    } else {
                        housePieceGroundLevelOffset = structurePoolElement2.getGroundLevelDelta();
                    }

                    PoolStructurePiece poolStructurePiece = this.pieceFactory.create(this.structureManager, structurePoolElement2, finalHouseJigsawPos, housePieceGroundLevelOffset, selectedRotation, finalHouseBoundingBox);
                    int jigsawSurfaceYOffset; // ab
                    if (bl) {
                        jigsawSurfaceYOffset = i + j;
                    } else if (isRigid) {
                        jigsawSurfaceYOffset = surfaceY + jigsawPosY;
                    } else {
                        if (k == -1) {
                            k = this.chunkGenerator.getHeightOnGround(blockPos2.getX(), blockPos2.getZ(), Heightmap.Type.WORLD_SURFACE_WG, world);
                        }

                        jigsawSurfaceYOffset = k + jigsawOffsetY / 2;
                    }
                    piece.addJunction(new JigsawJunction(blockPos3.getX(), jigsawSurfaceYOffset - j + streetPieceGroundLevelOffset, blockPos3.getZ(), jigsawOffsetY, streetProjection));
                    poolStructurePiece.addJunction(new JigsawJunction(blockPos2.getX(), jigsawSurfaceYOffset - jigsawPosY + housePieceGroundLevelOffset, blockPos2.getZ(), -jigsawOffsetY, projection));
                    this.children.add(poolStructurePiece);
                    if (currentSize + 1 <= this.maxSize) {
                        this.structurePieces.addLast(ShapedPoolStructurePieceAccessor.createShapedPoolStructurePiece(poolStructurePiece, mutableObject3, m, currentSize + 1));
                    }
                }
            }
            ci.cancel();
        }
    }
}
