package net.momirealms.craftengine.neoforge.client.network;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.world.level.block.Block;
import net.momirealms.craftengine.neoforge.client.config.ModConfig;
import net.momirealms.craftengine.neoforge.client.util.NetWorkDataTypes;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public record ConfigurationTask(ServerConfigurationPacketListener listener) implements ICustomConfigurationTask {
    private static final Type TYPE = new Type("craftengine:task");

    @Override
    public void run(@NotNull Consumer<CustomPacketPayload> consumer) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.isLocalServer() || minecraft.getConnection() == null) {
            this.listener.finishCurrentTask(type());
            return;
        }
        if (!ModConfig.enableNetwork && !ModConfig.enableCancelBlockUpdate) {
            this.listener.finishCurrentTask(type());
            return;
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        if (ModConfig.enableNetwork) {
            buf.writeEnum(NetWorkDataTypes.CLIENT_CUSTOM_BLOCK);
            NetWorkDataTypes.CLIENT_CUSTOM_BLOCK.encode(buf, Block.BLOCK_STATE_REGISTRY.size());
        } else if (ModConfig.enableCancelBlockUpdate) {
            buf.writeEnum(NetWorkDataTypes.CANCEL_BLOCK_UPDATE);
            NetWorkDataTypes.CANCEL_BLOCK_UPDATE.encode(buf, true);
        }
        PacketDistributor.sendToServer(CraftEnginePayload.of(buf.array()));
        this.listener.finishCurrentTask(type());
    }

    @Override
    public @NotNull Type type() {
        return TYPE;
    }
}
