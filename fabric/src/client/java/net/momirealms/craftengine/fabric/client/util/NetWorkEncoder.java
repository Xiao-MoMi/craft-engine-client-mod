package net.momirealms.craftengine.fabric.client.util;

import io.netty.buffer.ByteBuf;

public interface NetWorkEncoder<T> {
    void encode(ByteBuf out, T value);
}
