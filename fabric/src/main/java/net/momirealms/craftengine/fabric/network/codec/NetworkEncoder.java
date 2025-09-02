package net.momirealms.craftengine.fabric.network.codec;

public interface NetworkEncoder<O, T> {
    void encode(O out, T value);
}
