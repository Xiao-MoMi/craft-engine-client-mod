package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.momirealms.craftengine.fabric.item.ItemManager;
import net.momirealms.craftengine.fabric.network.Context;
import net.momirealms.craftengine.fabric.network.ModPacket;
import net.momirealms.craftengine.fabric.registries.BuiltInRegistries;
import net.momirealms.craftengine.fabric.util.RegistryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public record CreativeModeTabItemsPacket(Action action, List<ItemStack> itemStacks) implements ModPacket {
    public static final ResourceKey<StreamCodec<FriendlyByteBuf, ? extends ModPacket>> TYPE = ResourceKey.create(
            BuiltInRegistries.MOD_PACKET.key(), ResourceLocation.tryBuild("craftengine", "creative_mode_tab_items")
    );
    public static final StreamCodec<FriendlyByteBuf, CreativeModeTabItemsPacket> CODEC = ModPacket.codec(
            CreativeModeTabItemsPacket::encode,
            CreativeModeTabItemsPacket::decode
    );

    private static CreativeModeTabItemsPacket decode(FriendlyByteBuf buf) {
        Action action = buf.readEnum(Action.class);
        if (action == Action.CLEAR) {
            return new CreativeModeTabItemsPacket(Action.CLEAR, List.of());
        } else {
            RegistryFriendlyByteBuf byteBuf = new RegistryFriendlyByteBuf(buf, RegistryUtils.getRegistryAccess());
            List<ItemStack> list = byteBuf.readCollection(ArrayList::new, $ -> ItemStack.STREAM_CODEC.decode(byteBuf));
            return new CreativeModeTabItemsPacket(action, list);
        }
    }

    private void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.action);
        if (this.action == Action.CLEAR) return;
        RegistryFriendlyByteBuf byteBuf = new RegistryFriendlyByteBuf(buf, RegistryUtils.getRegistryAccess());
        byteBuf.writeCollection(this.itemStacks, ($, itemStack) -> ItemStack.STREAM_CODEC.encode(byteBuf, itemStack));
    }

    @Override
    public ResourceKey<StreamCodec<FriendlyByteBuf, ? extends ModPacket>> type() {
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
