package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.momirealms.craftengine.fabric.config.ModConfig;
import net.momirealms.craftengine.fabric.network.ClientCustomPacket;
import net.momirealms.craftengine.fabric.network.Context;
import net.momirealms.craftengine.fabric.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public final class ClientboundCancelBlockUpdateResponsePacket implements ClientCustomPacket {
    public static final ClientboundCancelBlockUpdateResponsePacket INSTANCE = new ClientboundCancelBlockUpdateResponsePacket();
    public static final Identifier ID = Identifier.fromNamespaceAndPath("craftengine", "cancel_block_update_response");
    public static final Type<ClientboundCancelBlockUpdateResponsePacket> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ClientboundCancelBlockUpdateResponsePacket> CODEC = ClientCustomPacket.codec(
            ($, $$) -> {
            },
            $ -> INSTANCE
    );

    private ClientboundCancelBlockUpdateResponsePacket() {
    }

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, ClientboundCancelBlockUpdateResponsePacket> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Type<ClientboundCancelBlockUpdateResponsePacket> type() {
        return TYPE;
    }

    @Override
    public void handle(Context context) {
        ModConfig.INSTANCE.enableCancelBlockUpdate(true);
        NetworkManager.instance().serverInstalled(true);
    }
}
