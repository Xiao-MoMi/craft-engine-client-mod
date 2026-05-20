package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.momirealms.craftengine.fabric.block.BlockManager;
import net.momirealms.craftengine.fabric.network.ClientCustomPacket;
import net.momirealms.craftengine.fabric.network.Context;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public final class ClientboundVisualBlockStateBatchFinishedPacket implements ClientCustomPacket {
    public static final ClientboundVisualBlockStateBatchFinishedPacket INSTANCE = new ClientboundVisualBlockStateBatchFinishedPacket();
    public static final ResourceLocation ID = ResourceLocation.tryBuild("craftengine", "visual_block_state_batch_finished");
    public static final Type<ClientboundVisualBlockStateBatchFinishedPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ClientboundVisualBlockStateBatchFinishedPacket> CODEC = ClientCustomPacket.codec(
            ($, $$) -> {
            },
            $ -> INSTANCE
    );

    private ClientboundVisualBlockStateBatchFinishedPacket() {
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, ClientboundVisualBlockStateBatchFinishedPacket> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Type<ClientboundVisualBlockStateBatchFinishedPacket> type() {
        return TYPE;
    }

    @Override
    public void handle(Context context) {
        BlockManager.instance().handleVisualBlockStateBatchFinished();
    }
}
