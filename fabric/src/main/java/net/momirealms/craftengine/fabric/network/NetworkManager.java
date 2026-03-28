package net.momirealms.craftengine.fabric.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.impl.networking.client.ClientNetworkingImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.config.ModConfig;
import net.momirealms.craftengine.fabric.network.protocol.CancelBlockUpdatePacket;
import net.momirealms.craftengine.fabric.network.protocol.ClientBlockStateSizePacket;
import net.momirealms.craftengine.fabric.network.protocol.ClientCustomBlockPacket;
import net.momirealms.craftengine.fabric.network.protocol.VisualBlockStatePacket;
import net.momirealms.craftengine.fabric.registries.BuiltInRegistries;
import net.momirealms.craftengine.fabric.util.BlockStateUtils;

@Environment(EnvType.CLIENT)
public class NetworkManager {
    private static NetworkManager instance;
    private final CraftEngineFabricMod mod;
    private boolean serverInstalled = false;

    public NetworkManager(CraftEngineFabricMod mod) {
        instance = this;
        this.mod = mod;
        registerDataTypes();
        PayloadTypeRegistry.configurationS2C().register(CraftEnginePayload.TYPE, CraftEnginePayload.CODEC);
        PayloadTypeRegistry.configurationC2S().register(CraftEnginePayload.TYPE, CraftEnginePayload.CODEC);
        ClientConfigurationNetworking.registerGlobalReceiver(CraftEnginePayload.TYPE, this::handleReceiver);
        PayloadTypeRegistry.playS2C().register(CraftEnginePayload.TYPE, CraftEnginePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CraftEnginePayload.TYPE, CraftEnginePayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(CraftEnginePayload.TYPE, this::handleReceiver);
        ClientConfigurationConnectionEvents.START.register(this::initChannel);
        ClientPlayConnectionEvents.DISCONNECT.register((client, handler) -> serverInstalled(false));
    }

    public static NetworkManager instance() {
        return instance;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean serverInstalled() {
        return this.serverInstalled;
    }

    public void serverInstalled(boolean serverInstalled) {
        this.serverInstalled = serverInstalled;
    }

    private void registerDataTypes() {
        registerDataType(ClientCustomBlockPacket.TYPE, ClientCustomBlockPacket.CODEC);
        registerDataType(CancelBlockUpdatePacket.TYPE, CancelBlockUpdatePacket.CODEC);
        registerDataType(ClientBlockStateSizePacket.TYPE, ClientBlockStateSizePacket.CODEC);
        registerDataType(VisualBlockStatePacket.TYPE, VisualBlockStatePacket.CODEC);
    }

    public static <T extends ModPacket> void registerDataType(ResourceKey<StreamCodec<FriendlyByteBuf, ? extends ModPacket>> key, StreamCodec<FriendlyByteBuf, T> codec) {
        ((WritableRegistry<StreamCodec<FriendlyByteBuf, ? extends ModPacket>>) BuiltInRegistries.MOD_PACKET).register(key, codec, RegistrationInfo.BUILT_IN);
    }

    private void initChannel(ClientConfigurationPacketListenerImpl handler, Minecraft client) {
        sendData(new ClientBlockStateSizePacket(Block.BLOCK_STATE_REGISTRY.size()));

        if (!ModConfig.INSTANCE.enableNetwork() && !ModConfig.INSTANCE.enableCancelBlockUpdate()) {
            return;
        }

        if (ModConfig.INSTANCE.enableNetwork()) {
            sendData(new ClientCustomBlockPacket(BlockStateUtils.vanillaStateSize(), Block.BLOCK_STATE_REGISTRY.size()));
        } else {
            sendData(new CancelBlockUpdatePacket(true));
        }
    }

    @SuppressWarnings({"unchecked"})
    public void sendData(ModPacket data) {
        StreamCodec<FriendlyByteBuf, ModPacket> codec = (StreamCodec<FriendlyByteBuf, ModPacket>) BuiltInRegistries.MOD_PACKET.getValue(data.type());
        if (codec == null) {
            this.mod.logger().warn("Unknown data type class: " + data.getClass().getName());
            return;
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeByte(BuiltInRegistries.MOD_PACKET.getId(codec));
        codec.encode(buf, data);
        if (Minecraft.getInstance().player != null) {
            ClientPlayNetworking.send(new CraftEnginePayload(buf.array()));
        } else if (ClientNetworkingImpl.getClientConfigurationAddon() != null) {
            ClientConfigurationNetworking.send(new CraftEnginePayload(buf.array()));
        }
    }

    private void handleReceiver(CraftEnginePayload payload, Object context) {
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
        networkData.handle(Context.of(context));
    }
}
