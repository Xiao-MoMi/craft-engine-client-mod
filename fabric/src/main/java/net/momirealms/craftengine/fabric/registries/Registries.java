package net.momirealms.craftengine.fabric.registries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.momirealms.craftengine.fabric.network.ClientCustomPacket;
import net.momirealms.craftengine.fabric.network.ServerCustomPacket;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;

@Environment(EnvType.CLIENT)
public class Registries {
    private Registries() {}

    public static final ResourceKey<Registry<NetworkCodec<FriendlyByteBuf, ? extends ClientCustomPacket>>> CLIENT_MOD_PACKET = ResourceKey.createRegistryKey(ResourceLocation.tryBuild("craftengine", "client_mod_packet"));
    public static final ResourceKey<Registry<NetworkCodec<FriendlyByteBuf, ? extends ServerCustomPacket>>> SERVER_MOD_PACKET = ResourceKey.createRegistryKey(ResourceLocation.tryBuild("craftengine", "server_mod_packet"));

}
