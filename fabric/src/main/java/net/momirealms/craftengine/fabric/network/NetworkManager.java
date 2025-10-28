package net.momirealms.craftengine.fabric.network;

import com.mojang.serialization.Lifecycle;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.momirealms.craftengine.fabric.config.ModConfig;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;
import net.momirealms.craftengine.fabric.network.protocol.CancelBlockUpdatePacket;
import net.momirealms.craftengine.fabric.network.protocol.ClientBlockStateSizePacket;
import net.momirealms.craftengine.fabric.network.protocol.ClientCustomBlockPacket;
import net.momirealms.craftengine.fabric.network.protocol.VisualBlockStatePacket;
import net.momirealms.craftengine.fabric.registries.BuiltInRegistries;
import net.momirealms.craftengine.fabric.util.BlockStateUtils;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class NetworkManager {
    public static final ResourceLocation CRAFTENGINE_PAYLOAD = Objects.requireNonNull(ResourceLocation.tryBuild("craftengine", "payload"));
    private static NetworkManager instance;
    private final CraftEngineFabricMod mod;
    private boolean serverInstalled = false;

    public NetworkManager(CraftEngineFabricMod mod) {
        instance = this;
        this.mod = mod;
        registerDataTypes();
        ClientPlayNetworking.registerGlobalReceiver(CRAFTENGINE_PAYLOAD, this::handleReceiver);
        ClientPlayConnectionEvents.JOIN.register(this::initChannel);
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

    public static <T extends ModPacket> void registerDataType(ResourceKey<NetworkCodec<FriendlyByteBuf, ? extends ModPacket>> key, NetworkCodec<FriendlyByteBuf, T> codec) {
        ((WritableRegistry<NetworkCodec<FriendlyByteBuf, ? extends ModPacket>>) BuiltInRegistries.MOD_PACKET).register(key, codec, Lifecycle.stable());
    }

    private void initChannel(ClientPacketListener handler, PacketSender sender, Minecraft client) {
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
        NetworkCodec<FriendlyByteBuf, ModPacket> codec = (NetworkCodec<FriendlyByteBuf, ModPacket>) BuiltInRegistries.MOD_PACKET.get(data.type());
        if (codec == null) {
            this.mod.logger().warn("Unknown data type class: " + data.getClass().getName());
            return;
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeByte(BuiltInRegistries.MOD_PACKET.getId(codec));
        codec.encode(buf, data);
        if (Minecraft.getInstance().player != null) {
            ClientPlayNetworking.send(CRAFTENGINE_PAYLOAD, buf);
        }
    }

    private void handleReceiver(Minecraft client, ClientPacketListener handler, FriendlyByteBuf byteBuf, PacketSender responseSender) {
        byte type = byteBuf.readByte();
        @SuppressWarnings("unchecked")
        NetworkCodec<FriendlyByteBuf, ModPacket> codec = (NetworkCodec<FriendlyByteBuf, ModPacket>) BuiltInRegistries.MOD_PACKET.byId(type);
        if (codec == null) {
            this.mod.logger().warn("Unknown data type received: " + type);
            return;
        }

        ModPacket networkData = codec.decode(byteBuf);
        networkData.handle(Context.of(client, handler, responseSender));
    }
}
