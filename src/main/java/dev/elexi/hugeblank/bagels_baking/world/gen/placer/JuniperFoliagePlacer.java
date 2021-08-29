package dev.elexi.hugeblank.bagels_baking.world.gen.placer;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.Random;
import java.util.function.BiConsumer;

public class JuniperFoliagePlacer extends BlobFoliagePlacer {
    //type 0 = square, type 1 = rounded square, type 2 = diamond
    //layer type, layer radius, layer minimum, layer maximum
    private static final int[][] layers = {
            //{0, 0, 1, 2},
            {1, 1, 2, 2},
            {0, 1, 2, 2},
            {2, 2, 3, 3},
            {1, 2, 2, 2},
            {2, 2, 1, 1}
    };

    public static final Codec<JuniperFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return createCodec(instance).apply(instance, JuniperFoliagePlacer::new);
    });
    protected final int height;

    public JuniperFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset, height);
        this.height = height;
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return (dx == radius && dz == radius);
    }

    @Override
    protected boolean isPositionInvalid(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        int k;
        int l;
        if (giantTrunk) {
            k = Math.min(Math.abs(dx), Math.abs(dx - 1));
            l = Math.min(Math.abs(dz), Math.abs(dz - 1));
        } else {
            k = Math.abs(dx);
            l = Math.abs(dz);
        }

        return this.isInvalidForLeaves(random, k, y, l, radius, giantTrunk);
    }

    protected void generateSquare(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, TreeFeatureConfig config, BlockPos centerPos, int radius, int y, boolean giantTrunk, boolean corners) {
        int i = giantTrunk ? 1 : 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(int j = -radius; j <= radius + i; ++j) {
            for(int k = -radius; k <= radius + i; ++k) {
                if (!this.isPositionInvalid(random, j, y, k, radius, giantTrunk) || corners) {
                    mutable.set(centerPos, j, y, k);
                    placeFoliageBlock(world, replacer, random, config, mutable);
                }
            }
        }

    }

    protected void generateDiamond(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, TreeFeatureConfig config, BlockPos centerPos, int radius, int y, boolean giantTrunk) {

        BlockPos.Mutable mutable = new BlockPos.Mutable();

        generateSquare(world,replacer, random, config, centerPos, radius-1, y, giantTrunk, true);

        for (int i = -1; i <= 1; i+=1) {
            for (int j = -1; j <= 1; j+=1) {
                if (Math.abs(i) != Math.abs(j)) {
                    mutable.set(centerPos, radius*i, y, radius*j);
                    placeFoliageBlock(world, replacer, random, config, mutable);
                }
            }
        }
    }

    @Override
    protected void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
        BlockPos blockPos = treeNode.getCenter();
        /*
        int rad = 2;

        for(int l = offset; l >= -foliageHeight; --l) {
            //this.generateSquare(world, replacer, random, config, blockPos, rad, l, treeNode.isGiantTrunk(), true);
            this.generateDiamond(world, replacer, random, config, blockPos, rad, l, treeNode.isGiantTrunk());

        }
        */
        int hatHeight = random.nextInt(2);

        for (int i = offset; i >= offset-1-hatHeight; --i) { // just the tip
            this.generateSquare(world, replacer, random, config, blockPos, 0, offset-i, treeNode.isGiantTrunk(), true);
        }

        int current = 0;
        for (int[] layer : layers) {
            for (int j = 0; j <= layer[2]; j++) {
                switch (layer[0]) {
                    case 0:
                        this.generateSquare(world, replacer, random, config, blockPos, layer[1], current-j, treeNode.isGiantTrunk(), true);
                        break;
                    case 1:
                        this.generateSquare(world, replacer, random, config, blockPos, layer[1], current-j, treeNode.isGiantTrunk(), false);
                        break;
                    case 2:
                        this.generateDiamond(world, replacer, random, config, blockPos, layer[1], current-j, treeNode.isGiantTrunk());
                        break;
                }
            }
            current -= layer[2];
        }
    }
}
