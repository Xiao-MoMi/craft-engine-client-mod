package net.momirealms.craftengine.fabric.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.momirealms.craftengine.fabric.client.block.CraftEngineBlockClientProperties;
import net.momirealms.craftengine.fabric.util.Reflections;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class CraftEngineBlock extends Block implements CraftEngineBlockClientProperties {
    private final ResourceLocation replacedBlock;
    private final Block ownerBlock;
    private final BlockState clientSideBlockState;

    public CraftEngineBlock(Properties properties, ResourceLocation replacedBlock, Block ownerBlock, BlockState clientSideBlockState) {
        super(properties);
        this.replacedBlock = replacedBlock;
        this.ownerBlock = ownerBlock;
        this.clientSideBlockState = clientSideBlockState;
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return clientSideBlockState.getShape(blockGetter, blockPos, collisionContext);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return clientSideBlockState.getCollisionShape(blockGetter, blockPos, collisionContext);
    }

    @Override
    protected @NotNull VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return clientSideBlockState.getBlockSupportShape(blockGetter, blockPos);
    }

    public static Block generateBlock(ResourceLocation replacedBlock, Block ownerBlock, BlockState clientSideBlockState, BlockBehaviour.Properties properties) throws ReflectiveOperationException {
        BlockBehaviour.Properties ownerProperties = ownerBlock.properties();
        Reflections.field$BlockBehaviour$Properties$hasCollision.set(properties, Reflections.field$BlockBehaviour$Properties$hasCollision.get(ownerProperties));
        if (!clientSideBlockState.canOcclude()) properties.noOcclusion();
        properties.pushReaction(clientSideBlockState.getPistonPushReaction())
                .destroyTime(clientSideBlockState.getDestroySpeed(null, null))
                .strength(clientSideBlockState.getDestroySpeed(null, null));
        CraftEngineBlock newBlockInstance = new CraftEngineBlock(properties, replacedBlock, ownerBlock, clientSideBlockState);
        StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(newBlockInstance);
        StateDefinition<Block, BlockState> stateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, CraftEngineStateFactory.INSTANCE);
        Reflections.field$Block$stateDefinition.set(newBlockInstance, stateDefinition);
        Reflections.field$Block$defaultBlockState.set(newBlockInstance, stateDefinition.getPossibleStates().getFirst());
        return newBlockInstance;
    }

    @Override
    public RenderType chunkSectionLayer() {
        return ItemBlockRenderTypes.getChunkRenderType(this.ownerBlock.defaultBlockState());
    }

    @Override
    public boolean hasTints() {
        return replacedBlock.getPath().contains("leaves");
    }
}
