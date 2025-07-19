package net.momirealms.craftengine.neoforge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.momirealms.craftengine.neoforge.client.CraftEngineNeoForgeModClient.serverInstalled;
import static net.momirealms.craftengine.neoforge.client.config.ModConfig.enableCancelBlockUpdate;

@Mixin(FluidState.class)
public class FluidStateMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void cancelTick(ServerLevel level, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        ci.cancel();
    }
}
