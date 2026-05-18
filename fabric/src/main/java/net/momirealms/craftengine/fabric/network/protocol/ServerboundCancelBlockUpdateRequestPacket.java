package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.momirealms.craftengine.fabric.network.ServerCustomPacket;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public final class ServerboundCancelBlockUpdateRequestPacket implements ServerCustomPacket {
    public static final ServerboundCancelBlockUpdateRequestPacket INSTANCE = new ServerboundCancelBlockUpdateRequestPacket();
    public static final Identifier ID = Identifier.fromNamespaceAndPath("craftengine", "cancel_block_update_request");
    public static final Type<ServerboundCancelBlockUpdateRequestPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ServerboundCancelBlockUpdateRequestPacket> CODEC = ServerCustomPacket.codec(
            (_, _) -> {
            },
            _ -> INSTANCE
    );

    private ServerboundCancelBlockUpdateRequestPacket() {
    }

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, ServerboundCancelBlockUpdateRequestPacket> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Type<? extends ServerCustomPacket> type() {
        return TYPE;
    }
}
