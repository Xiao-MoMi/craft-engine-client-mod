package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.momirealms.craftengine.fabric.block.BlockManager;
import net.momirealms.craftengine.fabric.network.ClientCustomPacket;
import net.momirealms.craftengine.fabric.network.Context;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.BitSet;

@Environment(EnvType.CLIENT)
@SuppressWarnings({"unchecked", "DuplicatedCode"})
public record ClientboundVisualBlockStatesPacket(int startIndex, int[] data) implements ClientCustomPacket {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("craftengine", "visual_block_states");
    public static final Type<ClientboundVisualBlockStatesPacket> TYPE = new Type<>(ID);
    private static ClientboundVisualBlockStatesPacket previousPacket;
    public static final StreamCodec<FriendlyByteBuf, ClientboundVisualBlockStatesPacket> CODEC = ClientCustomPacket.codec(
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

    public static void handleTags() {
        if (previousPacket == null) return;
    }

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, ClientboundVisualBlockStatesPacket> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Type<ClientboundVisualBlockStatesPacket> type() {
        return TYPE;
    }

    @Override
    public void handle(Context context) {
        BlockManager.instance().handleVisualBlockStates(startIndex, data);
    }
}
