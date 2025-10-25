package net.momirealms.craftengine.fabric.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.momirealms.craftengine.fabric.client.block.CraftEngineBlockClientProperties;
import net.momirealms.craftengine.fabric.mixin.BlockAccessor;
import net.momirealms.craftengine.fabric.mixin.PropertiesAccessor;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class CraftEngineBlock extends Block implements CraftEngineBlockClientProperties {

    public CraftEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        try {
            int[] mappings = BlockManager.instance().mappings();
            if (mappings == null) return super.getShape(blockState, blockGetter, blockPos, collisionContext);
            int id = Block.BLOCK_STATE_REGISTRY.getId(blockState);
            int mapping = mappings[id];
            BlockState visualState = Block.BLOCK_STATE_REGISTRY.byId(mapping);
            if (visualState == null) return super.getShape(blockState, blockGetter, blockPos, collisionContext);
            return visualState.getShape(blockGetter, blockPos, collisionContext);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return super.getShape(blockState, blockGetter, blockPos, collisionContext);
        }
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        try {
            int[] mappings = BlockManager.instance().mappings();
            if (mappings == null) return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
            int id = Block.BLOCK_STATE_REGISTRY.getId(blockState);
            int mapping = mappings[id];
            BlockState visualState = Block.BLOCK_STATE_REGISTRY.byId(mapping);
            if (visualState == null) return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
            return visualState.getCollisionShape(blockGetter, blockPos, collisionContext);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
        }
    }

    @Override
    protected @NotNull VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        try {
            int[] mappings = BlockManager.instance().mappings();
            if (mappings == null) return super.getBlockSupportShape(blockState, blockGetter, blockPos);
            int id = Block.BLOCK_STATE_REGISTRY.getId(blockState);
            int mapping = mappings[id];
            BlockState visualState = Block.BLOCK_STATE_REGISTRY.byId(mapping);
            if (visualState == null) return super.getBlockSupportShape(blockState, blockGetter, blockPos);
            return visualState.getBlockSupportShape(blockGetter, blockPos);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return super.getBlockSupportShape(blockState, blockGetter, blockPos);
        }
    }

    public static CraftEngineBlock generateBlock(ResourceLocation blockId) {
        CraftEngineBlock newBlockInstance = new CraftEngineBlock(createEmptyBlockProperties(blockId));
        StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(newBlockInstance);
        StateDefinition<Block, BlockState> stateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, CraftEngineStateFactory.INSTANCE);
        BlockAccessor blockAccessor = (BlockAccessor) newBlockInstance;
        blockAccessor.setStateDefinition(stateDefinition);
        blockAccessor.setDefaultBlockState(stateDefinition.getPossibleStates().getFirst());
        return newBlockInstance;
    }

    private static Properties createEmptyBlockProperties(ResourceLocation id) {
        Properties blockProperties = Properties.of();
        ResourceKey<Block> resourceKey = ResourceKey.create(Registries.BLOCK, id);
        ((PropertiesAccessor) blockProperties).setId(resourceKey);
        return blockProperties;
    }
}
