package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.momirealms.craftengine.fabric.block.BlockManager;
import net.momirealms.craftengine.fabric.network.ClientCustomPacket;
import net.momirealms.craftengine.fabric.network.Context;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public final class ClientboundVisualBlockStateBatchFinishedPacket implements ClientCustomPacket {
    public static final ClientboundVisualBlockStateBatchFinishedPacket INSTANCE = new ClientboundVisualBlockStateBatchFinishedPacket();
    public static final ResourceLocation ID = ResourceLocation.tryBuild("craftengine", "visual_block_state_batch_finished");
    public static final NetworkCodec<FriendlyByteBuf, ClientboundVisualBlockStateBatchFinishedPacket> CODEC = ClientCustomPacket.codec(
            ($, $$) -> {
            },
            $ -> INSTANCE
    );
    public static final PacketType<ClientboundVisualBlockStateBatchFinishedPacket> TYPE = PacketType.create(ID, CODEC::decode);

    private ClientboundVisualBlockStateBatchFinishedPacket() {
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    @Override
    public NetworkCodec<FriendlyByteBuf, ClientboundVisualBlockStateBatchFinishedPacket> codec() {
        return CODEC;
    }

    @Override
    public PacketType<ClientboundVisualBlockStateBatchFinishedPacket> getType() {
        return TYPE;
    }

    @Override
    public void handle(Context context) {
        BlockManager.instance().handleVisualBlockStateBatchFinished();
    }
}
