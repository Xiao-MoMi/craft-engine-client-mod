package net.momirealms.craftengine.fabric.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public record CraftEnginePayload(byte[] data) implements CustomPacketPayload {
    public static final Identifier CRAFTENGINE_PAYLOAD = Identifier.fromNamespaceAndPath("craftengine", "payload");
    public static final Type<@NotNull CraftEnginePayload> TYPE = new Type<>(CraftEnginePayload.CRAFTENGINE_PAYLOAD);
    public static final StreamCodec<FriendlyByteBuf, CraftEnginePayload> CODEC = StreamCodec.of(
            (byteBuf, payload) -> byteBuf.writeBytes(payload.data()),
            buf -> {
                int i = buf.readableBytes();
                byte[] data = new byte[i];
                buf.readBytes(data);
                return new CraftEnginePayload(data);
            });

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}
