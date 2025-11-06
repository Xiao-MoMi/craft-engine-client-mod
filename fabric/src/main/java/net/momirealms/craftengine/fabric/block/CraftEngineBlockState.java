package net.momirealms.craftengine.fabric.block;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

    public CraftEngineBlockState(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> reference2ObjectArrayMap, MapCodec<BlockState> mapCodec) {
        super(block, reference2ObjectArrayMap, mapCodec);
    }

    public void setVisualBlockState(BlockState visualBlockState) {
        this.visualBlockState = visualBlockState;
    }

    @Override
    public @NotNull VoxelShape getFaceOcclusionShape(Direction direction) {
        return visualBlockState.getFaceOcclusionShape(direction);
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape() {
        return visualBlockState.getOcclusionShape();
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
    public @NotNull VoxelShape getEntityInsideCollisionShape(BlockGetter blockGetter, BlockPos blockPos, Entity entity) {
        return visualBlockState.getEntityInsideCollisionShape(blockGetter, blockPos, entity);
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
    public @NotNull InteractionResult useItemOn(ItemStack itemStack, Level level, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return visualBlockState.useItemOn(itemStack, level, player, interactionHand, blockHitResult);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(Level level, Player player, BlockHitResult blockHitResult) {
        return visualBlockState.useWithoutItem(level, player, blockHitResult);
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
    public @NotNull Vec3 getOffset(BlockPos blockPos) {
        return visualBlockState.getOffset(blockPos);
    }
}
