package net.momirealms.craftengine.fabric.registries;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.momirealms.craftengine.fabric.network.ModPacket;

public class Registries {
    private Registries() {}

    public static final ResourceKey<Registry<StreamCodec<FriendlyByteBuf, ? extends ModPacket>>> MOD_PACKET = ResourceKey.createRegistryKey(ResourceLocation.tryBuild("craftengine", "mod_packet"));

}
