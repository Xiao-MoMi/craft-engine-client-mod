package net.momirealms.craftengine.fabric.client.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public class ByteArrayCodec implements PacketCodec<RegistryByteBuf, byte[]> {

    @Override
    public void encode(RegistryByteBuf buf, byte[] value) {
        buf.writeBytes(value);
    }

    @Override
    public byte[] decode(RegistryByteBuf buf) {
        int length = buf.readableBytes();
        byte[] data = new byte[length];
        buf.readBytes(data);
        return data;
    }
}