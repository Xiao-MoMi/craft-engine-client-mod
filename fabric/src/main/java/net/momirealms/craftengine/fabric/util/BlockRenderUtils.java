package net.momirealms.craftengine.fabric.util;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class BlockRenderUtils {
    private BlockRenderUtils() {}

    public static void registerRenderLayer(Block block, BlockState vanillaState) {
        BlockRenderLayerMap.INSTANCE.putBlock(
                block,
                vanillaState.getBlock() instanceof LeavesBlock
                        ? RenderType.cutoutMipped()
                        : ItemBlockRenderTypes.getChunkRenderType(vanillaState)
        );
    }

    public static void registerColor(Block block, Block vanillaBlock) {
        BlockColor blockColor = ColorProviderRegistry.BLOCK.get(vanillaBlock);
        if (blockColor == null) return;
        ColorProviderRegistry.BLOCK.register(CustomBlockColor.INSTANCE, block);
    }

    public static final class CustomBlockColor implements BlockColor {
        public static final CustomBlockColor INSTANCE = new CustomBlockColor();

        @Override
        public int getColor(BlockState blockState, @Nullable BlockAndTintGetter blockAndTintGetter, @Nullable BlockPos blockPos, int i) {
            if (blockAndTintGetter != null && blockPos != null) {
                return BiomeColors.getAverageFoliageColor(blockAndTintGetter, blockPos);
            }
            return FoliageColor.getDefaultColor();
        }
    }
}
