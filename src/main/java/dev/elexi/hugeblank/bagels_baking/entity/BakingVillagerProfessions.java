package dev.elexi.hugeblank.bagels_baking.entity;

import com.google.common.collect.ImmutableSet;
import dev.elexi.hugeblank.bagels_baking.Baking;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Set;

public class BakingVillagerProfessions {
    public static final Identifier VINTNER_PROFESSION_ID = new Identifier(Baking.ID, "vintner");
    public static final VillagerProfession VINTNER_PROFESSION;
    public static final Identifier VINTNER_WORKSTATION_ID = new Identifier(Baking.ID, "vintner_workstation");
    public static final PointOfInterestType VINTNER_WORKSTATION;

    public static void init() {
        Registry.register(Registry.VILLAGER_PROFESSION, VINTNER_PROFESSION_ID, VINTNER_PROFESSION);
    }

    private static Set<BlockState> getAllStatesOf(Block block) {
        return ImmutableSet.copyOf(block.getStateManager().getStates());
    }

    static {
        VINTNER_WORKSTATION = PointOfInterestHelper.register(VINTNER_WORKSTATION_ID, 1, 1, getAllStatesOf(Baking.FERMENTER));
        VINTNER_PROFESSION = VillagerProfessionBuilder.create()
                .id(VINTNER_PROFESSION_ID)
                .workstation(VINTNER_WORKSTATION)
                .workSound(SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP)
                .build();
    }
}
