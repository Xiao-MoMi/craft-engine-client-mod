package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.momirealms.craftengine.fabric.network.ServerCustomPacket;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public record ServerboundHandshakePacket(int protocolVersion, int blockListSize) implements ServerCustomPacket {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("craftengine", "handshake");
    public static final Type<ServerboundHandshakePacket> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ServerboundHandshakePacket> CODEC = ServerCustomPacket.codec(
            (packet, buf) -> {
                buf.writeVarInt(packet.protocolVersion);
                buf.writeVarInt(packet.blockListSize);
            },
            buf -> new ServerboundHandshakePacket(
                    buf.readVarInt(),
                    buf.readVarInt()
            )
    );

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, ServerboundHandshakePacket> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Type<ServerboundHandshakePacket> type() {
        return TYPE;
    }
}
