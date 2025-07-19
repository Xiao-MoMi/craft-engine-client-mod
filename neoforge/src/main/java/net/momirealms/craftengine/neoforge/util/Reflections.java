package net.momirealms.craftengine.neoforge.util;

import net.minecraft.core.MappedRegistry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.lang.reflect.Field;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class Reflections {

    public static final Field field$MappedRegistry$frozen = requireNonNull(
            ReflectionUtils.getDeclaredField(MappedRegistry.class, boolean.class, 0)
    );

    public static final Field field$MappedRegistry$unregisteredIntrusiveHolders = requireNonNull(
            ReflectionUtils.getDeclaredField(MappedRegistry.class, Map.class, 5)
    );

    public static final Field field$BlockStateBase$occlusionShapesByFace = requireNonNull(
            ReflectionUtils.getDeclaredField(BlockBehaviour.BlockStateBase.class, VoxelShape[].class, 2)
    );
}
