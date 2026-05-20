package net.momirealms.craftengine.fabric.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.momirealms.craftengine.fabric.network.NetworkManager;
import net.momirealms.craftengine.fabric.network.protocol.ServerboundCancelBlockUpdateRequestPacket;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return ClothConfigMissingScreen::new;
        }
        return ConfigScreen.INSTANCE;
    }

    private static class ConfigScreen implements ConfigScreenFactory<@NotNull Screen> {
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
                            ModConfig.INSTANCE.enableClientCustomBlock())
                    .setDefaultValue(false)
                    .setSaveConsumer(ModConfig.INSTANCE::enableClientCustomBlock)
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
                    .setSaveConsumer(e -> {
                        if (e) {
                            NetworkManager.instance().sendCustomPacket(ServerboundCancelBlockUpdateRequestPacket.INSTANCE);
                        } else {
                            ModConfig.INSTANCE.enableCancelBlockUpdate(false);
                        }
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
                    .setDefaultValue(10000)
                    .setSaveConsumer(ModConfig.INSTANCE::serverSideBlocks)
                    .setTooltip(
                            Component.translatable("tooltip.craftengine.server_side_blocks")
                                    .withStyle(ChatFormatting.GRAY)
                    )
                    .build()
            );
            general.addEntry(entryBuilder.startBooleanToggle(
                            Component.translatable("option.craftengine.disable_resourcepack_loading_screen")
                                    .withStyle(ChatFormatting.WHITE),
                            ModConfig.INSTANCE.disableResourcePackLoadingScreen())
                    .setDefaultValue(false)
                    .setSaveConsumer(ModConfig.INSTANCE::disableResourcePackLoadingScreen)
                    .setTooltip(
                            Component.translatable("tooltip.craftengine.disable_resourcepack_loading_screen")
                                    .withStyle(ChatFormatting.GRAY)
                    )
                    .build()
            );
            general.addEntry(entryBuilder.startBooleanToggle(
                            Component.translatable("option.craftengine.force_ghost_recipe_show_input_itemstack_count")
                                    .withStyle(ChatFormatting.WHITE),
                            ModConfig.INSTANCE.forceGhostRecipeShowInputItemStackCount())
                    .setDefaultValue(false)
                    .setSaveConsumer(ModConfig.INSTANCE::forceGhostRecipeShowInputItemStackCount)
                    .setTooltip(
                            Component.translatable("tooltip.craftengine.force_ghost_recipe_show_input_itemstack_count")
                                    .withStyle(ChatFormatting.GRAY)
                    )
                    .build()
            );
            return builder.build();
        }
    }

    private static class ClothConfigMissingScreen extends Screen {
        private final Screen parent;

        protected ClothConfigMissingScreen(Screen parent) {
            super(Component.translatable("title.craftengine.config"));
            this.parent = parent;
        }

        @Override
        protected void init() {
            super.init();
            this.addRenderableWidget(Button.builder(Component.translatable("gui.back"), button -> this.onClose()).pos(this.width / 2 - 100, this.height - 30).size(200, 20).build());
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            super.render(guiGraphics, mouseX, mouseY, partialTick);
            guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 17, -1);
            Component message = Component.translatable("warning.craftengine.config");
            guiGraphics.drawCenteredString(this.font, message, this.width / 2, this.height / 2, -1);
        }

        @Override
        public void onClose() {
            this.minecraft.setScreen(this.parent);
        }
    }
}
