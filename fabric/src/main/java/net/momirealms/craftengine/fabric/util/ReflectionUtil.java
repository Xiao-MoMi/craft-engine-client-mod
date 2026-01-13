package net.momirealms.craftengine.fabric.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class ReflectionUtil {
    public static final Unsafe UNSAFE;

    private ReflectionUtil() {}

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
