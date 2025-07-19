package net.momirealms.craftengine.neoforge.util;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

import static java.util.Objects.requireNonNull;

public class BlockUtils {
    private final static Field COLLIDABLE_FIELD = requireNonNull(getDeclaredField(BlockBehaviour.Properties.class, boolean.class, 0));

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
            BlockBehaviour.Properties settings = state.getBlock().properties();
            return !COLLIDABLE_FIELD.getBoolean(settings);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access 'collidable' field", e);
        }
    }

    public static VoxelShape getShape(BlockState state) {
        if (state == null) return Shapes.block();
        Block block = state.getBlock();
        VoxelShape combinedShape = Shapes.empty();
        try {
            for (BlockState possibleState : block.getStateDefinition().getPossibleStates()) {
                VoxelShape currentShape = possibleState.getShape(null, BlockPos.ZERO);
                combinedShape = Shapes.or(combinedShape, currentShape);
            }
            return combinedShape.isEmpty() ? Shapes.block() : combinedShape;
        } catch (Throwable ignored) {
            return Shapes.block();
        }
    }

    public static boolean isTransparent(BlockState state) {
        if (state == null) return true;
        Block block = state.getBlock();
        if (block instanceof TransparentBlock) {
            return true;
        }
        return !state.canOcclude();
    }

    public static int canPush(BlockState state) {
        if (state == null) return 0;
        if (state.getPistonPushReaction() == PushReaction.NORMAL) return 1;
        if (state.getPistonPushReaction() == PushReaction.PUSH_ONLY) return 2;
        return 0;
    }

    public static VoxelShape[] getShapes(BlockState state) throws IllegalAccessException {
        return (VoxelShape[]) Reflections.field$BlockStateBase$occlusionShapesByFace.get(state);
    }
}
