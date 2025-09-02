package net.momirealms.craftengine.fabric.network.codec;

@FunctionalInterface
public interface NetworkMemberEncoder<O, T> {
    void encode(T object, O object2);
}
