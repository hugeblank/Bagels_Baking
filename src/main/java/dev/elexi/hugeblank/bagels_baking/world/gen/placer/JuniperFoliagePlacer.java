package dev.elexi.hugeblank.bagels_baking.world.gen.placer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.mixin.world.FoliagePlacerTypeInvoker;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.Random;
import java.util.function.BiConsumer;

public class JuniperFoliagePlacer extends FoliagePlacer {
    //type 0 = square, type 1 = rounded square, type 2 = diamond
    //layer type, layer radius, layer minimum, layer maximum
    private static final int[][] layers = {
            //{0, 0, 1, 2},
            {1, 1, 1, 3},
            {0, 1, 1, 3},
            {2, 2, 2, 4},
            {1, 2, 2, 4},
            {2, 2, 1, 2}
    };



    protected final IntProvider height;

    public static final Codec<JuniperFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillFoliagePlacerFields(instance)
            .and(IntProvider.createValidatingCodec(1, 512).fieldOf("foliage_height").forGetter(JuniperFoliagePlacer::getFoliageHeight))
            .apply(instance, JuniperFoliagePlacer::new));



    protected FoliagePlacerType<?> getType() {
        return Baking.JUNIPER_FOLIAGE_PLACER;
    }

    public JuniperFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider height) {
        super(radius, offset);
        this.height = height;
    }

    private IntProvider getFoliageHeight() {
        return this.height;
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
        int hatHeight = random.nextInt(3);

        for (int i = offset; i >= offset-1-hatHeight; --i) { // just the tip
            this.generateSquare(world, replacer, random, config, blockPos, 0, offset-i, treeNode.isGiantTrunk(), true);
        }

        int minHeight = 0;
        for (int[] layer : layers) {
            minHeight += layer[2];
        }

        int spareHeight = foliageHeight-minHeight;

        int current = 0;
        for (int[] layer : layers) {

            int variance = random.nextInt((layer[3]-layer[2])+1);
            variance = Math.min(variance, spareHeight);
            spareHeight -= variance;

            int thisHeight = layer[2] + variance;

            for (int j = 0; j <= thisHeight; j++) {
                switch (layer[0]) {
                    case 0 -> this.generateSquare(world, replacer, random, config, blockPos, layer[1], current - j, treeNode.isGiantTrunk(), true);
                    case 1 -> this.generateSquare(world, replacer, random, config, blockPos, layer[1], current - j, treeNode.isGiantTrunk(), false);
                    case 2 -> this.generateDiamond(world, replacer, random, config, blockPos, layer[1], current - j, treeNode.isGiantTrunk());
                }
            }
            current -= thisHeight;
        }
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return getFoliageHeight().get(random);
    }
}
