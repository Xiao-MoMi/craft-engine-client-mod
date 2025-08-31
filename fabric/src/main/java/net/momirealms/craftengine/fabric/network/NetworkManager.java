package net.momirealms.craftengine.fabric.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.client.config.ModConfig;
import net.momirealms.craftengine.fabric.network.protocol.CancelBlockUpdatePacket;
import net.momirealms.craftengine.fabric.network.protocol.ClientBlockStateSizePacket;
import net.momirealms.craftengine.fabric.network.protocol.ClientCustomBlockPacket;
import net.momirealms.craftengine.fabric.registries.BuiltInRegistries;

import java.util.Optional;

public class NetworkManager {
    public static boolean serverInstalled = false;
    private final CraftEngineFabricMod mod;

    public NetworkManager(CraftEngineFabricMod mod) {
        this.mod = mod;
        registerDataTypes();
        PayloadTypeRegistry.configurationS2C().register(CraftEnginePayload.TYPE, CraftEnginePayload.CODEC);
        PayloadTypeRegistry.configurationC2S().register(CraftEnginePayload.TYPE, CraftEnginePayload.CODEC);
        ClientConfigurationConnectionEvents.START.register(this::initChannel);
        ClientConfigurationNetworking.registerGlobalReceiver(CraftEnginePayload.TYPE, this::handleReceiver);
        ClientPlayConnectionEvents.DISCONNECT.register((client, handler) -> serverInstalled = false);
    }

    private void registerDataTypes() {
        registerDataType(ClientCustomBlockPacket.TYPE, ClientCustomBlockPacket.CODEC);
        registerDataType(CancelBlockUpdatePacket.TYPE, CancelBlockUpdatePacket.CODEC);
        registerDataType(ClientBlockStateSizePacket.TYPE, ClientBlockStateSizePacket.CODEC);
    }

    public static <T extends ModPacket> void registerDataType(ResourceKey<StreamCodec<FriendlyByteBuf, ? extends ModPacket>> key, StreamCodec<FriendlyByteBuf, T> codec) {
        ((WritableRegistry<StreamCodec<FriendlyByteBuf, ? extends ModPacket>>) BuiltInRegistries.MOD_PACKET).register(key, codec, RegistrationInfo.BUILT_IN);
    }

    private void initChannel(ClientConfigurationPacketListenerImpl handler, Minecraft client) {
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
        Optional<Holder.Reference<StreamCodec<FriendlyByteBuf, ? extends ModPacket>>> optionalType = BuiltInRegistries.MOD_PACKET.get(data.type());
        if (optionalType.isEmpty()) {
            this.mod.logger().warn("Unknown data type class: " + data.getClass().getName());
            return;
        }
        @SuppressWarnings("unchecked")
        StreamCodec<FriendlyByteBuf, ModPacket> codec = (StreamCodec<FriendlyByteBuf, ModPacket>) optionalType.get().value();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeByte(BuiltInRegistries.MOD_PACKET.getId(codec));
        codec.encode(buf, data);
        ClientConfigurationNetworking.send(new CraftEnginePayload(buf.array()));
    }

    private void handleReceiver(CraftEnginePayload payload, ClientConfigurationNetworking.Context context) {
        byte[] data = payload.data();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
        byte type = buf.readByte();
        @SuppressWarnings("unchecked")
        StreamCodec<FriendlyByteBuf, ModPacket> codec = (StreamCodec<FriendlyByteBuf, ModPacket>) BuiltInRegistries.MOD_PACKET.byId(type);
        if (codec == null) {
            this.mod.logger().warn("Unknown data type received: " + type);
            return;
        }

        ModPacket networkData = codec.decode(buf);
        networkData.handle(context);
    }
}
