package net.momirealms.craftengine.neoforge;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.momirealms.craftengine.neoforge.client.CraftEngineNeoForgeModClient;
import net.momirealms.craftengine.neoforge.client.config.ModConfig;
import net.momirealms.craftengine.neoforge.client.network.ConfigurationTask;
import net.momirealms.craftengine.neoforge.client.network.CraftEnginePayload;
import net.momirealms.craftengine.neoforge.util.BlockUtils;
import net.momirealms.craftengine.neoforge.util.LoggerFilter;
import net.momirealms.craftengine.neoforge.util.RegisterBlocks;
import net.momirealms.craftengine.neoforge.util.YamlUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Mod(CraftEngineNeoForgeMod.MOD_ID)
public class CraftEngineNeoForgeMod {
    public static final String MOD_ID = "craftengine";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("craft-engine-neoforge-mod").resolve("config.yml");

    public CraftEngineNeoForgeMod(IEventBus modEventBus, ModContainer modContainer) {
        if (FMLEnvironment.dist.isDedicatedServer()) return;
        modEventBus.register(this);
        loadConfig();
        LoggerFilter.filter();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class,
                () -> (container, parent) -> ModConfig.getConfigScreen(parent)
        );
    }

    @SuppressWarnings("unchecked")
    private void loadConfig() {
        if (!Files.exists(CONFIG_PATH)) {
            ModConfig.enableNetwork = false;
            ModConfig.enableCancelBlockUpdate = false;
            return;
        }
        try (InputStream inputStream = Files.newInputStream(CONFIG_PATH)) {
            Yaml yaml = new Yaml();
            var config = yaml.loadAs(inputStream, java.util.Map.class);
            if (config == null) {
                ModConfig.enableNetwork = false;
                ModConfig.enableCancelBlockUpdate = false;
                return;
            }
            ModConfig.enableNetwork = (Boolean) config.getOrDefault("enable-network", false);
            ModConfig.enableCancelBlockUpdate = (Boolean) config.getOrDefault("enable-cancel-block-update", false);
        } catch (IOException e) {
            LOGGER.error("Failed to load config", e);
        }
    }

    @SubscribeEvent
    public void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(CraftEngineNeoForgeMod.MOD_ID).optional();
        registrar.configurationToClient(CraftEnginePayload.ID, CraftEnginePayload.CODEC, CraftEngineNeoForgeModClient::handleReceiver).optional();
    }

    @SubscribeEvent
    public void onRegisterConfigTasks(final RegisterConfigurationTasksEvent event) {
        event.register(new ConfigurationTask(event.getListener()));
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        try {
            YamlUtils.saveDefaultResource();
            Map<ResourceLocation, Integer> map = YamlUtils.loadMappingsAndAdditionalBlocks();
            RegisterBlocks.unfreezeRegistry();
            for (Map.Entry<ResourceLocation, Integer> entry : map.entrySet()) {
                ResourceLocation replacedBlockId = entry.getKey();
                for (int i = 0; i < entry.getValue(); i++) {
                    BlockState blockState = YamlUtils.createBlockData("minecraft:" + replacedBlockId.getPath());
                    Block block = RegisterBlocks.register(
                            replacedBlockId.getPath() + "_" + i,
                            BlockUtils.canPassThrough(blockState),
                            BlockUtils.getShape(blockState),
                            BlockUtils.isTransparent(blockState),
                            BlockUtils.canPush(blockState),
                            BlockUtils.getShapes(blockState)
                    );
                    Block.BLOCK_STATE_REGISTRY.add(block.defaultBlockState());
                }
            }
            RegisterBlocks.freezeRegistry();
        } catch (IOException | ReflectiveOperationException e) {
            LOGGER.error("Failed to initialize the mod", e);
        }
        CraftEngineNeoForgeModClient.registerRenderLayer();
    }
}
