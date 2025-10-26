package net.momirealms.craftengine.fabric.registries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.momirealms.craftengine.fabric.network.ModPacket;

@Environment(EnvType.CLIENT)
public class Registries {
    private Registries() {}

    public static final ResourceKey<Registry<StreamCodec<FriendlyByteBuf, ? extends ModPacket>>> MOD_PACKET = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("craftengine", "mod_packet"));

}
