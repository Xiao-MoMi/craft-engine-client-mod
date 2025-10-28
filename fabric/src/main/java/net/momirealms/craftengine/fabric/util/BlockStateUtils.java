package net.momirealms.craftengine.fabric.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.momirealms.craftengine.fabric.block.BlockManager;
import net.momirealms.craftengine.fabric.block.CraftEngineBlockState;

@Environment(EnvType.CLIENT)
public final class BlockStateUtils {
    private static int vanillaStateSize;
    private static boolean hasInit;

    public static void init(int size) {
        if (hasInit) {
            throw new IllegalStateException("BlockStateUtils has already been initialized");
        }
        vanillaStateSize = size;
        hasInit = true;
    }

    public static int vanillaStateSize() {
        return vanillaStateSize;
    }

    public static boolean isVanillaBlock(BlockState state) {
        return !(state instanceof CraftEngineBlockState);
    }

    public static boolean isVanillaBlock(int id) {
        return id < vanillaStateSize && id >= 0;
    }

    public static BlockState remap(BlockState state) {
        if (isVanillaBlock(state)) return state;
        int id = Block.BLOCK_STATE_REGISTRY.getId(state);
        int remapped = BlockManager.instance().remapState(id);
        return Block.BLOCK_STATE_REGISTRY.byId(remapped);
    }
}
