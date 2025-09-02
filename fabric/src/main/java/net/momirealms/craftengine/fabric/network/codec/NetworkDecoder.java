package net.momirealms.craftengine.fabric.network.codec;

public interface NetworkDecoder<I, T> {
    T decode(I in);
}
