package net.momirealms.craftengine.fabric.network;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;
import net.momirealms.craftengine.fabric.network.codec.NetworkDecoder;
import net.momirealms.craftengine.fabric.network.codec.NetworkMemberEncoder;

public interface ModPacket {

    ResourceKey<NetworkCodec<FriendlyByteBuf, ? extends ModPacket>> type();

    default void handle(Minecraft client, ClientConfigurationPacketListenerImpl handler, PacketSender responseSender) {
    }

    static <B extends ByteBuf, T extends ModPacket> NetworkCodec<B, T> codec(NetworkMemberEncoder<B, T> streamMemberEncoder, NetworkDecoder<B, T> streamDecoder) {
        return NetworkCodec.ofMember(streamMemberEncoder, streamDecoder);
    }

}
