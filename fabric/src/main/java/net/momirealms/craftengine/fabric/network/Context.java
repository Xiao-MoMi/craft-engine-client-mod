package net.momirealms.craftengine.fabric.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;

@Environment(EnvType.CLIENT)
public class Context {
    private final Minecraft client;
    private final ClientCommonPacketListenerImpl packetListener;
    private final PacketSender responseSender;

    private Context(Minecraft client, ClientCommonPacketListenerImpl packetListener, PacketSender responseSender) {
        this.client = client;
        this.packetListener = packetListener;
        this.responseSender = responseSender;
    }

    public static Context of(Minecraft client, ClientCommonPacketListenerImpl handler, PacketSender responseSender) {
        return new Context(client, handler, responseSender);
    }

    public Minecraft client() {
        return client;
    }

    public ClientCommonPacketListenerImpl packetListener() {
        return packetListener;
    }

    public PacketSender responseSender() {
        return responseSender;
    }

    @Override
    public String toString() {
        return "Context{" +
                "client=" + client +
                ", packetListener=" + packetListener +
                ", responseSender=" + responseSender +
                '}';
    }
}
