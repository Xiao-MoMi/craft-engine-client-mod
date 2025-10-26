package net.momirealms.craftengine.fabric.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.momirealms.craftengine.fabric.client.block.CraftEngineBlockClientProperties;
import net.momirealms.craftengine.fabric.mixin.BlockAccessor;
import net.momirealms.craftengine.fabric.mixin.PropertiesAccessor;

@Environment(EnvType.CLIENT)
public class CraftEngineBlock extends Block implements CraftEngineBlockClientProperties {

    public CraftEngineBlock(Properties properties) {
        super(properties);
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
