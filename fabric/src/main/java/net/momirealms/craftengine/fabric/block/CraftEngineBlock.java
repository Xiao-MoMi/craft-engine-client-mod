package net.momirealms.craftengine.fabric.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
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
import net.momirealms.craftengine.fabric.mixin.BlockBehaviourInvoker;
import net.momirealms.craftengine.fabric.mixin.PropertiesAccessor;
import net.momirealms.craftengine.fabric.util.BlockStateUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
    protected @NotNull VoxelShape getOcclusionShape(BlockState blockState) {
        if (visualBlock == this) return super.getOcclusionShape(blockState);
        return ((BlockBehaviourInvoker) visualBlock).ce$getOcclusionShape(BlockStateUtils.remap(blockState));
    }

    @Override
    protected @NotNull VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (visualBlock == this) return super.getBlockSupportShape(blockState, blockGetter, blockPos);
        return ((BlockBehaviourInvoker) visualBlock).ce$getBlockSupportShape(BlockStateUtils.remap(blockState), blockGetter, blockPos);
    }

    @Override
    protected @NotNull VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (visualBlock == this) return super.getInteractionShape(blockState, blockGetter, blockPos);
        return ((BlockBehaviourInvoker) visualBlock).ce$getInteractionShape(BlockStateUtils.remap(blockState), blockGetter, blockPos);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (visualBlock == this) return super.getShape(blockState, blockGetter, blockPos, collisionContext);
        return ((BlockBehaviourInvoker) visualBlock).ce$getShape(BlockStateUtils.remap(blockState), blockGetter, blockPos, collisionContext);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (visualBlock == this) return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
        return ((BlockBehaviourInvoker) visualBlock).ce$getCollisionShape(BlockStateUtils.remap(blockState), blockGetter, blockPos, collisionContext);
    }

    @Override
    protected @NotNull VoxelShape getEntityInsideCollisionShape(BlockState blockState, Level level, BlockPos blockPos) {
        if (visualBlock == this) return super.getEntityInsideCollisionShape(blockState, level, blockPos);
        return ((BlockBehaviourInvoker) visualBlock).ce$getEntityInsideCollisionShape(BlockStateUtils.remap(blockState), level, blockPos);
    }

    @Override
    protected @NotNull VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (visualBlock == this) return super.getVisualShape(blockState, blockGetter, blockPos, collisionContext);
        return ((BlockBehaviourInvoker) visualBlock).ce$getVisualShape(BlockStateUtils.remap(blockState), blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        if (visualBlock == this) return super.getCloneItemStack(levelReader, blockPos, blockState);
        return visualBlock.getCloneItemStack(levelReader, blockPos, BlockStateUtils.remap(blockState));
    }
    // BlockBehaviour end

    // SimpleWaterloggedBlock start
    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        if (visualBlock == this || !(visualBlock instanceof SimpleWaterloggedBlock simpleWaterloggedBlock)) return false;
        return simpleWaterloggedBlock.canPlaceLiquid(player, blockGetter, blockPos, BlockStateUtils.remap(blockState), fluid);
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        if (visualBlock == this || !(visualBlock instanceof SimpleWaterloggedBlock simpleWaterloggedBlock)) return false;
        return simpleWaterloggedBlock.placeLiquid(levelAccessor, blockPos, BlockStateUtils.remap(blockState), fluidState);
    }

    @Override
    public @NotNull ItemStack pickupBlock(@Nullable Player player, LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        if (visualBlock == this || !(visualBlock instanceof SimpleWaterloggedBlock simpleWaterloggedBlock)) return ItemStack.EMPTY;
        return simpleWaterloggedBlock.pickupBlock(player, levelAccessor, blockPos, BlockStateUtils.remap(blockState));
    }

    @Override
    public @NotNull Optional<SoundEvent> getPickupSound() {
        if (visualBlock == this || !(visualBlock instanceof SimpleWaterloggedBlock simpleWaterloggedBlock)) return Optional.empty();
        return simpleWaterloggedBlock.getPickupSound();
    }
    // SimpleWaterloggedBlock end

    // BonemealableBlock start
    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        if (visualBlock == this || !(visualBlock instanceof BonemealableBlock bonemealableBlock)) return false;
        return bonemealableBlock.isValidBonemealTarget(levelReader, blockPos, BlockStateUtils.remap(blockState));
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

    @Override
    public @NotNull BlockPos getParticlePos(BlockPos blockPos) {
        if (visualBlock == this || !(visualBlock instanceof BonemealableBlock particleBlock)) return blockPos;
        return particleBlock.getParticlePos(blockPos);
    }

    @Override
    public @NotNull Type getType() {
        if (visualBlock == this || !(visualBlock instanceof BonemealableBlock particleBlock)) return BonemealableBlock.Type.GROWER;
        return particleBlock.getType();
    }
    // BonemealableBlock end

    public static CraftEngineBlock generateBlock(ResourceLocation blockId) {
        CraftEngineBlock newBlockInstance = new CraftEngineBlock(createEmptyBlockProperties(blockId));
        StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(newBlockInstance);
        StateDefinition<Block, BlockState> stateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, CraftEngineStateFactory.INSTANCE);
        BlockAccessor blockAccessor = (BlockAccessor) newBlockInstance;
        blockAccessor.ce$setStateDefinition(stateDefinition);
        blockAccessor.ce$setDefaultBlockState(stateDefinition.getPossibleStates().getFirst());
        return newBlockInstance;
    }

    private static Properties createEmptyBlockProperties(ResourceLocation id) {
        Properties blockProperties = Properties.of();
        ResourceKey<Block> resourceKey = ResourceKey.create(Registries.BLOCK, id);
        ((PropertiesAccessor) blockProperties).ce$setId(resourceKey);
        return blockProperties;
    }
}
