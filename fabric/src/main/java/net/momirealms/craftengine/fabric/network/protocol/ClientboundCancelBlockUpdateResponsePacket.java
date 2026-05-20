package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.momirealms.craftengine.fabric.config.ModConfig;
import net.momirealms.craftengine.fabric.network.ClientCustomPacket;
import net.momirealms.craftengine.fabric.network.Context;
import net.momirealms.craftengine.fabric.network.NetworkManager;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public final class ClientboundCancelBlockUpdateResponsePacket implements ClientCustomPacket {
    public static final ClientboundCancelBlockUpdateResponsePacket INSTANCE = new ClientboundCancelBlockUpdateResponsePacket();
    public static final ResourceLocation ID = ResourceLocation.tryBuild("craftengine", "cancel_block_update_response");
    public static final NetworkCodec<FriendlyByteBuf, ClientboundCancelBlockUpdateResponsePacket> CODEC = ClientCustomPacket.codec(
            ($, $$) -> {
            },
            $ -> INSTANCE
    );
    public static final PacketType<ClientboundCancelBlockUpdateResponsePacket> TYPE = PacketType.create(ID, CODEC::decode);

    private ClientboundCancelBlockUpdateResponsePacket() {
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        CODEC.encode(friendlyByteBuf, this);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    @Override
    public NetworkCodec<FriendlyByteBuf, ClientboundCancelBlockUpdateResponsePacket> codec() {
        return CODEC;
    }

    @Override
    public PacketType<ClientboundCancelBlockUpdateResponsePacket> getType() {
        return TYPE;
    }

    @Override
    public void handle(Context context) {
        ModConfig.INSTANCE.enableCancelBlockUpdate(true);
        NetworkManager.instance().serverInstalled(true);
    }
}
