package net.momirealms.craftengine.fabric.block;

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

    public CraftEngineBlockState(Block owner, Property<?>[] propertyKeys, Comparable<?>[] propertyValues) {
        super(owner, propertyKeys, propertyValues);
    }

    public void setVisualBlockState(BlockState visualBlockState) {
        this.visualBlockState = visualBlockState;
    }

    @Override
    public @NotNull VoxelShape getFaceOcclusionShape(@NotNull Direction direction) {
        return visualBlockState.getFaceOcclusionShape(direction);
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape() {
        return visualBlockState.getOcclusionShape();
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return visualBlockState.getShape(blockGetter, blockPos);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return visualBlockState.getShape(blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return visualBlockState.getCollisionShape(blockGetter, blockPos);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return visualBlockState.getCollisionShape(blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull VoxelShape getEntityInsideCollisionShape(@NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull Entity entity) {
        return visualBlockState.getEntityInsideCollisionShape(blockGetter, blockPos, entity);
    }

    @Override
    public @NotNull VoxelShape getBlockSupportShape(@NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return visualBlockState.getBlockSupportShape(blockGetter, blockPos);
    }

    @Override
    public @NotNull VoxelShape getVisualShape(@NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return visualBlockState.getVisualShape(blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(@NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return visualBlockState.getInteractionShape(blockGetter, blockPos);
    }

    @Override
    public @NotNull InteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        return visualBlockState.useItemOn(itemStack, level, player, interactionHand, blockHitResult);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull Level level, @NotNull Player player, @NotNull BlockHitResult blockHitResult) {
        return visualBlockState.useWithoutItem(level, player, blockHitResult);
    }

    @Override
    public boolean isValidSpawn(@NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull EntityType<?> entityType) {
        return visualBlockState.isValidSpawn(blockGetter, blockPos, entityType);
    }

    @Override
    public @NotNull Vec3 getOffset(@NotNull BlockPos blockPos) {
        return visualBlockState.getOffset(blockPos);
    }

    @Override
    public <T extends Comparable<T>> T getValue(@NotNull Property<@NotNull T> property) {
        try {
            return visualBlockState.getValue(property);
        } catch (Throwable e) {
            return property.getPossibleValues().getFirst();
        }
    }

    @Override
    public <T extends Comparable<T>, V extends T> @NotNull BlockState setValue(@NotNull Property<@NotNull T> property, V comparable) {
        return this;
    }
}
