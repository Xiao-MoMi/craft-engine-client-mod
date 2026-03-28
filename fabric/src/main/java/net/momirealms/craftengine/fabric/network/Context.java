package net.momirealms.craftengine.fabric.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class Context {
    private final Minecraft client;
    private final PacketSender responseSender;
    private final ClientCommonPacketListenerImpl networkHandler;
    @Nullable
    private final LocalPlayer player;

    private Context(ClientConfigurationNetworking.Context context) {
        this.client = context.client();
        this.networkHandler = context.packetListener();
        this.responseSender = context.responseSender();
        this.player = null;
    }

    private Context(ClientPlayNetworking.Context context) {
        this.client = context.client();
        this.player = context.player();
        this.responseSender = context.responseSender();
        this.networkHandler = this.player.connection;
    }

    public static Context of(Object o) {
        return switch (o) {
            case ClientConfigurationNetworking.Context context -> new Context(context);
            case ClientPlayNetworking.Context context -> new Context(context);
            default -> throw new IllegalArgumentException("Invalid context type: " + o.getClass().getName());
        };
    }

    public Minecraft client() {
        return client;
    }

    public PacketSender responseSender() {
        return responseSender;
    }

    public ClientCommonPacketListenerImpl networkHandler() {
        return networkHandler;
    }

    @Nullable
    public LocalPlayer player() {
        return player;
    }

    @Override
    public String toString() {
        return "Context{" +
                "client=" + client +
                ", responseSender=" + responseSender +
                ", networkHandler=" + networkHandler +
                ", player=" + player +
                '}';
    }
}
