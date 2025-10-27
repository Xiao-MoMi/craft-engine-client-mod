package net.momirealms.craftengine.fabric.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.momirealms.craftengine.fabric.network.NetworkManager;
import net.momirealms.craftengine.fabric.network.protocol.CancelBlockUpdatePacket;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigScreen.INSTANCE;
    }

    private static class ConfigScreen implements ConfigScreenFactory<Screen> {
        private static final ConfigScreen INSTANCE = new ConfigScreen();

        @Override
        public Screen create(Screen parent) {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setSavingRunnable(ModConfig.INSTANCE::saveConfig)
                    .setTitle(Component.translatable("title.craftengine.config"));
            ConfigCategory general = builder.getOrCreateCategory(Component.translatable("category.craftengine.general"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            general.addEntry(entryBuilder.startBooleanToggle(
                            Component.translatable("option.craftengine.enable_network")
                                    .withStyle(ChatFormatting.WHITE),
                            ModConfig.INSTANCE.enableNetwork())
                    .setDefaultValue(false)
                    .setSaveConsumer(ModConfig.INSTANCE::enableNetwork)
                    .setTooltip(
                            Component.translatable("tooltip.craftengine.enable_network")
                                    .withStyle(ChatFormatting.GRAY)
                    )
                    .build());
            general.addEntry(entryBuilder.startBooleanToggle(
                            Component.translatable("option.craftengine.enable_cancel_block_update")
                                    .withStyle(ChatFormatting.WHITE),
                            ModConfig.INSTANCE.enableCancelBlockUpdate())
                    .setDefaultValue(false)
                    .setSaveConsumer(s -> {
                        ModConfig.INSTANCE.enableCancelBlockUpdate(s);
                        NetworkManager.instance().sendData(new CancelBlockUpdatePacket(s));
                    })
                    .setTooltip(
                            Component.translatable("tooltip.craftengine.enable_cancel_block_update")
                                    .withStyle(ChatFormatting.GRAY)
                    )
                    .build()
            );
            general.addEntry(entryBuilder.startIntField(
                            Component.translatable("option.craftengine.server_side_blocks")
                                    .withStyle(ChatFormatting.WHITE),
                            ModConfig.INSTANCE.serverSideBlocks())
                    .setDefaultValue(2000)
                    .setSaveConsumer(ModConfig.INSTANCE::serverSideBlocks)
                    .setTooltip(
                            Component.translatable("tooltip.craftengine.server_side_blocks")
                                    .withStyle(ChatFormatting.GRAY)
                    )
                    .build()
            );
            return builder.build();
        }
    }
}
