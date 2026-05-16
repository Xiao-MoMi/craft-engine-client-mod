package net.momirealms.craftengine.fabric.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;

public final class RegistryUtils {
    private RegistryUtils() {}

    public static RegistryAccess getRegistryAccess() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        return connection == null ? RegistryAccess.EMPTY : connection.registryAccess();
    }
}
