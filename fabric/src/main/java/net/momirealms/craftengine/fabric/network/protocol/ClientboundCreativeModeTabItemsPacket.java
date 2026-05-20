package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.momirealms.craftengine.fabric.item.ItemManager;
import net.momirealms.craftengine.fabric.network.ClientCustomPacket;
import net.momirealms.craftengine.fabric.network.Context;
import net.momirealms.craftengine.fabric.util.RegistryUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public record ClientboundCreativeModeTabItemsPacket(Action action,
                                                    List<ItemStack> itemStacks) implements ClientCustomPacket {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("craftengine", "creative_mode_tab_items");
    public static final Type<ClientboundCreativeModeTabItemsPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ClientboundCreativeModeTabItemsPacket> CODEC = ClientCustomPacket.codec(
            ClientboundCreativeModeTabItemsPacket::encode,
            ClientboundCreativeModeTabItemsPacket::decode
    );

    private static ClientboundCreativeModeTabItemsPacket decode(FriendlyByteBuf buf) {
        Action action = buf.readEnum(Action.class);
        if (action == Action.CLEAR) {
            return new ClientboundCreativeModeTabItemsPacket(Action.CLEAR, List.of());
        } else {
            RegistryFriendlyByteBuf byteBuf = new RegistryFriendlyByteBuf(buf, RegistryUtils.getRegistryAccess());
            List<ItemStack> list = byteBuf.readCollection(ArrayList::new, $ -> ItemStack.STREAM_CODEC.decode(byteBuf));
            return new ClientboundCreativeModeTabItemsPacket(action, list);
        }
    }

    private void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.action);
        if (this.action == Action.CLEAR) return;
        RegistryFriendlyByteBuf byteBuf = new RegistryFriendlyByteBuf(buf, RegistryUtils.getRegistryAccess());
        byteBuf.writeCollection(this.itemStacks, ($, itemStack) -> ItemStack.STREAM_CODEC.encode(byteBuf, itemStack));
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
        this.action.execute(this.itemStacks);
    }

    public enum Action {
        ADD(list -> {
            ItemManager itemManager = ItemManager.instance();
            list.addAll(itemManager.creativeTabItems());
            itemManager.loadFromNetwork(list);
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
