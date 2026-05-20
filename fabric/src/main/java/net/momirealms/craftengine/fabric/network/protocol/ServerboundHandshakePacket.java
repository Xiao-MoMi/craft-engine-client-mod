package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.momirealms.craftengine.fabric.network.ServerCustomPacket;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public record ServerboundHandshakePacket(int protocolVersion, int blockListSize) implements ServerCustomPacket {
    public static final ResourceLocation ID = ResourceLocation.tryBuild("craftengine", "handshake");
    public static final NetworkCodec<FriendlyByteBuf, ServerboundHandshakePacket> CODEC = ServerCustomPacket.codec(
            (packet, buf) -> {
                buf.writeVarInt(packet.protocolVersion);
                buf.writeVarInt(packet.blockListSize);
            },
            buf -> new ServerboundHandshakePacket(
                    buf.readVarInt(),
                    buf.readVarInt()
            )
    );
    public static final PacketType<ServerboundHandshakePacket> TYPE = PacketType.create(ID, CODEC::decode);

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    @Override
    public NetworkCodec<FriendlyByteBuf, ServerboundHandshakePacket> codec() {
        return CODEC;
    }

    @Override
    public PacketType<ServerboundHandshakePacket> getType() {
        return TYPE;
    }
}
