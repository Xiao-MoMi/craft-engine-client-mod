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
public record ClientboundVisualBlockStateBatchStartPacket(int size) implements ClientCustomPacket {
    public static final ResourceLocation ID = ResourceLocation.tryBuild("craftengine", "visual_block_state_batch_start");
    public static final NetworkCodec<FriendlyByteBuf, ClientboundVisualBlockStateBatchStartPacket> CODEC = ClientCustomPacket.codec(
            (packet, buf) -> buf.writeVarInt(packet.size),
            buf -> new ClientboundVisualBlockStateBatchStartPacket(buf.readVarInt())
    );
    public static final PacketType<ClientboundVisualBlockStateBatchStartPacket> TYPE = PacketType.create(ID, CODEC::decode);

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    @Override
    public NetworkCodec<FriendlyByteBuf, ClientboundVisualBlockStateBatchStartPacket> codec() {
        return CODEC;
    }

    @Override
    public PacketType<ClientboundVisualBlockStateBatchStartPacket> getType() {
        return TYPE;
    }

    @Override
    public void handle(Context context) {
        BlockManager.instance().handleVisualBlockStateBatchStart(this.size);
    }
}
