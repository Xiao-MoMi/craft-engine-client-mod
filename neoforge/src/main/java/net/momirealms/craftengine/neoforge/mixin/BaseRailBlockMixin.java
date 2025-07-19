package net.momirealms.craftengine.neoforge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.momirealms.craftengine.neoforge.client.CraftEngineNeoForgeModClient.serverInstalled;
import static net.momirealms.craftengine.neoforge.client.config.ModConfig.enableCancelBlockUpdate;

@Mixin(BaseRailBlock.class)
public abstract class BaseRailBlockMixin {

    @Inject(
            method = "updateState(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Z)Lnet/minecraft/world/level/block/state/BlockState;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelUpdateState(BlockState state, Level level, BlockPos pos, boolean movedByPiston, CallbackInfoReturnable<BlockState> cir) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        cir.setReturnValue(state);
    }
}
