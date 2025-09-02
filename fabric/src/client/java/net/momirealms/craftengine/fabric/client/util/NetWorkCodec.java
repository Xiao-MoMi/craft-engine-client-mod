package net.momirealms.craftengine.fabric.client.util;

import io.netty.buffer.ByteBuf;

import java.util.function.Function;

public interface NetWorkCodec<T> extends NetWorkEncoder<T>, NetWorkDecoder<T>  {

    default <O> NetWorkCodec<O> map(Function<? super T, ? extends O> factory, Function<? super O, ? extends T> getter) {
        return new NetWorkCodec<>() {
            @Override
            public O decode(ByteBuf in) {
                return factory.apply(NetWorkCodec.this.decode(in));
            }

            @Override
            public void encode(ByteBuf out, O value) {
                NetWorkCodec.this.encode(out, getter.apply(value));
            }
        };
    }
}
