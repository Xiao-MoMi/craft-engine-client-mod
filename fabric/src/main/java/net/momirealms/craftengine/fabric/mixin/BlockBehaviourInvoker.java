package net.momirealms.craftengine.fabric.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockBehaviour.class)
public interface BlockBehaviourInvoker {

    @Invoker("getOcclusionShape")
    VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos);

    @Invoker("getBlockSupportShape")
    VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos);

    @Invoker("getInteractionShape")
    VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos);

    @Invoker("getShape")
    VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext);

    @Invoker("getCollisionShape")
    VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext);

    @Invoker("getVisualShape")
    VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext);

    @Invoker("getFluidState")
    FluidState getFluidState(BlockState blockState);

}
