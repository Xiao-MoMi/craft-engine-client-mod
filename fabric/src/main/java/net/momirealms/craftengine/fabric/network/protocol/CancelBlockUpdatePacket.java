package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.momirealms.craftengine.fabric.client.config.ModConfig;
import net.momirealms.craftengine.fabric.network.ModPacket;
import net.momirealms.craftengine.fabric.network.NetworkManager;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;
import net.momirealms.craftengine.fabric.registries.BuiltInRegistries;

public record CancelBlockUpdatePacket(boolean enabled) implements ModPacket {
    public static final ResourceKey<NetworkCodec<FriendlyByteBuf, ? extends ModPacket>> TYPE = ResourceKey.create(
            BuiltInRegistries.MOD_PACKET.key(), ResourceLocation.tryBuild("craftengine", "cancel_block_update")
    );
    public static final NetworkCodec<FriendlyByteBuf, CancelBlockUpdatePacket> CODEC = ModPacket.codec(
            CancelBlockUpdatePacket::encode,
            CancelBlockUpdatePacket::new
    );

    private CancelBlockUpdatePacket(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    private void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.enabled);
    }

    @Override
    public ResourceKey<NetworkCodec<FriendlyByteBuf, ? extends ModPacket>> type() {
        return TYPE;
    }

    @Override
    public void handle(Minecraft client, ClientPacketListener handler, PacketSender responseSender) {
        ModConfig.enableCancelBlockUpdate = this.enabled;
        NetworkManager.serverInstalled = this.enabled;
    }
}
