package net.momirealms.craftengine.fabric.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@Environment(EnvType.CLIENT)
public final class Reflections {
    private Reflections() {
    }

    public static final Field field$BlockBehaviour$Properties$hasCollision = requireNonNull(
            ReflectionUtils.getDeclaredField(
                    BlockBehaviour.Properties.class, boolean.class, 0
            )
    );

    public static final Field field$Block$stateDefinition = requireNonNull(
            ReflectionUtils.getDeclaredField(
                    Block.class, StateDefinition.class, 0
            )
    );

    public static final Field field$Block$defaultBlockState = requireNonNull(
            ReflectionUtils.getDeclaredField(
                    Block.class, BlockState.class, 0
            )
    );

    public static final Method method$Holder$Reference$bindValue = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    Holder.Reference.class, void.class, Object.class
            )
    );

    public static final Field field$Holder$Reference$tags = requireNonNull(
            ReflectionUtils.getDeclaredField(
                    Holder.Reference.class, Set.class, 0
            )
    );

    public static final Field field$BlockBehaviour$soundType = requireNonNull(
            ReflectionUtils.getDeclaredField(
                    BlockBehaviour.class, SoundType.class, 0
            )
    );

    public static final Field field$BlockBehaviour$properties = requireNonNull(
            ReflectionUtils.getDeclaredField(
                    BlockBehaviour.class, BlockBehaviour.Properties.class, 0
            )
    );
}
