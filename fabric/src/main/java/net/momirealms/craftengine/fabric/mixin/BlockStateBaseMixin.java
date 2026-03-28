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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.momirealms.craftengine.fabric.config.ModConfig;
import net.momirealms.craftengine.fabric.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {

    @Inject(method = "updateShape", at = @At("HEAD"), cancellable = true)
    private void ce$cancelUpdateShape(Direction direction, BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2, CallbackInfoReturnable<BlockStateBaseMixin> cir) {
        if (!ModConfig.INSTANCE.enableCancelBlockUpdate() || !NetworkManager.instance().serverInstalled()) return;
        cir.setReturnValue(this);
    }

    @Inject(method = "handleNeighborChanged", at = @At("HEAD"), cancellable = true)
    private void ce$cancelNeighborUpdate(Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl, CallbackInfo ci) {
        if (!ModConfig.INSTANCE.enableCancelBlockUpdate() || !NetworkManager.instance().serverInstalled()) return;
        ci.cancel();
    }

    @Inject(method = "updateNeighbourShapes*", at = @At("HEAD"), cancellable = true)
    private void ce$cancelUpdateNeighbors(LevelAccessor world, BlockPos pos, int flags, CallbackInfo ci) {
        if (!ModConfig.INSTANCE.enableCancelBlockUpdate() || !NetworkManager.instance().serverInstalled()) return;
        ci.cancel();
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void ce$cancelScheduledTick(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        if (!ModConfig.INSTANCE.enableCancelBlockUpdate() || !NetworkManager.instance().serverInstalled()) return;
        ci.cancel();
    }

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void ce$passCanPlaceAt(LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (!ModConfig.INSTANCE.enableCancelBlockUpdate() || !NetworkManager.instance().serverInstalled()) return;
        cir.setReturnValue(true);
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void ce$cancelRandomTick(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        if (!ModConfig.INSTANCE.enableCancelBlockUpdate() || !NetworkManager.instance().serverInstalled()) return;
        ci.cancel();
    }
}
