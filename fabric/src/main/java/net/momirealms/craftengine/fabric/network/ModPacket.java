package net.momirealms.craftengine.fabric.network;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.codec.StreamMemberEncoder;
import net.minecraft.resources.ResourceKey;

public interface ModPacket {

    ResourceKey<StreamCodec<FriendlyByteBuf, ? extends ModPacket>> type();

    default void handle(ClientConfigurationNetworking.Context context) {
    }

    static <B extends ByteBuf, T extends ModPacket> StreamCodec<B, T> codec(StreamMemberEncoder<B, T> streamMemberEncoder, StreamDecoder<B, T> streamDecoder) {
        return StreamCodec.ofMember(streamMemberEncoder, streamDecoder);
    }

}
