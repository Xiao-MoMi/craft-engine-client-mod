package net.momirealms.craftengine.fabric.network.protocol;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.momirealms.craftengine.fabric.network.Data;

public record ClientBlockStateSizeData(int blockStateSize) implements Data {
    public static final StreamCodec<FriendlyByteBuf, ClientBlockStateSizeData> CODEC = Data.codec(
            ClientBlockStateSizeData::encode,
            ClientBlockStateSizeData::new
    );

    private ClientBlockStateSizeData(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    private void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.blockStateSize);
    }

}
