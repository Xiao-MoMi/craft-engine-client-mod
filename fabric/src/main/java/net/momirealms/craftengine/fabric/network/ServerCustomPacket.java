package net.momirealms.craftengine.fabric.network;

import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.codec.StreamMemberEncoder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public interface ServerCustomPacket extends CustomPacketPayload {

    static <B extends ByteBuf, T extends ServerCustomPacket> StreamCodec<B, T> codec(StreamMemberEncoder<B, T> networkMemberEncoder, StreamDecoder<B, T> networkDecoder) {
        return StreamCodec.ofMember(networkMemberEncoder, networkDecoder);
    }

    Identifier id();

    StreamCodec<FriendlyByteBuf, ? extends ServerCustomPacket> codec();

    @NotNull Type<? extends ServerCustomPacket> type();

    default void handle(Context context) {
    }
}
