package net.momirealms.craftengine.neoforge.client.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import org.jetbrains.annotations.NotNull;

public class CustomBlock extends Block {
    private final VoxelShape outlineShape;
    private final VoxelShape collisionShape;
    private final boolean isTransparent;
    private final VoxelShape[] occlusionShapesByFace;

    public CustomBlock(BlockBehaviour.Properties settings, VoxelShape outlineShape, VoxelShape collisionShape, boolean isTransparent, VoxelShape[] occlusionShapesByFace) {
        super(settings);
        this.outlineShape = outlineShape;
        this.collisionShape = collisionShape;
        this.isTransparent = isTransparent;
        this.occlusionShapesByFace = occlusionShapesByFace;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.outlineShape;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.collisionShape;
    }

    public boolean isTransparent() {
        return this.isTransparent;
    }

    public VoxelShape[] occlusionShapesByFace() {
        return this.occlusionShapesByFace;
    }
}
