package net.momirealms.craftengine.fabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;

@Environment(EnvType.CLIENT)
public class CommandManager {
    private static CommandManager instance;
    private final CraftEngineFabricMod mod;

    public CommandManager(CraftEngineFabricMod mod) {
        instance = this;
        this.mod = mod;
        ClientCommandRegistrationCallback.EVENT.register(this::registerCommands);
    }

    public static CommandManager instance() {
        return instance;
    }

    private void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext context) {
        ReloadCommands.register(dispatcher);
    }
}
