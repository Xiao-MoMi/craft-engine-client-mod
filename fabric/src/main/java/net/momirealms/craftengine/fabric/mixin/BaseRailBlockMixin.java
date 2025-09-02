package net.momirealms.craftengine.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.momirealms.craftengine.fabric.client.config.ModConfig.enableCancelBlockUpdate;
import static net.momirealms.craftengine.fabric.network.NetworkManager.serverInstalled;

@Environment(EnvType.CLIENT)
@Mixin(BaseRailBlock.class)
public abstract class BaseRailBlockMixin {

    @Inject(method = "updateState(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Z)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("HEAD"), cancellable = true)
    private void cancelUpdateCurves(BlockState blockState, Level level, BlockPos blockPos, boolean bl, CallbackInfoReturnable<BlockState> cir) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        cir.setReturnValue(blockState);
    }

    @Inject(method = "updateState(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;)V", at = @At("HEAD"), cancellable = true)
    private void cancelUpdateCurves(BlockState blockState, Level level, BlockPos blockPos, Block block, CallbackInfo ci) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        ci.cancel();
    }
}
