package net.momirealms.craftengine.neoforge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.momirealms.craftengine.neoforge.client.blocks.CustomBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.momirealms.craftengine.neoforge.client.CraftEngineNeoForgeModClient.serverInstalled;
import static net.momirealms.craftengine.neoforge.client.config.ModConfig.enableCancelBlockUpdate;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {

    @Shadow private VoxelShape[] occlusionShapesByFace;
    @Shadow @Final private static VoxelShape[] FULL_BLOCK_OCCLUSION_SHAPES;

    @Shadow public abstract Block getBlock();

    @Inject(method = "updateShape", at = @At("HEAD"), cancellable = true)
    private void cancelUpdateShape(LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, net.minecraft.world.level.block.state.BlockState neighborState, RandomSource random, CallbackInfoReturnable<BlockStateBaseMixin> cir) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        cir.setReturnValue(this);
    }

    @Inject(method = "handleNeighborChanged", at = @At("HEAD"), cancellable = true)
    private void cancelHandleNeighborChanged(Level level, BlockPos pos, net.minecraft.world.level.block.Block neighborBlock, Orientation orientation, boolean movedByPiston, CallbackInfo ci) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        ci.cancel();
    }

    @Inject(method = "updateNeighbourShapes*", at = @At("HEAD"), cancellable = true)
    private void cancelUpdateNeighbourShapes(LevelAccessor level, BlockPos pos, int flags, CallbackInfo ci) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        ci.cancel();
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void cancelTick(ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        ci.cancel();
    }

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void passCanSurvive(LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        cir.setReturnValue(true);
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void cancelRandomTick(ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        ci.cancel();
    }

    @Inject(method = "getFaceOcclusionShape", at = @At("HEAD"))
    private void initGetFaceOcclusionShape(Direction direction, CallbackInfoReturnable<VoxelShape> cir) {
        if (this.occlusionShapesByFace == null && this.getBlock() instanceof CustomBlock customBlock) {
            this.occlusionShapesByFace = customBlock.occlusionShapesByFace();
        }
    }
}
