package net.momirealms.craftengine.fabric.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.momirealms.craftengine.fabric.client.config.ModConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public final class ConfigUtils {
    public static final Path CONFIG_DIR = Path.of("config/craft-engine-fabric-mod/");

    public static void saveDefaultResource() throws IOException {
        if (!Files.exists(CONFIG_DIR)) {
            Files.createDirectories(CONFIG_DIR);
            Path readme = CONFIG_DIR.resolve("README.txt");
            Files.writeString(readme, "Please copy 'mappings.yml' & 'additional-real-blocks.yml' to this folder to apply the configs.");
        }
        if (!Files.exists(ModConfig.CONFIG_PATH)) {
            ModConfig.saveConfig();
        }
    }

}
