package net.momirealms.craftengine.fabric.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class CraftEngineBlockState extends BlockState {
    private BlockState visualBlockState = Blocks.STONE.defaultBlockState();

    public CraftEngineBlockState(Block block, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec) {
        super(block, immutableMap, mapCodec);
    }

    public void setVisualBlockState(BlockState visualBlockState) {
        this.visualBlockState = visualBlockState;
    }

    @Override
    public @NotNull VoxelShape getFaceOcclusionShape(BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return visualBlockState.getFaceOcclusionShape(blockGetter, blockPos, direction);
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.getOcclusionShape(blockGetter, blockPos);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.getShape(blockGetter, blockPos);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return visualBlockState.getShape(blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.getCollisionShape(blockGetter, blockPos);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return visualBlockState.getCollisionShape(blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull VoxelShape getBlockSupportShape(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.getBlockSupportShape(blockGetter, blockPos);
    }

    @Override
    public @NotNull VoxelShape getVisualShape(BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return visualBlockState.getVisualShape(blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.getInteractionShape(blockGetter, blockPos);
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return visualBlockState.use(level, player, interactionHand, blockHitResult);
    }

    @Override
    public boolean isSolidRender(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.isSolidRender(blockGetter, blockPos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.propagatesSkylightDown(blockGetter, blockPos);
    }

    @Override
    public int getLightBlock(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.getLightBlock(blockGetter, blockPos);
    }

    @Override
    public boolean isValidSpawn(BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return visualBlockState.isValidSpawn(blockGetter, blockPos, entityType);
    }

    @Override
    public boolean emissiveRendering(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.emissiveRendering(blockGetter, blockPos);
    }

    @Override
    public boolean isRedstoneConductor(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.isRedstoneConductor(blockGetter, blockPos);
    }

    @Override
    public boolean isSuffocating(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.isSuffocating(blockGetter, blockPos);
    }

    @Override
    public boolean isViewBlocking(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.isViewBlocking(blockGetter, blockPos);
    }

    @Override
    public boolean hasPostProcess(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.hasPostProcess(blockGetter, blockPos);
    }

    @Override
    public @NotNull Vec3 getOffset(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.getOffset(blockGetter, blockPos);
    }
}
