package net.momirealms.craftengine.fabric.network;

import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;
import net.momirealms.craftengine.fabric.network.codec.NetworkDecoder;
import net.momirealms.craftengine.fabric.network.codec.NetworkMemberEncoder;

@Environment(EnvType.CLIENT)
public interface ModPacket {

    ResourceKey<NetworkCodec<FriendlyByteBuf, ? extends ModPacket>> type();

    default void handle(Context context) {
    }

    static <B extends ByteBuf, T extends ModPacket> NetworkCodec<B, T> codec(NetworkMemberEncoder<B, T> streamMemberEncoder, NetworkDecoder<B, T> streamDecoder) {
        return NetworkCodec.ofMember(streamMemberEncoder, streamDecoder);
    }

}
