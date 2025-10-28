package net.momirealms.craftengine.fabric.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.momirealms.craftengine.fabric.mixin.BlockAccessor;
import net.momirealms.craftengine.fabric.util.BlockStateUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@SuppressWarnings("deprecation")
@Environment(EnvType.CLIENT)
public class CraftEngineBlock extends Block implements BonemealableBlock, SimpleWaterloggedBlock {
    private Block visualBlock = this;

    public CraftEngineBlock(Properties properties) {
        super(properties);
    }

    public void setVisualBlock(Block visualBlock) {
        this.visualBlock = visualBlock;
    }

    // BlockBehaviour start
    @Override
    public @NotNull VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (visualBlock == this) return super.getOcclusionShape(blockState, blockGetter, blockPos);
        return visualBlock.getOcclusionShape(BlockStateUtils.remap(blockState), blockGetter, blockPos);
    }

    @Override
    public @NotNull VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (visualBlock == this) return super.getBlockSupportShape(blockState, blockGetter, blockPos);
        return visualBlock.getBlockSupportShape(BlockStateUtils.remap(blockState), blockGetter, blockPos);
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (visualBlock == this) return super.getInteractionShape(blockState, blockGetter, blockPos);
        return visualBlock.getInteractionShape(BlockStateUtils.remap(blockState), blockGetter, blockPos);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (visualBlock == this) return super.getShape(blockState, blockGetter, blockPos, collisionContext);
        return visualBlock.getShape(BlockStateUtils.remap(blockState), blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (visualBlock == this) return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
        return visualBlock.getCollisionShape(BlockStateUtils.remap(blockState), blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (visualBlock == this) return super.getVisualShape(blockState, blockGetter, blockPos, collisionContext);
        return visualBlock.getVisualShape(BlockStateUtils.remap(blockState), blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState blockState) {
        if (visualBlock == this) return super.getFluidState(blockState);
        return visualBlock.getFluidState(BlockStateUtils.remap(blockState));
    }
    // BlockBehaviour end

    // SimpleWaterloggedBlock start
    @Override
    public boolean canPlaceLiquid(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        if (visualBlock == this || !(visualBlock instanceof SimpleWaterloggedBlock simpleWaterloggedBlock)) return false;
        return simpleWaterloggedBlock.canPlaceLiquid(blockGetter, blockPos, BlockStateUtils.remap(blockState), fluid);
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        if (visualBlock == this || !(visualBlock instanceof SimpleWaterloggedBlock simpleWaterloggedBlock)) return false;
        return simpleWaterloggedBlock.placeLiquid(levelAccessor, blockPos, BlockStateUtils.remap(blockState), fluidState);
    }

    @Override
    public @NotNull ItemStack pickupBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        if (visualBlock == this || !(visualBlock instanceof SimpleWaterloggedBlock simpleWaterloggedBlock)) return ItemStack.EMPTY;
        return simpleWaterloggedBlock.pickupBlock(levelAccessor, blockPos, BlockStateUtils.remap(blockState));
    }

    @Override
    public @NotNull Optional<SoundEvent> getPickupSound() {
        if (visualBlock == this || !(visualBlock instanceof SimpleWaterloggedBlock simpleWaterloggedBlock)) return Optional.empty();
        return simpleWaterloggedBlock.getPickupSound();
    }
    // SimpleWaterloggedBlock end

    // BonemealableBlock start
    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
        if (visualBlock == this || !(visualBlock instanceof BonemealableBlock bonemealableBlock)) return false;
        return bonemealableBlock.isValidBonemealTarget(levelReader, blockPos, BlockStateUtils.remap(blockState), bl);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        if (visualBlock == this || !(visualBlock instanceof BonemealableBlock bonemealableBlock)) return false;
        return bonemealableBlock.isBonemealSuccess(level, randomSource, blockPos, BlockStateUtils.remap(blockState));
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        if (visualBlock == this || !(visualBlock instanceof BonemealableBlock bonemealableBlock)) return;
        bonemealableBlock.performBonemeal(serverLevel, randomSource, blockPos, BlockStateUtils.remap(blockState));
    }
    // BonemealableBlock end

    public static CraftEngineBlock generateBlock(ResourceLocation blockId) {
        CraftEngineBlock newBlockInstance = new CraftEngineBlock(createEmptyBlockProperties(blockId));
        StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(newBlockInstance);
        StateDefinition<Block, BlockState> stateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, CraftEngineStateFactory.INSTANCE);
        BlockAccessor blockAccessor = (BlockAccessor) newBlockInstance;
        blockAccessor.setStateDefinition(stateDefinition);
        blockAccessor.setDefaultBlockState(stateDefinition.getPossibleStates().get(0));
        return newBlockInstance;
    }

    @SuppressWarnings("unused")
    private static Properties createEmptyBlockProperties(ResourceLocation id) {
        return Properties.of();
    }
}
