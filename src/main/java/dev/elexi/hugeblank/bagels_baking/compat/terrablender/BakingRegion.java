package dev.elexi.hugeblank.bagels_baking.compat.terrablender;

import com.mojang.datafixers.util.Pair;
import dev.elexi.hugeblank.bagels_baking.world.biome.BakingBiomes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class BakingRegion extends Region {

    public BakingRegion(Identifier name, int weight)
    {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {
        this.addBiomeSimilar(mapper, BiomeKeys.FOREST, BakingBiomes.CHERRY_ORCHARD);
        this.addBiomeSimilar(mapper, BiomeKeys.FOREST, BakingBiomes.LEMON_GROVE);
        this.addBiomeSimilar(mapper, BiomeKeys.TAIGA, BakingBiomes.JUNIPER_TAIGA);
        this.addBiomeSimilar(mapper, BiomeKeys.SNOWY_TAIGA, BakingBiomes.SNOWY_JUNIPER_TAIGA);
    }
}
