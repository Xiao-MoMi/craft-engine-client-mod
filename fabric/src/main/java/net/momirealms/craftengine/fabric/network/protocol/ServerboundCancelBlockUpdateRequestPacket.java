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
public final class ServerboundCancelBlockUpdateRequestPacket implements ServerCustomPacket {
    public static final ServerboundCancelBlockUpdateRequestPacket INSTANCE = new ServerboundCancelBlockUpdateRequestPacket();
    public static final ResourceLocation ID = ResourceLocation.tryBuild("craftengine", "cancel_block_update_request");
    public static final NetworkCodec<FriendlyByteBuf, ServerboundCancelBlockUpdateRequestPacket> CODEC = ServerCustomPacket.codec(
            ($, $$) -> {
            },
            $ -> INSTANCE
    );
    public static final PacketType<ServerboundCancelBlockUpdateRequestPacket> TYPE = PacketType.create(ID, CODEC::decode);

    private ServerboundCancelBlockUpdateRequestPacket() {
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    @Override
    public NetworkCodec<FriendlyByteBuf, ServerboundCancelBlockUpdateRequestPacket> codec() {
        return CODEC;
    }

    @Override
    public PacketType<ServerboundCancelBlockUpdateRequestPacket> getType() {
        return TYPE;
    }
}
