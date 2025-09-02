package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.momirealms.craftengine.fabric.client.config.ModConfig;
import net.momirealms.craftengine.fabric.network.ModPacket;
import net.momirealms.craftengine.fabric.network.NetworkManager;
import net.momirealms.craftengine.fabric.registries.BuiltInRegistries;

public record CancelBlockUpdatePacket(boolean enabled) implements ModPacket {
    public static final ResourceKey<StreamCodec<FriendlyByteBuf, ? extends ModPacket>> TYPE = ResourceKey.create(
            BuiltInRegistries.MOD_PACKET.key(), ResourceLocation.fromNamespaceAndPath("craftengine", "cancel_block_update")
    );
    public static final StreamCodec<FriendlyByteBuf, CancelBlockUpdatePacket> CODEC = ModPacket.codec(
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
    public ResourceKey<StreamCodec<FriendlyByteBuf, ? extends ModPacket>> type() {
        return TYPE;
    }

    @Override
    public void handle(ClientConfigurationNetworking.Context context) {
        ModConfig.enableCancelBlockUpdate = this.enabled;
        NetworkManager.serverInstalled = this.enabled;
    }
}
