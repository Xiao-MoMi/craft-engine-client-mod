package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.momirealms.craftengine.fabric.item.ItemManager;
import net.momirealms.craftengine.fabric.network.ClientCustomPacket;
import net.momirealms.craftengine.fabric.network.Context;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public record ClientboundCreativeModeTabItemsPacket(Action action,
                                                    List<ItemStack> itemStacks) implements ClientCustomPacket {
    public static final ResourceLocation ID = ResourceLocation.tryBuild("craftengine", "creative_mode_tab_items");
    public static final NetworkCodec<FriendlyByteBuf, ClientboundCreativeModeTabItemsPacket> CODEC = ClientCustomPacket.codec(
            ClientboundCreativeModeTabItemsPacket::encode,
            ClientboundCreativeModeTabItemsPacket::decode
    );
    public static final PacketType<ClientboundCreativeModeTabItemsPacket> TYPE = PacketType.create(ID, CODEC::decode);

    private static ClientboundCreativeModeTabItemsPacket decode(FriendlyByteBuf buf) {
        Action action = buf.readEnum(Action.class);
        if (action == Action.CLEAR) {
            return new ClientboundCreativeModeTabItemsPacket(Action.CLEAR, null);
        } else {
            return new ClientboundCreativeModeTabItemsPacket(action, buf.readList(FriendlyByteBuf::readItem));
        }
    }

    private void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.action);
        if (this.action == Action.CLEAR) return;
        buf.writeCollection(this.itemStacks, FriendlyByteBuf::writeItem);
    }


    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    @Override
    public NetworkCodec<FriendlyByteBuf, ClientboundCreativeModeTabItemsPacket> codec() {
        return CODEC;
    }

    @Override
    public PacketType<ClientboundCreativeModeTabItemsPacket> getType() {
        return TYPE;
    }

    @Override
    public void handle(Context context) {
        this.action.execute(itemStacks);
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
