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

import java.util.Arrays;
import java.util.BitSet;

@Environment(EnvType.CLIENT)
public record ClientboundVisualBlockStatesPacket(int startIndex, int[] data) implements ClientCustomPacket {
    public static final ResourceLocation ID = ResourceLocation.tryBuild("craftengine", "visual_block_states");
    public static final NetworkCodec<FriendlyByteBuf, ClientboundVisualBlockStatesPacket> CODEC = ClientCustomPacket.codec(
            (packet, buf) -> {
                buf.writeVarInt(packet.startIndex);
                int[] data = packet.data;
                BitSet present = new BitSet(data.length);
                for (int i = 0; i < data.length; i++) {
                    if (data[i] != -1) present.set(i);
                }
                buf.writeVarInt(data.length);
                byte[] bits = present.toByteArray();
                buf.writeVarInt(bits.length);
                buf.writeBytes(bits);
                for (int v : data) {
                    if (v != -1) buf.writeVarInt(v);
                }
            },
            buf -> {
                int startIndex = buf.readVarInt();
                int len = buf.readVarInt();
                byte[] bits = new byte[buf.readVarInt()];
                buf.readBytes(bits);
                BitSet present = BitSet.valueOf(bits);
                int[] data = new int[len];
                Arrays.fill(data, -1);
                for (int i = present.nextSetBit(0); i >= 0; i = present.nextSetBit(i + 1)) {
                    data[i] = buf.readVarInt();
                }
                return new ClientboundVisualBlockStatesPacket(startIndex, data);
            }
    );
    public static final PacketType<ClientboundVisualBlockStatesPacket> TYPE = PacketType.create(ID, CODEC::decode);

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    @Override
    public NetworkCodec<FriendlyByteBuf, ClientboundVisualBlockStatesPacket> codec() {
        return CODEC;
    }

    @Override
    public PacketType<ClientboundVisualBlockStatesPacket> getType() {
        return TYPE;
    }

    @Override
    public void handle(Context context) {
        BlockManager.instance().handleVisualBlockStates(startIndex, data);
    }
}
