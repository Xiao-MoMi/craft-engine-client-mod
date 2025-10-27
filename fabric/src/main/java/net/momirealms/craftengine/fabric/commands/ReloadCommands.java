package net.momirealms.craftengine.fabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.chat.Component;
import net.momirealms.craftengine.fabric.config.ModConfig;

@Environment(EnvType.CLIENT)
public class ReloadCommands {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext context) {
        dispatcher.register(ClientCommandManager.literal("craftengine-client").then(ClientCommandManager.literal("reload").executes(ReloadCommands::reloadConfig)));
        dispatcher.register(ClientCommandManager.literal("cec").then(ClientCommandManager.literal("reload").executes(ReloadCommands::reloadConfig)));
    }

    private static int reloadConfig(CommandContext<FabricClientCommandSource> context) {
        ModConfig.INSTANCE.loadConfig();
        context.getSource().sendFeedback(Component.translatable("craftengine.reload.success").withStyle(ChatFormatting.GREEN));
        return 0;
    }
}
