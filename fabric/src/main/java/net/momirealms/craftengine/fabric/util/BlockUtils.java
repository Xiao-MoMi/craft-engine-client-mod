package net.momirealms.craftengine.fabric.util;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

import static java.util.Objects.requireNonNull;

public class BlockUtils {
    private final static Field COLLIDABLE_FIELD = requireNonNull(getDeclaredField(AbstractBlock.Settings.class, boolean.class, 0));

    @Nullable
    public static Field getDeclaredField(final Class<?> clazz, final Class<?> type, int index) {
        int i = 0;
        for (final Field field : clazz.getDeclaredFields()) {
            if (field.getType() == type) {
                if (index == i) {
                    return setAccessible(field);
                }
                i++;
            }
        }
        return null;
    }

    @NotNull
    public static <T extends AccessibleObject> T setAccessible(@NotNull final T o) {
        o.setAccessible(true);
        return o;
    }

    public static boolean canPassThrough(BlockState state) {
        try {
            if (state == null) return false;
            AbstractBlock.Settings settings = state.getBlock().getSettings();
            return !COLLIDABLE_FIELD.getBoolean(settings);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access 'collidable' field", e);
        }
    }

    public static VoxelShape getShape(BlockState state) {
        if (state == null) return VoxelShapes.fullCube();
        Block block = state.getBlock();
        VoxelShape combinedShape = VoxelShapes.empty();
        try {
            for (BlockState possibleState : block.getStateManager().getStates()) {
                VoxelShape currentShape = possibleState.getOutlineShape(null, BlockPos.ORIGIN);
                combinedShape = VoxelShapes.union(combinedShape, currentShape);
            }
            return combinedShape.isEmpty() ? VoxelShapes.fullCube() : combinedShape;
        } catch (Throwable ignored) {
            return VoxelShapes.fullCube();
        }
    }

    public static boolean isTransparent(BlockState state) {
        if (state == null) return true;
        Block block = state.getBlock();
        if (block instanceof TransparentBlock) {
            return true;
        }
        return !state.isOpaque();
    }

    public static int canPush(BlockState state) {
        if (state == null) return 0;
        if (state.getPistonBehavior() == PistonBehavior.NORMAL) return 1;
        if (state.getPistonBehavior() == PistonBehavior.PUSH_ONLY) return 2;
        return 0;
    }
}
