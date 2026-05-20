package net.momirealms.craftengine.fabric.network;

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
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.config.ModConfig;
import net.momirealms.craftengine.fabric.network.protocol.*;
import net.momirealms.craftengine.fabric.registries.BuiltInRegistries;
import net.momirealms.craftengine.fabric.registries.Registries;
import net.momirealms.craftengine.fabric.util.BlockStateUtils;

@Environment(EnvType.CLIENT)
public class NetworkManager {
    public static final int PROTOCOL_VERSION = 1;
    public static final StreamCodec<FriendlyByteBuf, ClientboundVisualBlockStateBatchStartPacket> VISUAL_BLOCK_STATE_BATCH_START = registerClientbound(ClientboundVisualBlockStateBatchStartPacket.TYPE, ClientboundVisualBlockStateBatchStartPacket.CODEC);
    public static final StreamCodec<FriendlyByteBuf, ClientboundVisualBlockStateBatchFinishedPacket> VISUAL_BLOCK_STATE_BATCH_FINISHED = registerClientbound(ClientboundVisualBlockStateBatchFinishedPacket.TYPE, ClientboundVisualBlockStateBatchFinishedPacket.CODEC);
    public static final StreamCodec<FriendlyByteBuf, ClientboundVisualBlockStatesPacket> VISUAL_BLOCK_STATES = registerClientbound(ClientboundVisualBlockStatesPacket.TYPE, ClientboundVisualBlockStatesPacket.CODEC);
    public static final StreamCodec<FriendlyByteBuf, ClientboundCancelBlockUpdateResponsePacket> CANCEL_BLOCK_UPDATE_RESPONSE = registerClientbound(ClientboundCancelBlockUpdateResponsePacket.TYPE, ClientboundCancelBlockUpdateResponsePacket.CODEC);
    public static final StreamCodec<FriendlyByteBuf, ClientboundCreativeModeTabItemsPacket> CREATIVE_MODE_TAB_ITEMS = registerClientbound(ClientboundCreativeModeTabItemsPacket.TYPE, ClientboundCreativeModeTabItemsPacket.CODEC);
    public static final StreamCodec<FriendlyByteBuf, ServerboundHandshakePacket> HANDSHAKE = registerServerbound(ServerboundHandshakePacket.TYPE, ServerboundHandshakePacket.CODEC);
    public static final StreamCodec<FriendlyByteBuf, ServerboundEnableClientCustomBlockPacket> ENABLE_CLIENT_CUSTOM_BLOCK = registerServerbound(ServerboundEnableClientCustomBlockPacket.TYPE, ServerboundEnableClientCustomBlockPacket.CODEC);
    public static final StreamCodec<FriendlyByteBuf, ServerboundCancelBlockUpdateRequestPacket> CANCEL_BLOCK_UPDATE_REQUEST = registerServerbound(ServerboundCancelBlockUpdateRequestPacket.TYPE, ServerboundCancelBlockUpdateRequestPacket.CODEC);
    private static NetworkManager instance;
    private final CraftEngineFabricMod mod;
    private boolean serverInstalled = false;

    public NetworkManager(CraftEngineFabricMod mod) {
        instance = this;
        this.mod = mod;
        ClientConfigurationConnectionEvents.START.register(this::initChannel);
        ClientPlayConnectionEvents.DISCONNECT.register((_, _) -> serverInstalled(false));
    }

    public static NetworkManager instance() {
        return instance;
    }

    public static <T extends ClientCustomPacket> StreamCodec<FriendlyByteBuf, T> registerClientbound(CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> codec) {
        ((WritableRegistry<StreamCodec<FriendlyByteBuf, ? extends ClientCustomPacket>>) BuiltInRegistries.CLIENT_MOD_PACKET)
                .register(ResourceKey.create(Registries.CLIENT_MOD_PACKET, type.id()), codec, RegistrationInfo.BUILT_IN);
        PayloadTypeRegistry.clientboundConfiguration().register(type, codec);
        PayloadTypeRegistry.clientboundPlay().register(type, codec);
        ClientConfigurationNetworking.registerGlobalReceiver(type, (payload, context) -> payload.handle(Context.of(context)));
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> payload.handle(Context.of(context)));
        return codec;
    }

    public static <T extends ServerCustomPacket> StreamCodec<FriendlyByteBuf, T> registerServerbound(CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> codec) {
        ((WritableRegistry<StreamCodec<FriendlyByteBuf, ? extends ServerCustomPacket>>) BuiltInRegistries.SERVER_MOD_PACKET)
                .register(ResourceKey.create(Registries.SERVER_MOD_PACKET, type.id()), codec, RegistrationInfo.BUILT_IN);
        PayloadTypeRegistry.serverboundConfiguration().register(type, codec);
        PayloadTypeRegistry.serverboundPlay().register(type, codec);
        return codec;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean serverInstalled() {
        return this.serverInstalled;
    }

    public void serverInstalled(boolean serverInstalled) {
        this.serverInstalled = serverInstalled;
    }

    private void initChannel(ClientConfigurationPacketListenerImpl handler, Minecraft client) {
        sendCustomPacket(new ServerboundHandshakePacket(PROTOCOL_VERSION, Block.BLOCK_STATE_REGISTRY.size()));
        if (ModConfig.INSTANCE.enableClientCustomBlock()) {
            sendCustomPacket(new ServerboundEnableClientCustomBlockPacket(BlockStateUtils.vanillaStateSize(), Block.BLOCK_STATE_REGISTRY.size()));
        } else if (ModConfig.INSTANCE.enableCancelBlockUpdate()) {
            sendCustomPacket(ServerboundCancelBlockUpdateRequestPacket.INSTANCE);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public void sendCustomPacket(ServerCustomPacket data) {
        if (Minecraft.getInstance().player != null) {
            ClientPlayNetworking.send(data);
        } else if (ClientNetworkingImpl.getClientConfigurationAddon() != null) {
            ClientConfigurationNetworking.send(data);
        }
    }
}
