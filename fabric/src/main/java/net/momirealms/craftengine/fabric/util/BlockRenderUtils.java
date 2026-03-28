package net.momirealms.craftengine.fabric.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class BlockRenderUtils {
    private BlockRenderUtils() {}

    public static void registerColor(Block block, BlockState vanillaState) {
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        List<BlockTintSource> tintSources = blockColors.getTintSources(vanillaState);
        if (tintSources.isEmpty()) return;
        blockColors.register(tintSources, block);
    }
}
