package net.momirealms.craftengine.fabric.client.util;

import io.netty.buffer.ByteBuf;

public interface NetWorkDecoder<T> {
    T decode(ByteBuf in);
}
