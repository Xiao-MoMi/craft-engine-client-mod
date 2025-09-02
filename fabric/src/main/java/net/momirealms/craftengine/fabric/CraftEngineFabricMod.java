package net.momirealms.craftengine.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.momirealms.craftengine.fabric.block.BlockManager;
import net.momirealms.craftengine.fabric.client.config.ModConfig;
import net.momirealms.craftengine.fabric.network.NetworkManager;
import net.momirealms.craftengine.fabric.logger.LoggerFilter;
import net.momirealms.craftengine.fabric.logger.ModLogger;
import net.momirealms.craftengine.fabric.logger.Slf4jModLogger;
import net.momirealms.craftengine.fabric.util.ConfigUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public class CraftEngineFabricMod implements ModInitializer {
    public static final String MOD_ID = "craftengine";
    private static CraftEngineFabricMod instance;
    private Path configPath;
    private ModLogger logger;
    private NetworkManager networkManager;
    private BlockManager blockManager;

    @Override
    public void onInitialize() {
        instance = this;
        try {
            LoggerFilter.filter();
            ConfigUtils.saveDefaultResource();
            ModConfig.loadConfig();
            this.networkManager = new NetworkManager(this);
            this.blockManager = new BlockManager(this);
        } catch (IOException e) {
            logger().severe("Failed to initialize the mod", e);
        }
    }

    public static CraftEngineFabricMod instance() {
        return instance;
    }

    public ModLogger logger() {
        if (logger == null) {
            logger = new Slf4jModLogger(LoggerFactory.getLogger(MOD_ID));
        }
        return logger;
    }

    public Path dataFolderPath() {
        if (configPath == null) {
            configPath = FabricLoader.getInstance().getConfigDir().resolve("craft-engine-fabric-mod");
        }
        return configPath;
    }

    public NetworkManager networkManager() {
        if (networkManager == null) {
            throw new IllegalStateException("NetworkManager not initialized");
        }
        return networkManager;
    }

    public BlockManager blockManager() {
        if (blockManager == null) {
            throw new IllegalStateException("BlockManager not initialized");
        }
        return blockManager;
    }
}
