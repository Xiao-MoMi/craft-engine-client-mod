package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.momirealms.craftengine.fabric.client.config.ModConfig;
import net.momirealms.craftengine.fabric.network.Data;
import net.momirealms.craftengine.fabric.network.NetworkManager;

public record CancelBlockUpdateData(boolean enabled) implements Data {
    public static final StreamCodec<FriendlyByteBuf, CancelBlockUpdateData> CODEC = Data.codec(
            CancelBlockUpdateData::encode,
            CancelBlockUpdateData::new
    );

    private CancelBlockUpdateData(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    private void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.enabled);
    }

    @Override
    public void handle(ClientConfigurationNetworking.Context context) {
        ModConfig.enableCancelBlockUpdate = this.enabled;
        NetworkManager.serverInstalled = this.enabled;
    }
}
