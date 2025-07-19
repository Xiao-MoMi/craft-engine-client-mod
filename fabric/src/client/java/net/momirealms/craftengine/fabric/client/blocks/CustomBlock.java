package net.momirealms.craftengine.fabric.client.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class CustomBlock extends Block {
    private final VoxelShape outlineShape;
    private final VoxelShape collisionShape;
    private final boolean isTransparent;

    public CustomBlock(Settings settings, VoxelShape outlineShape, VoxelShape collisionShape, boolean isTransparent) {
        super(settings);
        this.outlineShape = outlineShape;
        this.collisionShape = collisionShape;
        this.isTransparent = isTransparent;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.outlineShape;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.collisionShape;
    }

    public boolean isTransparent() {
        return this.isTransparent;
    }
}
