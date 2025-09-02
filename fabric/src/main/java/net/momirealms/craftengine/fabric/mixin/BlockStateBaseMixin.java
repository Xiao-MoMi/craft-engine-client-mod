package net.momirealms.craftengine.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.redstone.Orientation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.momirealms.craftengine.fabric.client.config.ModConfig.enableCancelBlockUpdate;
import static net.momirealms.craftengine.fabric.network.NetworkManager.serverInstalled;

@Environment(EnvType.CLIENT)
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {

    @Inject(method = "updateShape", at = @At("HEAD"), cancellable = true)
    private void cancelUpdateShape(LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, net.minecraft.world.level.block.state.BlockState blockState, RandomSource randomSource, CallbackInfoReturnable<BlockStateBaseMixin> cir) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        cir.setReturnValue(this);
    }

    @Inject(method = "handleNeighborChanged", at = @At("HEAD"), cancellable = true)
    private void cancelNeighborUpdate(Level level, BlockPos blockPos, net.minecraft.world.level.block.Block block, Orientation orientation, boolean bl, CallbackInfo ci) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        ci.cancel();
    }

    @Inject(method = "updateNeighbourShapes*", at = @At("HEAD"), cancellable = true)
    private void cancelUpdateNeighbors(LevelAccessor world, BlockPos pos, int flags, CallbackInfo ci) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        ci.cancel();
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void cancelScheduledTick(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        ci.cancel();
    }

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void passCanPlaceAt(LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        cir.setReturnValue(true);
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void cancelRandomTick(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        ci.cancel();
    }
}
