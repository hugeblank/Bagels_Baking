package dev.elexi.hugeblank.bagels_baking.block.entity;

import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class IceBoxBlockEntity extends ChestBlockEntity {

    public IceBoxBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public IceBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(Baking.ICE_BOX_ENTITY_TYPE, blockPos, blockState);
    }

    protected Text getContainerName() {
        return new TranslatableText("container.iceBox");
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new GenericContainerScreenHandler(Baking.ICE_BOX_9X3, syncId, playerInventory, this, 3);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        super.onOpen(player);
        if (this.hasWorld()) world.setBlockState(this.getPos(), this.getCachedState().with(Properties.LIT, true));
    }

    @Override
    public void onClose(PlayerEntity player) {
        super.onClose(player);
        if (this.hasWorld()) world.setBlockState(this.getPos(), this.getCachedState().with(Properties.LIT, false));
    }
}
