package net.momirealms.craftengine.fabric.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.config.ModConfig;

import java.io.IOException;
import java.nio.file.Files;

@Environment(EnvType.CLIENT)
public final class ConfigUtils {

    public static void saveDefaultResource() {
        if (!Files.exists(ModConfig.CONFIG_DIR)) {
            try {
                Files.createDirectories(ModConfig.CONFIG_DIR);
            } catch (IOException e) {
                CraftEngineFabricMod.instance().logger().warn("Failed to create config directory", e);
            }
        }
        if (!Files.exists(ModConfig.CONFIG_PATH)) {
            ModConfig.INSTANCE.saveConfig();
        }
    }

}
