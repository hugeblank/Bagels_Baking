package dev.elexi.hugeblank.bagels_baking.entity.boat;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.item.BasicBoatItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BasicBoatRegistry {
    public EntityType<BasicBoatEntity> BOAT;

    private BasicBoatRegistry(String name, Item planks) {
        BasicBoatItem boat_item = new BasicBoatItem(() -> BOAT, new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1));
        BasicBoat boat_type = new BasicBoat(boat_item, planks.asItem(), name);
        BOAT = Registry.register(Registry.ENTITY_TYPE, new Identifier(Baking.ID, name + "_boat"),
                FabricEntityTypeBuilder.<BasicBoatEntity>create(SpawnGroup.MISC, (entity, world) -> new BasicBoatEntity(entity, world, boat_type))
                        .dimensions(new EntityDimensions(1.375f, 0.5625f, true))
                        .trackedUpdateRate(10)
                        .build());
        Registry.register(Registry.ITEM, new Identifier(Baking.ID, name + "_boat"), boat_item);

        // THIS IS BAD - I SHOULD NOT BE ALLOWED TO PROGRAM.
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            EntityRendererRegistry.INSTANCE.register(BOAT, BoatEntityRenderer::new);
        }
    }

    public static void register(String name, Item planks) {
        new BasicBoatRegistry(name, planks);
    }
}