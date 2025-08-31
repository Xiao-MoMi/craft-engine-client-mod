package net.momirealms.craftengine.fabric.network.protocol;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.momirealms.craftengine.fabric.network.Data;

public record ClientCustomBlockData(int size) implements Data {
    public static final StreamCodec<FriendlyByteBuf, ClientCustomBlockData> CODEC = Data.codec(
            ClientCustomBlockData::encode,
            ClientCustomBlockData::new
    );

    private ClientCustomBlockData(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    private void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.size);
    }

}
