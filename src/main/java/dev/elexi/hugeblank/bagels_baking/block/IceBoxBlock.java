package dev.elexi.hugeblank.bagels_baking.block;

import dev.elexi.hugeblank.bagels_baking.Baking;
import dev.elexi.hugeblank.bagels_baking.block.entity.IceBoxBlockEntity;
import dev.elexi.hugeblank.bagels_baking.recipe.FreezingRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class IceBoxBlock extends ChestBlock {
    public static final BooleanProperty LIT = Properties.LIT;

    private static final DoubleBlockProperties.PropertyRetriever<ChestBlockEntity, Optional<NamedScreenHandlerFactory>> NAME_RETRIEVER = new DoubleBlockProperties.PropertyRetriever<>() {
        public Optional<NamedScreenHandlerFactory> getFromBoth(final ChestBlockEntity leftIceBox, final ChestBlockEntity rightIceBox) {
            final Inventory inventory = new DoubleInventory(leftIceBox, rightIceBox);
            return Optional.of(new NamedScreenHandlerFactory() {
                @Nullable
                public ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    if (leftIceBox.checkUnlocked(playerEntity) && rightIceBox.checkUnlocked(playerEntity)) {
                        leftIceBox.checkLootInteraction(playerInventory.player);
                        rightIceBox.checkLootInteraction(playerInventory.player);
                        return new GenericContainerScreenHandler(Baking.ICE_BOX_9X6, i, playerInventory, inventory, 6);
                    } else {
                        return null;
                    }
                }

                public Text getDisplayName() {
                    if (leftIceBox.hasCustomName()) {
                        return leftIceBox.getDisplayName();
                    } else {
                        return rightIceBox.hasCustomName() ? rightIceBox.getDisplayName() : new TranslatableText("container.iceBoxDouble");
                    }
                }
            });
        }

        public Optional<NamedScreenHandlerFactory> getFrom(ChestBlockEntity iceBoxBlockEntity) {
            return Optional.of(iceBoxBlockEntity);
        }

        public Optional<NamedScreenHandlerFactory> getFallback() {
            return Optional.empty();
        }
    };

    public IceBoxBlock(Settings settings, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
        super(settings, supplier);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false));
    }

    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return this.getBlockEntitySource(state, world, pos, false).apply(NAME_RETRIEVER).orElse(null);
    }

    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Inventory invo = ChestBlock.getInventory(this, state, world, pos, true);
        if (invo != null) {
            List<FreezingRecipe> recipes = world.getRecipeManager().getAllMatches(FreezingRecipe.TYPE, invo, world);
            int recipePos = -1;
            if (recipes.size() > 0) {
                recipePos = random.nextInt(recipes.size());
            }

            if (recipePos >= 0) {
                recipes.get(recipePos).craft(invo);
                return;
            }

            if (random.nextFloat() < 0.05f) {
                invo.setStack(random.nextInt(invo.size()), new ItemStack(Items.ICE));
            }
        }
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new IceBoxBlockEntity(pos, state);
    }

    protected Stat<Identifier> getOpenStat() {
        return Stats.CUSTOM.getOrCreateStat(Baking.OPEN_ICE_BOX);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(LIT);
    }
}
