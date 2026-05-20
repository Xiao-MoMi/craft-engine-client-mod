package net.momirealms.craftengine.fabric.registries;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.momirealms.craftengine.fabric.network.ClientCustomPacket;
import net.momirealms.craftengine.fabric.network.ServerCustomPacket;

@Environment(EnvType.CLIENT)
public class BuiltInRegistries {
    private BuiltInRegistries() {}

    public static final Registry<StreamCodec<FriendlyByteBuf, ? extends ClientCustomPacket>> CLIENT_MOD_PACKET = registerSimple(Registries.CLIENT_MOD_PACKET);
    public static final Registry<StreamCodec<FriendlyByteBuf, ? extends ServerCustomPacket>> SERVER_MOD_PACKET = registerSimple(Registries.SERVER_MOD_PACKET);

    private static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> resourceKey) {
        return new MappedRegistry<>(resourceKey, Lifecycle.stable(), false);
    }

}
