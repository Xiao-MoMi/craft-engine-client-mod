package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.momirealms.craftengine.fabric.network.ServerCustomPacket;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public record ServerboundEnableClientCustomBlockPacket(int vanillaSize, int currentSize) implements ServerCustomPacket {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("craftengine", "enable_client_custom_block");
    public static final Type<ServerboundEnableClientCustomBlockPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ServerboundEnableClientCustomBlockPacket> CODEC = ServerCustomPacket.codec(
            (packet, buf) -> {
                buf.writeVarInt(packet.vanillaSize);
                buf.writeVarInt(packet.currentSize);
            },
            buf -> new ServerboundEnableClientCustomBlockPacket(
                    buf.readVarInt(),
                    buf.readVarInt()
            )
    );

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, ServerboundEnableClientCustomBlockPacket> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Type<ServerboundEnableClientCustomBlockPacket> type() {
        return TYPE;
    }
}
