package net.momirealms.craftengine.fabric.network.codec;

import java.util.function.Function;

public interface NetworkCodec<B, T> extends NetworkEncoder<B, T>, NetworkDecoder<B, T> {

    default <V> NetworkCodec<B, V> map(Function<? super T, ? extends V> factory, Function<? super V, ? extends T> getter) {
        return new NetworkCodec<>() {
            @Override
            public V decode(B in) {
                return factory.apply(NetworkCodec.this.decode(in));
            }

            @Override
            public void encode(B out, V value) {
                NetworkCodec.this.encode(out, getter.apply(value));
            }
        };
    }

    static <B, V> NetworkCodec<B, V> ofMember(final NetworkMemberEncoder<B, V> networkMemberEncoder, final NetworkDecoder<B, V> networkDecoder) {
        return new NetworkCodec<>() {
            public V decode(B in) {
                return networkDecoder.decode(in);
            }

            public void encode(B out, V value) {
                networkMemberEncoder.encode(value, out);
            }
        };
    }
}
