package net.momirealms.craftengine.fabric.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record CraftEnginePayload(byte[] data) implements CustomPacketPayload {
    public static final ResourceLocation CRAFTENGINE_PAYLOAD = Objects.requireNonNull(ResourceLocation.tryBuild("craftengine", "payload"));

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBytes(data);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return CRAFTENGINE_PAYLOAD;
    }
}
