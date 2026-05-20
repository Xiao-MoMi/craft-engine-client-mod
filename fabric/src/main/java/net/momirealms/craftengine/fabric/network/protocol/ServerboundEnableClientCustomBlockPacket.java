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
public record ServerboundEnableClientCustomBlockPacket(int vanillaSize, int currentSize) implements ServerCustomPacket {
    public static final ResourceLocation ID = ResourceLocation.tryBuild("craftengine", "enable_client_custom_block");
    public static final NetworkCodec<FriendlyByteBuf, ServerboundEnableClientCustomBlockPacket> CODEC = ServerCustomPacket.codec(
            (packet, buf) -> {
                buf.writeVarInt(packet.vanillaSize);
                buf.writeVarInt(packet.currentSize);
            },
            buf -> new ServerboundEnableClientCustomBlockPacket(
                    buf.readVarInt(),
                    buf.readVarInt()
            )
    );
    public static final PacketType<ServerboundEnableClientCustomBlockPacket> TYPE = PacketType.create(ID, CODEC::decode);

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    @Override
    public NetworkCodec<FriendlyByteBuf, ServerboundEnableClientCustomBlockPacket> codec() {
        return CODEC;
    }

    @Override
    public PacketType<ServerboundEnableClientCustomBlockPacket> getType() {
        return TYPE;
    }
}
