package dev.elexi.hugeblank.bagels_baking.mixin.world;

import com.google.common.collect.ImmutableList;
import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.Blocks;
import net.minecraft.structure.processor.RuleStructureProcessor;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.structure.processor.StructureProcessorRule;
import net.minecraft.structure.rule.AlwaysTrueRuleTest;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RandomBlockMatchRuleTest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StructureProcessorLists.class)
public class CropProcessorList {
    // Redistributing crop appearance in structures

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;of(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;", ordinal = 12), method = "<clinit>")
    private static ImmutableList<StructureProcessor> savanna_streets(Object ignore) {
        return ImmutableList.of(
                new RuleStructureProcessor(ImmutableList.of(
                        new StructureProcessorRule(new BlockMatchRuleTest(Blocks.GRASS_PATH), new BlockMatchRuleTest(Blocks.WATER), Blocks.ACACIA_PLANKS.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.GRASS_PATH, 0.2F), AlwaysTrueRuleTest.INSTANCE, Blocks.GRASS_BLOCK.getDefaultState()),
                        new StructureProcessorRule(new BlockMatchRuleTest(Blocks.GRASS_BLOCK), new BlockMatchRuleTest(Blocks.WATER), Blocks.WATER.getDefaultState()),
                        new StructureProcessorRule(new BlockMatchRuleTest(Blocks.DIRT), new BlockMatchRuleTest(Blocks.WATER), Blocks.WATER.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, Baking.RICE_PLANT.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.2F), AlwaysTrueRuleTest.INSTANCE, Baking.CORN_STALK.getDefaultState())
                )));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;of(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;", ordinal = 14), method = "<clinit>")
    private static ImmutableList<StructureProcessor> plains(Object ignore) {
        return ImmutableList.of(
                new RuleStructureProcessor(ImmutableList.of(
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.2F), AlwaysTrueRuleTest.INSTANCE, Baking.CORN_STALK.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.15F), AlwaysTrueRuleTest.INSTANCE, Blocks.POTATOES.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.15F), AlwaysTrueRuleTest.INSTANCE, Baking.TOMATO_PLANT.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.CARROTS.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.1F), AlwaysTrueRuleTest.INSTANCE, Baking.RICE_PLANT.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.05F), AlwaysTrueRuleTest.INSTANCE, Blocks.BEETROOTS.getDefaultState())
                )));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;of(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;", ordinal = 16), method = "<clinit>")
    private static ImmutableList<StructureProcessor> savanna_farms(Object ignore) {
        return ImmutableList.of(
                new RuleStructureProcessor(ImmutableList.of(
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, Baking.RICE_PLANT.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.2F), AlwaysTrueRuleTest.INSTANCE, Baking.CORN_STALK.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.MELON_STEM.getDefaultState())
                )));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;of(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;", ordinal = 17), method = "<clinit>")
    private static ImmutableList<StructureProcessor> snowy(Object ignore) {
        return ImmutableList.of(
                new RuleStructureProcessor(ImmutableList.of(
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.7F), AlwaysTrueRuleTest.INSTANCE, Blocks.POTATOES.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.CARROTS.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.1F), AlwaysTrueRuleTest.INSTANCE, Baking.TOMATO_PLANT.getDefaultState())
                )));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;of(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;", ordinal = 18), method = "<clinit>")
    private static ImmutableList<StructureProcessor> taiga(Object ignore) {
        return ImmutableList.of(
                new RuleStructureProcessor(ImmutableList.of(
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, Blocks.PUMPKIN_STEM.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.2F), AlwaysTrueRuleTest.INSTANCE, Blocks.POTATOES.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.2F), AlwaysTrueRuleTest.INSTANCE, Baking.RICE_PLANT.getDefaultState())
                )));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;of(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;", ordinal = 19), method = "<clinit>")
    private static ImmutableList<StructureProcessor> desert(Object ignore) {
        return ImmutableList.of(
                new RuleStructureProcessor(ImmutableList.of(
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, Baking.RICE_PLANT.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.2F), AlwaysTrueRuleTest.INSTANCE, Baking.TOMATO_PLANT.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.2F), AlwaysTrueRuleTest.INSTANCE, Blocks.BEETROOTS.getDefaultState()),
                        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.MELON_STEM.getDefaultState())

                )));
    }

    /* Vanilla rules written out cleanly so you don't have to see the abomination that was the source.

    FARM_PLAINS = register("farm_plains", ImmutableList.of(
    new RuleStructureProcessor(ImmutableList.of(
        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, Blocks.CARROTS.getDefaultState()),
        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.2F), AlwaysTrueRuleTest.INSTANCE, Blocks.POTATOES.getDefaultState()),
        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.BEETROOTS.getDefaultState())
    ))));
    FARM_SAVANNA = register("farm_savanna", ImmutableList.of(
    new RuleStructureProcessor(ImmutableList.of(
        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.MELON_STEM.getDefaultState())
    ))));
    FARM_SNOWY = register("farm_snowy", ImmutableList.of(
    new RuleStructureProcessor(ImmutableList.of(
        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.CARROTS.getDefaultState()),
        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.8F), AlwaysTrueRuleTest.INSTANCE, Blocks.POTATOES.getDefaultState())
    ))));
    FARM_TAIGA = register("farm_taiga", ImmutableList.of(
    new RuleStructureProcessor(ImmutableList.of(
        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, Blocks.PUMPKIN_STEM.getDefaultState()),
        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.2F), AlwaysTrueRuleTest.INSTANCE, Blocks.POTATOES.getDefaultState())
    ))));
    FARM_DESERT = register("farm_desert", ImmutableList.of(
    new RuleStructureProcessor(ImmutableList.of(
        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.2F), AlwaysTrueRuleTest.INSTANCE, Blocks.BEETROOTS.getDefaultState()),
        new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.MELON_STEM.getDefaultState())
    ))));

    */

}
