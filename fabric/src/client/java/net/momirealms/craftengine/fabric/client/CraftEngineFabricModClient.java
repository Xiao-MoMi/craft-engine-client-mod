package net.momirealms.craftengine.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.momirealms.craftengine.fabric.client.block.CraftEngineBlockClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class CraftEngineFabricModClient implements ClientModInitializer {
    public static final String MOD_ID = "craftengine";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        CraftEngineBlockClientProperties.registerRenderLayer();
    }
}