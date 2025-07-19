package net.momirealms.craftengine.fabric.client.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public enum NetWorkDataTypes {
    CLIENT_CUSTOM_BLOCK(PacketCodecs.INTEGER),
    CANCEL_BLOCK_UPDATE(PacketCodecs.BOOL);

    private final PacketCodec<?, ?> codec;

    NetWorkDataTypes(PacketCodec<?, ?> codec) {
        this.codec = codec;
    }

    public PacketCodec<?, ?> codec() {
        return codec;
    }

    @SuppressWarnings("unchecked")
    public <B extends ByteBuf, V> V decode(B buf) {
        return ((PacketCodec<B, V>) codec).decode(buf);
    }

    @SuppressWarnings("unchecked")
    public <B extends ByteBuf, V> void encode(B buf, V value) {
        ((PacketCodec<B, V>) codec).encode(buf, value);
    }
}