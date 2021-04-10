package dev.elexi.hugeblank.bagels_baking.mixin.entity;

import com.mojang.authlib.GameProfile;
import dev.elexi.hugeblank.bagels_baking.Baking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Calendar;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerTick extends PlayerEntity {
    @Shadow public abstract void resetStat(Stat<?> stat);

    @Shadow public abstract void increaseStat(Stat<?> stat, int amount);

    private int tick = 0;

    public PlayerTick(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(at = @At("HEAD"), method = "playerTick()V")
    private void playerTick(CallbackInfo ci) {
        tick++;
        if (tick%20 == 0) {
            Stat<Identifier> day = Stats.CUSTOM.getOrCreateStat(Baking.DAY_OF_WEEK, StatFormatter.DEFAULT);
            this.resetStat(day);
            this.increaseStat(day, Calendar.DAY_OF_WEEK);
            tick = 0;
        }
    }
}
