package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.momirealms.craftengine.fabric.network.ModPacket;
import net.momirealms.craftengine.fabric.registries.BuiltInRegistries;

@Environment(EnvType.CLIENT)
public record ClientCustomBlockPacket(int vanillaSize, int currentSize) implements ModPacket {
    public static final ResourceKey<StreamCodec<FriendlyByteBuf, ? extends ModPacket>> TYPE = ResourceKey.create(
            BuiltInRegistries.MOD_PACKET.key(), Identifier.fromNamespaceAndPath("craftengine", "client_custom_block")
    );
    public static final StreamCodec<FriendlyByteBuf, ClientCustomBlockPacket> CODEC = ModPacket.codec(
            ClientCustomBlockPacket::encode,
            ClientCustomBlockPacket::new
    );

    private ClientCustomBlockPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt());
    }

    private void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.vanillaSize);
        buf.writeInt(this.currentSize);
    }

    @Override
    public ResourceKey<StreamCodec<FriendlyByteBuf, ? extends ModPacket>> type() {
        return TYPE;
    }

}
