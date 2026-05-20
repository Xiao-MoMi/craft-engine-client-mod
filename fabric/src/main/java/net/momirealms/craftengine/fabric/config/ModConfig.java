package net.momirealms.craftengine.fabric.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ModConfig {
    public static final ModConfig INSTANCE = new ModConfig();
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("craftengine");
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("craftengine").resolve("config.yml");
    private boolean enableClientCustomBlock = false;
    private boolean enableCancelBlockUpdate = false;
    private int serverSideBlocks = 10000;
    private boolean disableResourcePackLoadingScreen = false;

    private ModConfig() {
    }

    public boolean enableClientCustomBlock() {
        return enableClientCustomBlock;
    }

    public void enableClientCustomBlock(boolean enable) {
        this.enableClientCustomBlock = enable;
    }

    public boolean enableCancelBlockUpdate() {
        return enableCancelBlockUpdate;
    }

    public void enableCancelBlockUpdate(boolean enable) {
        this.enableCancelBlockUpdate = enable;
    }

    public int serverSideBlocks() {
        return serverSideBlocks;
    }

    public void serverSideBlocks(int size) {
        this.serverSideBlocks = size;
    }

    public boolean disableResourcePackLoadingScreen() {
        return disableResourcePackLoadingScreen;
    }

    public void disableResourcePackLoadingScreen(boolean disable) {
        this.disableResourcePackLoadingScreen = disable;
    }

    public void saveConfig() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        var data = new java.util.HashMap<String, Object>();
        data.put("enable-client-custom-block", enableClientCustomBlock());
        data.put("enable-cancel-block-update", enableCancelBlockUpdate());
        data.put("server-side-blocks", serverSideBlocks());
        data.put("disable-resource-pack-loading-screen", disableResourcePackLoadingScreen());
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            CraftEngineFabricMod.instance().logger().warn("Failed to save config file", e);
        }
    }

    public void loadConfig() {
        if (!Files.exists(CONFIG_PATH)) {
            setDefaultConfig();
            saveConfig();
            return;
        }
        try (InputStream inputStream = Files.newInputStream(CONFIG_PATH)) {
            Yaml yaml = new Yaml();
            Map<Object, Object> config = yaml.loadAs(inputStream, Map.class);
            if (config == null) {
                setDefaultConfig();
                saveConfig();
                return;
            }
            enableClientCustomBlock((boolean) config.getOrDefault("enable-client-custom-block", false));
            enableCancelBlockUpdate((boolean) config.getOrDefault("enable-cancel-block-update", false));
            serverSideBlocks((int) config.getOrDefault("server-side-blocks", 10000));
            disableResourcePackLoadingScreen((boolean) config.getOrDefault("disable-resource-pack-loading-screen", false));
        } catch (Throwable e) {
            CraftEngineFabricMod.instance().logger().severe("Failed to load config", e);
        }
    }

    private void setDefaultConfig() {
        enableClientCustomBlock(false);
        enableCancelBlockUpdate(false);
        serverSideBlocks(10000);
        disableResourcePackLoadingScreen(false);
    }
}
