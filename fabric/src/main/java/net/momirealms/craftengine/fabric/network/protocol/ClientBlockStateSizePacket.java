package net.momirealms.craftengine.fabric.network.protocol;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.momirealms.craftengine.fabric.network.ModPacket;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;
import net.momirealms.craftengine.fabric.registries.BuiltInRegistries;

public record ClientBlockStateSizePacket(int blockStateSize) implements ModPacket {
    public static final ResourceKey<NetworkCodec<FriendlyByteBuf, ? extends ModPacket>> TYPE = ResourceKey.create(
            BuiltInRegistries.MOD_PACKET.key(), ResourceLocation.tryBuild("craftengine", "client_block_state_size")
    );
    public static final NetworkCodec<FriendlyByteBuf, ClientBlockStateSizePacket> CODEC = ModPacket.codec(
            ClientBlockStateSizePacket::encode,
            ClientBlockStateSizePacket::new
    );

    private ClientBlockStateSizePacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    private void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.blockStateSize);
    }

    @Override
    public ResourceKey<NetworkCodec<FriendlyByteBuf, ? extends ModPacket>> type() {
        return TYPE;
    }

}
