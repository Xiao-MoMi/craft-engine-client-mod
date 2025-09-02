package net.momirealms.craftengine.fabric.client.block;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;

public interface CraftEngineBlockClientProperties {

    RenderType chunkSectionLayer();

    boolean hasTints();

    static void registerRenderLayer() {
        ItemBlockRenderTypes.setFancy(true);
        for (Block block : BuiltInRegistries.BLOCK) {
            if (!(block instanceof CraftEngineBlockClientProperties craftEngineBlock)) continue;
            BlockRenderLayerMap.INSTANCE.putBlock(block, craftEngineBlock.chunkSectionLayer());
            if (craftEngineBlock.hasTints()) registerColor(block);
        }
    }

    static void registerColor(Block block) {
        ColorProviderRegistry.BLOCK.register(
                (state, world, pos, tintIndex) -> {
                    if (world != null && pos != null) {
                        return BiomeColors.getAverageFoliageColor(world, pos);
                    }
                    return FoliageColor.getDefaultColor();
                }, block
        );
    }
}
