package net.momirealms.craftengine.fabric.network;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.WritableRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.config.ModConfig;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;
import net.momirealms.craftengine.fabric.network.protocol.*;
import net.momirealms.craftengine.fabric.registries.BuiltInRegistries;
import net.momirealms.craftengine.fabric.registries.Registries;
import net.momirealms.craftengine.fabric.util.BlockStateUtils;

@Environment(EnvType.CLIENT)
public class NetworkManager {
    public static final int PROTOCOL_VERSION = 1;
    public static final NetworkCodec<FriendlyByteBuf, ClientboundVisualBlockStateBatchStartPacket> VISUAL_BLOCK_STATE_BATCH_START = registerClientbound(ClientboundVisualBlockStateBatchStartPacket.TYPE, ClientboundVisualBlockStateBatchStartPacket.CODEC);
    public static final NetworkCodec<FriendlyByteBuf, ClientboundVisualBlockStateBatchFinishedPacket> VISUAL_BLOCK_STATE_BATCH_FINISHED = registerClientbound(ClientboundVisualBlockStateBatchFinishedPacket.TYPE, ClientboundVisualBlockStateBatchFinishedPacket.CODEC);
    public static final NetworkCodec<FriendlyByteBuf, ClientboundVisualBlockStatesPacket> VISUAL_BLOCK_STATES = registerClientbound(ClientboundVisualBlockStatesPacket.TYPE, ClientboundVisualBlockStatesPacket.CODEC);
    public static final NetworkCodec<FriendlyByteBuf, ClientboundCancelBlockUpdateResponsePacket> CANCEL_BLOCK_UPDATE_RESPONSE = registerClientbound(ClientboundCancelBlockUpdateResponsePacket.TYPE, ClientboundCancelBlockUpdateResponsePacket.CODEC);
    public static final NetworkCodec<FriendlyByteBuf, ClientboundCreativeModeTabItemsPacket> CREATIVE_MODE_TAB_ITEMS = registerClientbound(ClientboundCreativeModeTabItemsPacket.TYPE, ClientboundCreativeModeTabItemsPacket.CODEC);
    public static final NetworkCodec<FriendlyByteBuf, ServerboundHandshakePacket> HANDSHAKE = registerServerbound(ServerboundHandshakePacket.TYPE, ServerboundHandshakePacket.CODEC);
    public static final NetworkCodec<FriendlyByteBuf, ServerboundEnableClientCustomBlockPacket> ENABLE_CLIENT_CUSTOM_BLOCK = registerServerbound(ServerboundEnableClientCustomBlockPacket.TYPE, ServerboundEnableClientCustomBlockPacket.CODEC);
    public static final NetworkCodec<FriendlyByteBuf, ServerboundCancelBlockUpdateRequestPacket> CANCEL_BLOCK_UPDATE_REQUEST = registerServerbound(ServerboundCancelBlockUpdateRequestPacket.TYPE, ServerboundCancelBlockUpdateRequestPacket.CODEC);
    private static NetworkManager instance;
    private final CraftEngineFabricMod mod;
    private boolean serverInstalled = false;

    public NetworkManager(CraftEngineFabricMod mod) {
        instance = this;
        this.mod = mod;
        ClientPlayConnectionEvents.JOIN.register(this::initChannel);
        ClientPlayConnectionEvents.DISCONNECT.register(($, $$) -> serverInstalled(false));
    }

    public static NetworkManager instance() {
        return instance;
    }

    public static <T extends ClientCustomPacket> NetworkCodec<FriendlyByteBuf, T> registerClientbound(PacketType<T> type, NetworkCodec<FriendlyByteBuf, T> codec) {
        ((WritableRegistry<NetworkCodec<FriendlyByteBuf, ? extends ClientCustomPacket>>) BuiltInRegistries.CLIENT_MOD_PACKET)
                .register(ResourceKey.create(Registries.CLIENT_MOD_PACKET, type.getId()), codec, Lifecycle.stable());
        ClientPlayNetworking.registerGlobalReceiver(type, (packet, player, responseSender) -> packet.handle(Context.of(player, responseSender)));
        return codec;
    }

    public static <T extends ServerCustomPacket> NetworkCodec<FriendlyByteBuf, T> registerServerbound(PacketType<T> type, NetworkCodec<FriendlyByteBuf, T> codec) {
        ((WritableRegistry<NetworkCodec<FriendlyByteBuf, ? extends ServerCustomPacket>>) BuiltInRegistries.SERVER_MOD_PACKET)
                .register(ResourceKey.create(Registries.SERVER_MOD_PACKET, type.getId()), codec, Lifecycle.stable());
        return codec;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean serverInstalled() {
        return this.serverInstalled;
    }

    public void serverInstalled(boolean serverInstalled) {
        this.serverInstalled = serverInstalled;
    }

    private void initChannel(ClientPacketListener handler, PacketSender sender, Minecraft client) {
        sendCustomPacket(new ServerboundHandshakePacket(PROTOCOL_VERSION, Block.BLOCK_STATE_REGISTRY.size()));
        if (ModConfig.INSTANCE.enableClientCustomBlock()) {
            sendCustomPacket(new ServerboundEnableClientCustomBlockPacket(BlockStateUtils.vanillaStateSize(), Block.BLOCK_STATE_REGISTRY.size()));
        } else if (ModConfig.INSTANCE.enableCancelBlockUpdate()) {
            sendCustomPacket(ServerboundCancelBlockUpdateRequestPacket.INSTANCE);
        }
    }

    public void sendCustomPacket(ServerCustomPacket data) {
        if (Minecraft.getInstance().player != null) {
            ClientPlayNetworking.getSender().sendPacket(data);
        }
    }
}
