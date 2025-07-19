package net.momirealms.craftengine.fabric.client.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.Objects;

public record CraftEnginePayload(byte[] data) implements CustomPayload {
    public static final Identifier CRAFTENGINE_PAYLOAD = Objects.requireNonNull(Identifier.of("craftengine", "payload"));

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBytes(data);
    }

    @Override
    public Identifier id() {
        return CRAFTENGINE_PAYLOAD;
    }
}
