package net.momirealms.craftengine.fabric.client.block;

import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public interface CraftEngineBlockClientProperties {

    static void registerRenderLayer(Block block, BlockState vanillaState) {
        BlockRenderLayerMap.putBlock(
                block,
                vanillaState.getBlock() instanceof LeavesBlock
                        ? ChunkSectionLayer.CUTOUT_MIPPED
                        : ItemBlockRenderTypes.getChunkRenderType(vanillaState)
        );
    }

    static void registerColor(Block block) {
        ColorProviderRegistry.BLOCK.register(
                (state, world, pos, tintIndex) -> {
                    if (world != null && pos != null) {
                        return BiomeColors.getAverageFoliageColor(world, pos);
                    }
                    return FoliageColor.FOLIAGE_DEFAULT;
                }, block
        );
    }
}
