package net.momirealms.craftengine.fabric.registries;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.momirealms.craftengine.fabric.network.ClientCustomPacket;
import net.momirealms.craftengine.fabric.network.ServerCustomPacket;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;

@Environment(EnvType.CLIENT)
public class BuiltInRegistries {
    private BuiltInRegistries() {}

    public static final Registry<NetworkCodec<FriendlyByteBuf, ? extends ClientCustomPacket>> CLIENT_MOD_PACKET = registerSimple(Registries.CLIENT_MOD_PACKET);
    public static final Registry<NetworkCodec<FriendlyByteBuf, ? extends ServerCustomPacket>> SERVER_MOD_PACKET = registerSimple(Registries.SERVER_MOD_PACKET);

    private static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> resourceKey) {
        return new MappedRegistry<>(resourceKey, Lifecycle.stable(), false);
    }

}
