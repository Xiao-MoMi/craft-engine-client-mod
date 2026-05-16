package net.momirealms.craftengine.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(BlockBehaviour.class)
public interface BlockBehaviourInvoker {

    @Invoker("getOcclusionShape")
    VoxelShape ce$getOcclusionShape(BlockState blockState);

    @Invoker("getBlockSupportShape")
    VoxelShape ce$getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos);

    @Invoker("getInteractionShape")
    VoxelShape ce$getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos);

    @Invoker("getShape")
    VoxelShape ce$getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext);

    @Invoker("getCollisionShape")
    VoxelShape ce$getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext);

    @Invoker("getEntityInsideCollisionShape")
    VoxelShape ce$getEntityInsideCollisionShape(BlockState blockState, Level level, BlockPos blockPos);

    @Invoker("getVisualShape")
    VoxelShape ce$getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext);

}
