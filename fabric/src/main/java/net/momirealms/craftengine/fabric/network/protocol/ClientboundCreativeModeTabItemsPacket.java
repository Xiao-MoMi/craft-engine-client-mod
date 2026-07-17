package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.item.ItemManager;
import net.momirealms.craftengine.fabric.network.ClientCustomPacket;
import net.momirealms.craftengine.fabric.network.Context;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public record ClientboundCreativeModeTabItemsPacket(Action action,
                                                    FriendlyByteBuf itemStacks) implements ClientCustomPacket {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("craftengine", "creative_mode_tab_items");
    public static final Type<ClientboundCreativeModeTabItemsPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ClientboundCreativeModeTabItemsPacket> CODEC = ClientCustomPacket.codec(
            ClientboundCreativeModeTabItemsPacket::encode,
            ClientboundCreativeModeTabItemsPacket::decode
    );

    private static ClientboundCreativeModeTabItemsPacket decode(FriendlyByteBuf buf) {
        Action action = buf.readEnum(Action.class);
        if (action == Action.CLEAR) {
            return new ClientboundCreativeModeTabItemsPacket(Action.CLEAR, null);
        } else {
            return new ClientboundCreativeModeTabItemsPacket(action, new FriendlyByteBuf(buf.readBytes(buf.readableBytes())));
        }
    }

    private void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.action);
        if (this.action == Action.CLEAR) return;
        buf.writeBytes(itemStacks);
    }


    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, ClientboundCreativeModeTabItemsPacket> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Type<ClientboundCreativeModeTabItemsPacket> type() {
        return TYPE;
    }

    @Override
    public void handle(Context context) {
        if (!(context.networkHandler() instanceof ClientPacketListener listener)) return;
        RegistryFriendlyByteBuf byteBuf = new RegistryFriendlyByteBuf(this.itemStacks, listener.registryAccess());
        try {
            List<ItemStack> list = byteBuf.readCollection(ArrayList::new, $ -> ItemStack.OPTIONAL_STREAM_CODEC.decode(byteBuf));
            this.action.execute(list);
        } catch (Throwable t) {
            CraftEngineFabricMod.instance().logger().warn("Failed to handle ClientboundCreativeModeTabItemsPacket", t);
        }
    }

    public enum Action {
        ADD(list -> {
            ItemManager itemManager = ItemManager.instance();
            List<ItemStack> newList = itemManager.creativeTabItems();
            newList.addAll(list);
            itemManager.loadFromNetwork(newList);
        }),
        CLEAR($ -> ItemManager.instance().clearCreativeTabItems()),
        SET(ItemManager.instance()::loadFromNetwork);

        private final Consumer<List<ItemStack>> action;

        Action(Consumer<List<ItemStack>> action) {
            this.action = action;
        }

        public void execute(List<ItemStack> list) {
            this.action.accept(list);
        }
    }
}
