package net.momirealms.craftengine.fabric.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.client.config.ModConfig;
import net.momirealms.craftengine.fabric.network.protocol.CancelBlockUpdateData;
import net.momirealms.craftengine.fabric.network.protocol.ClientBlockStateSizeData;
import net.momirealms.craftengine.fabric.network.protocol.ClientCustomBlockData;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class NetworkManager {
    public static boolean serverInstalled = false;
    private final CraftEngineFabricMod mod;
    private static final Map<Class<Data>, Byte> classToType = new HashMap<>();
    private static final Map<Byte, StreamCodec<FriendlyByteBuf, Data>> typeToCodec = new HashMap<>();
    private static final AtomicInteger typeCounter = new AtomicInteger(0);

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
        registerDataType(ClientCustomBlockData.class, ClientCustomBlockData.CODEC);
        registerDataType(CancelBlockUpdateData.class, CancelBlockUpdateData.CODEC);
        registerDataType(ClientBlockStateSizeData.class, ClientBlockStateSizeData.CODEC);
    }

    @SuppressWarnings("unchecked")
    private <T extends Data> void registerDataType(Class<T> dataClass, StreamCodec<FriendlyByteBuf, T> codec) {
        if (classToType.containsKey(dataClass)) {
            this.mod.logger().warn("Duplicate data type class: " + dataClass.getName());
            return;
        }
        int next = typeCounter.getAndIncrement();
        if (next > 255) {
            throw new IllegalStateException("Too many data types registered, byte index overflow (max 256)");
        }
        byte type = (byte) next;
        classToType.put((Class<Data>) dataClass, type);
        typeToCodec.put(type, (StreamCodec<FriendlyByteBuf, Data>) codec);
    }

    private void initChannel(ClientConfigurationPacketListenerImpl handler, Minecraft client) {
        sendData(new ClientBlockStateSizeData(Block.BLOCK_STATE_REGISTRY.size()));

        if (!ModConfig.enableNetwork && !ModConfig.enableCancelBlockUpdate) {
            return;
        }

        if (ModConfig.enableNetwork) {
            sendData(new ClientCustomBlockData(Block.BLOCK_STATE_REGISTRY.size()));
        } else {
            sendData(new CancelBlockUpdateData(true));
        }
    }

    public void sendData(Data data) {
        Class<? extends Data> dataClass = data.getClass();
        Byte type = classToType.get(dataClass);
        if (type == null) {
            CraftEngineFabricMod.instance().logger().warn("Unknown data type class: " + dataClass.getName());
            return;
        }
        StreamCodec<FriendlyByteBuf, Data> codec = typeToCodec.get(type);
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeByte(type);
        codec.encode(buf, data);
        ClientConfigurationNetworking.send(new CraftEnginePayload(buf.array()));
    }

    private void handleReceiver(CraftEnginePayload payload, ClientConfigurationNetworking.Context context) {
        byte[] data = payload.data();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
        byte type = buf.readByte();
        StreamCodec<FriendlyByteBuf, Data> codec = typeToCodec.get(type);
        if (codec == null) {
            CraftEngineFabricMod.instance().logger().warn("Unknown data type received: " + type);
            return;
        }

        Data networkData = codec.decode(buf);
        networkData.handle(context);
    }
}
