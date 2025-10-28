package net.momirealms.craftengine.fabric.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

@Environment(EnvType.CLIENT)
public class Context {
    private final Minecraft client;
    private final ClientPacketListener packetListener;
    private final PacketSender responseSender;

    private Context(Minecraft client, ClientPacketListener packetListener, PacketSender responseSender) {
        this.client = client;
        this.packetListener = packetListener;
        this.responseSender = responseSender;
    }

    public static Context of(Minecraft client, ClientPacketListener handler, PacketSender responseSender) {
        return new Context(client, handler, responseSender);
    }

    public Minecraft client() {
        return client;
    }

    public ClientPacketListener packetListener() {
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
