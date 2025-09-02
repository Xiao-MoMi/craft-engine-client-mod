package net.momirealms.craftengine.fabric.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record CraftEnginePayload(byte[] data) implements CustomPacketPayload {
    public static final ResourceLocation CRAFTENGINE_PAYLOAD = ResourceLocation.tryBuild("craftengine", "payload");
    public static final Type<CraftEnginePayload> TYPE = new Type<>(CraftEnginePayload.CRAFTENGINE_PAYLOAD);
    public static final StreamCodec<FriendlyByteBuf, CraftEnginePayload> CODEC = StreamCodec.of(
            (byteBuf, payload) -> byteBuf.writeBytes(payload.data()),
            buf -> {
                int i = buf.readableBytes();
                byte[] data = new byte[i];
                buf.readBytes(data);
                return new CraftEnginePayload(data);
            });

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
