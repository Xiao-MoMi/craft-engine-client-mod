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
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("craft-engine-fabric-mod");
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("craft-engine-fabric-mod").resolve("config.yml");
    private boolean enableNetwork = false;
    private boolean enableCancelBlockUpdate = false;
    private int serverSideBlocks = 10000;
    private boolean disableResourcePackLoadingScreen = false;
    private boolean forceGhostRecipeShowInputItemStackCount = false;

    private ModConfig() {
    }

    public boolean enableNetwork() {
        return enableNetwork;
    }

    public void enableNetwork(boolean enableNetwork) {
        this.enableNetwork = enableNetwork;
    }

    public boolean enableCancelBlockUpdate() {
        return enableCancelBlockUpdate;
    }

    public void enableCancelBlockUpdate(boolean enableCancelBlockUpdate) {
        this.enableCancelBlockUpdate = enableCancelBlockUpdate;
    }

    public int serverSideBlocks() {
        return serverSideBlocks;
    }

    public void serverSideBlocks(int serverSideBlocks) {
        this.serverSideBlocks = serverSideBlocks;
    }

    public boolean disableResourcePackLoadingScreen() {
        return disableResourcePackLoadingScreen;
    }

    public void disableResourcePackLoadingScreen(boolean disableResourcePackLoadingScreen) {
        this.disableResourcePackLoadingScreen = disableResourcePackLoadingScreen;
    }

    public boolean forceGhostRecipeShowInputItemStackCount() {
        return forceGhostRecipeShowInputItemStackCount;
    }

    public void forceGhostRecipeShowInputItemStackCount(boolean forceGhostRecipeShowInputItemStackCount) {
        this.forceGhostRecipeShowInputItemStackCount = forceGhostRecipeShowInputItemStackCount;
    }

    public void saveConfig() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        var data = new java.util.HashMap<String, Object>();
        data.put("enable-network", enableNetwork());
        data.put("enable-cancel-block-update", enableCancelBlockUpdate());
        data.put("server-side-blocks", serverSideBlocks());
        data.put("disable-resource-pack-loading-screen", disableResourcePackLoadingScreen());
        data.put("force-ghost-recipe-show-input-itemstack-count", forceGhostRecipeShowInputItemStackCount());
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            CraftEngineFabricMod.instance().logger().warn("Failed to save config file", e);
        }
    }

    public void loadConfig() {
        if (!Files.exists(CONFIG_PATH)) {
            enableNetwork(false);
            enableCancelBlockUpdate(false);
            serverSideBlocks(10000);
            disableResourcePackLoadingScreen(false);
            forceGhostRecipeShowInputItemStackCount(false);
            return;
        }
        try (InputStream inputStream = Files.newInputStream(CONFIG_PATH)) {
            Yaml yaml = new Yaml();
            Map<Object, Object> config = yaml.loadAs(inputStream, Map.class);
            if (config == null) {
                enableNetwork(false);
                enableCancelBlockUpdate(false);
                serverSideBlocks(10000);
                disableResourcePackLoadingScreen(false);
                forceGhostRecipeShowInputItemStackCount(false);
                return;
            }
            enableNetwork((boolean) config.getOrDefault("enable-network", false));
            enableCancelBlockUpdate((boolean) config.getOrDefault("enable-cancel-block-update", false));
            serverSideBlocks((int) config.getOrDefault("server-side-blocks", 10000));
            disableResourcePackLoadingScreen((boolean) config.getOrDefault("disable-resource-pack-loading-screen", false));
            forceGhostRecipeShowInputItemStackCount((boolean) config.getOrDefault("force-ghost-recipe-show-input-itemstack-count", false));
        } catch (Throwable e) {
            CraftEngineFabricMod.instance().logger().severe("Failed to load config", e);
        }
    }
}
