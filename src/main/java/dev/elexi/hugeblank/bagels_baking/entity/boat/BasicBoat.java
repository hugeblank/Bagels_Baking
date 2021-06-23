package dev.elexi.hugeblank.bagels_baking.entity.boat;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class BasicBoat {
    private final BoatEntity.Type type = BoatEntity.Type.OAK;
    private final Item boatItem;
    private final Item plankItem;
    private final Identifier texture;

    public BasicBoat(Item boatItem, Item plankItem, String plankType) {
        this.boatItem = boatItem;
        this.plankItem = plankItem;
        this.texture = new Identifier(Baking.ID, "textures/entity/boat/" + plankType + "_boat.png");
    }

    public Item asItem() {
        return boatItem;
    }

    public Item asPlanks() {
        return plankItem;
    }

    public Identifier getSkin() {
        return texture;
    }

    public BoatEntity.Type getType() {
        return type;
    }
}
