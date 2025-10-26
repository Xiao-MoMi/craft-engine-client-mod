package net.momirealms.craftengine.fabric.block;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
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

    public @NotNull VoxelShape getShape(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.getShape(blockGetter, blockPos);
    }
    public @NotNull VoxelShape getShape(BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return visualBlockState.getShape(blockGetter, blockPos, collisionContext);
    }
    public @NotNull VoxelShape getCollisionShape(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.getCollisionShape(blockGetter, blockPos);
    }
    public @NotNull VoxelShape getCollisionShape(BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return visualBlockState.getCollisionShape(blockGetter, blockPos, collisionContext);
    }
    public @NotNull VoxelShape getEntityInsideCollisionShape(BlockGetter blockGetter, BlockPos blockPos, Entity entity) {
        return visualBlockState.getEntityInsideCollisionShape(blockGetter, blockPos, entity);
    }
    public @NotNull VoxelShape getBlockSupportShape(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.getBlockSupportShape(blockGetter, blockPos);
    }
    public @NotNull VoxelShape getVisualShape(BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return visualBlockState.getVisualShape(blockGetter, blockPos, collisionContext);
    }
    public @NotNull VoxelShape getInteractionShape(BlockGetter blockGetter, BlockPos blockPos) {
        return visualBlockState.getInteractionShape(blockGetter, blockPos);
    }
}
