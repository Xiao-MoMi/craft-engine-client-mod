package net.momirealms.craftengine.fabric.network;

import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;
import net.momirealms.craftengine.fabric.network.codec.NetworkDecoder;
import net.momirealms.craftengine.fabric.network.codec.NetworkMemberEncoder;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public interface ClientCustomPacket extends FabricPacket {

    static <B extends ByteBuf, T extends ClientCustomPacket> NetworkCodec<B, T> codec(NetworkMemberEncoder<B, T> networkMemberEncoder, NetworkDecoder<B, T> networkDecoder) {
        return NetworkCodec.ofMember(networkMemberEncoder, networkDecoder);
    }

    default void write(FriendlyByteBuf friendlyByteBuf) {
        @SuppressWarnings("unchecked")
        var codec = (NetworkCodec<FriendlyByteBuf, ClientCustomPacket>) codec();
        codec.encode(friendlyByteBuf, this);
    }

    @NotNull ResourceLocation id();

    NetworkCodec<FriendlyByteBuf, ? extends ClientCustomPacket> codec();

    PacketType<? extends ClientCustomPacket> getType();

    default void handle(Context context) {
    }
}
