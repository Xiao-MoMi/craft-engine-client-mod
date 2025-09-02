package net.momirealms.craftengine.fabric.network;

import com.mojang.serialization.Lifecycle;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.WritableRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.client.config.ModConfig;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;
import net.momirealms.craftengine.fabric.network.protocol.CancelBlockUpdatePacket;
import net.momirealms.craftengine.fabric.network.protocol.ClientBlockStateSizePacket;
import net.momirealms.craftengine.fabric.network.protocol.ClientCustomBlockPacket;
import net.momirealms.craftengine.fabric.registries.BuiltInRegistries;

import java.util.Objects;

public class NetworkManager {
    public static final ResourceLocation CRAFTENGINE_PAYLOAD = Objects.requireNonNull(ResourceLocation.tryBuild("craftengine", "payload"));
    public static boolean serverInstalled = false;
    private final CraftEngineFabricMod mod;

    public NetworkManager(CraftEngineFabricMod mod) {
        this.mod = mod;
        registerDataTypes();
        ClientPlayConnectionEvents.JOIN.register(this::initChannel);
        ClientPlayConnectionEvents.DISCONNECT.register((client, handler) -> serverInstalled = false);
        ClientPlayNetworking.registerGlobalReceiver(CRAFTENGINE_PAYLOAD, this::handleReceiver);
    }

    private void registerDataTypes() {
        registerDataType(ClientCustomBlockPacket.TYPE, ClientCustomBlockPacket.CODEC);
        registerDataType(CancelBlockUpdatePacket.TYPE, CancelBlockUpdatePacket.CODEC);
        registerDataType(ClientBlockStateSizePacket.TYPE, ClientBlockStateSizePacket.CODEC);
    }

    public static <T extends ModPacket> void registerDataType(ResourceKey<NetworkCodec<FriendlyByteBuf, ? extends ModPacket>> key, NetworkCodec<FriendlyByteBuf, T> codec) {
        ((WritableRegistry<NetworkCodec<FriendlyByteBuf, ? extends ModPacket>>) BuiltInRegistries.MOD_PACKET).register(key, codec, Lifecycle.stable());
    }

    private void initChannel(ClientPacketListener clientPacketListener, PacketSender packetSender, Minecraft minecraft) {
        sendData(new ClientBlockStateSizePacket(Block.BLOCK_STATE_REGISTRY.size()));

        if (!ModConfig.enableNetwork && !ModConfig.enableCancelBlockUpdate) {
            return;
        }

        if (ModConfig.enableNetwork) {
            sendData(new ClientCustomBlockPacket(Block.BLOCK_STATE_REGISTRY.size()));
        } else {
            sendData(new CancelBlockUpdatePacket(true));
        }
    }

    public void sendData(ModPacket data) {
        @SuppressWarnings("unchecked")
        NetworkCodec<FriendlyByteBuf, ModPacket> codec = (NetworkCodec<FriendlyByteBuf, ModPacket>) BuiltInRegistries.MOD_PACKET.get(data.type());
        if (codec == null) {
            this.mod.logger().warn("Unknown data type class: " + data.getClass().getName());
            return;
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeByte(BuiltInRegistries.MOD_PACKET.getId(codec));
        codec.encode(buf, data);
        ClientPlayNetworking.send(CRAFTENGINE_PAYLOAD, buf);
    }

    private void handleReceiver(Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf byteBuf, PacketSender packetSender) {
        byte type = byteBuf.readByte();
        @SuppressWarnings("unchecked")
        NetworkCodec<FriendlyByteBuf, ModPacket> codec = (NetworkCodec<FriendlyByteBuf, ModPacket>) BuiltInRegistries.MOD_PACKET.byId(type);
        if (codec == null) {
            this.mod.logger().warn("Unknown data type received: " + type);
            return;
        }

        ModPacket networkData = codec.decode(byteBuf);
        networkData.handle(minecraft, clientPacketListener, packetSender);
    }
}
