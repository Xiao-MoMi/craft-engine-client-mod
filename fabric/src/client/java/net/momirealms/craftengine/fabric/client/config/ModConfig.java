package net.momirealms.craftengine.fabric.client.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.momirealms.craftengine.fabric.client.CraftEngineFabricModClient;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ModConfig {
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("craft-engine-fabric-mod").resolve("config.yml");
    public static boolean enableNetwork = false;
    public static boolean enableCancelBlockUpdate = false;

    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setSavingRunnable(ModConfig::saveConfig)
                .setTitle(Component.translatable("title.craftengine.config"));
        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("category.craftengine.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        general.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("option.craftengine.enable_network")
                                .withStyle(ChatFormatting.WHITE),
                        enableNetwork)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> enableNetwork = newValue)
                .setTooltip(
                        Component.translatable("tooltip.craftengine.enable_network")
                                .withStyle(ChatFormatting.GRAY)
                )
                .build());
        general.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("option.craftengine.enable_cancel_block_update")
                                .withStyle(ChatFormatting.WHITE),
                        enableCancelBlockUpdate)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> enableCancelBlockUpdate = newValue)
                .setTooltip(
                        Component.translatable("tooltip.craftengine.enable_cancel_block_update")
                )
                .build()
        );
        return builder.build();
    }

    public static void saveConfig() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        var data = new java.util.HashMap<String, Object>();
        data.put("enable-network", ModConfig.enableNetwork);
        data.put("enable-cancel-block-update", ModConfig.enableCancelBlockUpdate);
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            CraftEngineFabricModClient.LOGGER.warn("Failed to save config file", e);
        }
    }

    public static void loadConfig() {
        if (!Files.exists(CONFIG_PATH)) {
            ModConfig.enableNetwork = false;
            ModConfig.enableCancelBlockUpdate = false;
            return;
        }
        try (InputStream inputStream = Files.newInputStream(CONFIG_PATH)) {
            Yaml yaml = new Yaml();
            Map<Object, Object> config = yaml.loadAs(inputStream, Map.class);
            if (config == null) {
                ModConfig.enableNetwork = false;
                ModConfig.enableCancelBlockUpdate = false;
                return;
            }
            ModConfig.enableNetwork = (boolean) config.getOrDefault("enable-network", false);
            ModConfig.enableCancelBlockUpdate = (boolean) config.getOrDefault("enable-cancel-block-update", false);
        } catch (IOException e) {
            CraftEngineFabricModClient.LOGGER.error("Failed to load config", e);
        }
    }
}
