package dev.elexi.hugeblank.bagels_baking.world.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.pool.*;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class SingleAggressivePoolElement extends SinglePoolElement {
    // Pool element that hides jigsaw blocks (except for ones tagged with minecraft:building_entrance) and then generates them after the placement for the structure has been decided
    // This prevents things like cellars clipping into impeding structure boxes.

    public static final Codec<SingleAggressivePoolElement> CODEC;
    public static final Codec<Either<Identifier, Structure>> ELEMENT_KEY;

    private static <T> DataResult<T> serialize(Either<Identifier, Structure> either, DynamicOps<T> dynamicOps, T object) {
        Optional<Identifier> optional = either.left();
        return !optional.isPresent() ? DataResult.error("Can not serialize a runtime pool element") : Identifier.CODEC.encode(optional.get(), dynamicOps, object);
    }

    protected static <E extends SingleAggressivePoolElement> RecordCodecBuilder<E, Supplier<StructureProcessorList>> encodeProcessors() {
        return StructureProcessorType.REGISTRY_CODEC.fieldOf("processors").forGetter((element) -> element.processors);
    }

    protected static <E extends SingleAggressivePoolElement> RecordCodecBuilder<E, Either<Identifier, Structure>> encodeLocation() {
        return ELEMENT_KEY.fieldOf("location").forGetter((singlePoolElement) -> singlePoolElement.location);
    }

    public static Function<StructurePool.Projection, SingleAggressivePoolElement> ofAggressiveSingle(String id) {
        return (projection) -> new SingleAggressivePoolElement(Either.left(new Identifier(id)), () -> StructureProcessorLists.EMPTY, projection);
    }

    public SingleAggressivePoolElement(Either<Identifier, Structure> location, Supplier<StructureProcessorList> processors, StructurePool.Projection projection) {
        super(location, processors, projection);
    }

    @Override
    public List<Structure.StructureBlockInfo> getStructureBlockInfos(StructureManager structureManager, BlockPos pos, BlockRotation rotation, Random random) {
        List<Structure.StructureBlockInfo> jigsaws = super.getStructureBlockInfos(structureManager, pos, rotation, random);
        jigsaws.removeIf(jigsaw -> !jigsaw.nbt.getString("name").contains("minecraft:building_entrance"));
        return jigsaws;
    }

    private Structure createStructure(StructureManager structureManager) {
        Objects.requireNonNull(structureManager);
        return ((Either<Identifier, Structure>) this.location).map(structureManager::getStructureOrBlank, Function.identity());
    }

    @Override
    public boolean generate(StructureManager structureManager, StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, BlockPos pos, BlockPos blockPos, BlockRotation rotation, BlockBox box, Random random, boolean keepJigsaws) {
        // Close. Structures are generating from the jigsaw, but not attached.
        Structure structure = this.createStructure(structureManager);
        StructurePlacementData structurePlacementData = this.createPlacementData(rotation, box, keepJigsaws);
        if (structure.place(world, pos, blockPos, structurePlacementData, random, Block.NOTIFY_LISTENERS | Block.FORCE_STATE)) {
            List<Structure.StructureBlockInfo> jigsaws = super.getStructureBlockInfos(structureManager, pos, rotation, random);
            List<PoolStructurePiece> poolStructurePieces = Lists.newArrayList();
            Map<PoolStructurePiece, BlockPos> pieceBlockPosMap = Maps.newHashMap();
            for (Structure.StructureBlockInfo jigsaw : jigsaws) {
                subGenerate(new Identifier(jigsaw.nbt.getString("pool")), structureManager, world, jigsaw.pos, rotation, random, poolStructurePieces, pieceBlockPosMap);
            }
            for (PoolStructurePiece piece : poolStructurePieces) {
                piece.generate(world, structureAccessor, chunkGenerator, random, BlockBox.infinite(), pieceBlockPosMap.get(piece), false);
            }
            return true;
        }

        return false;
    }

    private void subGenerate(Identifier poolID, StructureManager structureManager, StructureWorldAccess world, BlockPos blockPos, BlockRotation rotation, Random random, List<PoolStructurePiece> elements, Map<PoolStructurePiece, BlockPos> positionMap) {
        // Aggressive, evil, brutish, hostile, and dare I say despicable. If you want to make this not ugly by all means contribute...
        StructurePool pool = world.getRegistryManager().get(Registry.STRUCTURE_POOL_KEY).get(poolID);
        if (pool != null && pool.getElementCount() > 0) {
            StructurePoolElement structurePoolElement = pool.getRandomElement(random);
            PoolStructurePiece piece = new PoolStructurePiece(structureManager, structurePoolElement, blockPos, 1, BlockRotation.NONE, new BlockBox(blockPos));
            elements.add(piece);
            positionMap.put(piece, blockPos);

            List<Structure.StructureBlockInfo> jigsaws = structurePoolElement.getStructureBlockInfos(structureManager, blockPos, rotation, random);
            for (Structure.StructureBlockInfo jigsaw : jigsaws) {
                subGenerate(new Identifier(jigsaw.nbt.getString("pool")), structureManager, world, jigsaw.pos, rotation, random, elements, positionMap);
            }
        }
    }

    public StructurePoolElementType<?> getType() {
        return BakingPoolElements.SINGLE_AGGRESSIVE_POOL_ELEMENT;
    }

    public String toString() {
        // me_irl
        return "AggressiveSingle[" + this.location + "]";
    }

    static {
        ELEMENT_KEY = Codec.of(SingleAggressivePoolElement::serialize, Identifier.CODEC.map(Either::left));
        CODEC = RecordCodecBuilder.create((instance) -> instance.group(encodeLocation(), encodeProcessors(), method_28883()).apply(instance, (Function3<Either<Identifier, Structure>, Supplier<StructureProcessorList>, StructurePool.Projection, SingleAggressivePoolElement>) SingleAggressivePoolElement::new));
    }


}
