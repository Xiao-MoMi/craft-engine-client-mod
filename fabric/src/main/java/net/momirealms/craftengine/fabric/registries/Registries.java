package net.momirealms.craftengine.fabric.registries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.momirealms.craftengine.fabric.network.ClientCustomPacket;
import net.momirealms.craftengine.fabric.network.ServerCustomPacket;

@Environment(EnvType.CLIENT)
public class Registries {
    private Registries() {}

    public static final ResourceKey<Registry<StreamCodec<FriendlyByteBuf, ? extends ClientCustomPacket>>> CLIENT_MOD_PACKET = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("craftengine", "client_mod_packet"));
    public static final ResourceKey<Registry<StreamCodec<FriendlyByteBuf, ? extends ServerCustomPacket>>> SERVER_MOD_PACKET = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("craftengine", "server_mod_packet"));

}
