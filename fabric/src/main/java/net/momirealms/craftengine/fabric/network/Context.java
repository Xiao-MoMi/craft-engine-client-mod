package net.momirealms.craftengine.fabric.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;

@Environment(EnvType.CLIENT)
public class Context {
    private final LocalPlayer localPlayer;
    private final PacketSender responseSender;

    private Context(LocalPlayer localPlayer, PacketSender responseSender) {
        this.localPlayer = localPlayer;
        this.responseSender = responseSender;
    }

    public static Context of(LocalPlayer localPlayer, PacketSender responseSender) {
        return new Context(localPlayer, responseSender);
    }

    public LocalPlayer localPlayer() {
        return localPlayer;
    }

    public PacketSender responseSender() {
        return responseSender;
    }

    @Override
    public String toString() {
        return "Context{" +
                "localPlayer=" + localPlayer +
                ", responseSender=" + responseSender +
                '}';
    }
}
