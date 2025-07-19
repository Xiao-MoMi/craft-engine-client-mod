package net.momirealms.craftengine.neoforge.client.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;

public enum NetWorkDataTypes {
    CLIENT_CUSTOM_BLOCK(ByteBufCodecs.INT),
    CANCEL_BLOCK_UPDATE(ByteBufCodecs.BOOL);

    private final StreamCodec<?, ?> codec;

    NetWorkDataTypes(StreamCodec<?, ?> codec) {
        this.codec = codec;
    }

    public StreamCodec<?, ?> codec() {
        return codec;
    }

    @SuppressWarnings("unchecked")
    public <B extends ByteBuf, V> V decode(B buf) {
        return ((StreamCodec<B, V>) codec).decode(buf);
    }

    @SuppressWarnings("unchecked")
    public <B extends ByteBuf, V> void encode(B buf, V value) {
        ((StreamCodec<B, V>) codec).encode(buf, value);
    }
}