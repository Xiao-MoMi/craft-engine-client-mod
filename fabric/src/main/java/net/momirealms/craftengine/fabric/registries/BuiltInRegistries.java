package net.momirealms.craftengine.fabric.registries;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.momirealms.craftengine.fabric.network.ModPacket;

public class BuiltInRegistries {
    private BuiltInRegistries() {}

    public static final Registry<StreamCodec<FriendlyByteBuf, ? extends ModPacket>> MOD_PACKET = registerSimple(Registries.MOD_PACKET);

    private static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> resourceKey) {
        return new MappedRegistry<>(resourceKey, Lifecycle.stable(), false);
    }

}
