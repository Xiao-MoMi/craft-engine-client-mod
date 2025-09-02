package net.momirealms.craftengine.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.momirealms.craftengine.fabric.client.config.ModConfig.enableCancelBlockUpdate;
import static net.momirealms.craftengine.fabric.network.NetworkManager.serverInstalled;

@Environment(EnvType.CLIENT)
@Mixin(FluidState.class)
public class FluidStateMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void cancelScheduledTick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        ci.cancel();
    }
}
