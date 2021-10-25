package dev.elexi.hugeblank.bagels_baking.world.gen.structure;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;

import java.util.Random;

public class WineryGenerator {

    private static final Identifier PLAINS_HOUSE = new Identifier(Baking.ID, "winery/plains_winery");
    private static final Identifier PLAINS_CELLAR = new Identifier(Baking.ID, "winery/plains_cellar");
    private static final Identifier SAVANNA_HOUSE = new Identifier(Baking.ID, "winery/savanna_winery");
    private static final Identifier SAVANNA_CELLAR = new Identifier(Baking.ID, "winery/savanna_cellar");

    WineryGenerator() {}

    public static void generate(StructureManager manager, BlockPos pos, BlockRotation rotation, StructurePiecesHolder structurePiecesHolder, Random random, WineryFeature.Type type) {
        switch (type) {
            case PLAINS -> {
                structurePiecesHolder.addPiece(new WineryGenerator.Piece(manager, PLAINS_HOUSE, pos, rotation, 0));
                structurePiecesHolder.addPiece(new WineryGenerator.Piece(manager, PLAINS_CELLAR, pos, rotation, 6));
            }
            case SAVANNA -> {
                structurePiecesHolder.addPiece(new WineryGenerator.Piece(manager, SAVANNA_HOUSE, pos, rotation, 0));
                structurePiecesHolder.addPiece(new WineryGenerator.Piece(manager, SAVANNA_CELLAR, pos, rotation, 6));
            }
        }
    }

    public static class Piece extends SimpleStructurePiece {
        public Piece(StructureManager manager, Identifier identifier, BlockPos pos, BlockRotation rotation, int yOffset) {
            super(BakingConfiguredStructures.WINERY_PIECE_TYPE, 0, manager, identifier, identifier.toString(), createPlacementData(rotation), pos.down(yOffset));
        }

        public Piece(ServerWorld serverWorld, NbtCompound nbtCompound) {
            super(BakingConfiguredStructures.WINERY_PIECE_TYPE, nbtCompound, serverWorld, (identifier) -> createPlacementData(BlockRotation.valueOf(nbtCompound.getString("Rot"))));
        }

        protected void writeNbt(ServerWorld world, NbtCompound nbt) {
            super.writeNbt(world, nbt);
            nbt.putString("Rot", this.placementData.getRotation().name());
        }

        private static StructurePlacementData createPlacementData(BlockRotation rotation) {
            return (new StructurePlacementData()).setRotation(rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
            if (metadata.equals("villager")) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                world.spawnEntity(EntityType.VILLAGER.spawn(world.toServerWorld(), null, null, null, pos, SpawnReason.NATURAL, true, false));
            }
        }
    }
}