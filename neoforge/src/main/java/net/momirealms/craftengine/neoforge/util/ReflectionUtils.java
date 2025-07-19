package net.momirealms.craftengine.neoforge.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

public class ReflectionUtils {

    @NotNull
    public static <T extends AccessibleObject> T setAccessible(@NotNull final T o) {
        o.setAccessible(true);
        return o;
    }

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
}
