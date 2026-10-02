package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
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

@Environment(EnvType.CLIENT)
public record ClientboundCreativeModeTabCategoriesPacket(FriendlyByteBuf data) implements ClientCustomPacket {
    public static final int FORMAT_VERSION = 1;
    public static final Identifier ID = Identifier.fromNamespaceAndPath("craftengine", "creative_mode_tab_categories");
    public static final Type<ClientboundCreativeModeTabCategoriesPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ClientboundCreativeModeTabCategoriesPacket> CODEC = ClientCustomPacket.codec(
            ClientboundCreativeModeTabCategoriesPacket::encode,
            ClientboundCreativeModeTabCategoriesPacket::decode
    );

    private static ClientboundCreativeModeTabCategoriesPacket decode(FriendlyByteBuf buf) {
        return new ClientboundCreativeModeTabCategoriesPacket(new FriendlyByteBuf(buf.readBytes(buf.readableBytes())));
    }

    private void encode(FriendlyByteBuf buf) {
        buf.writeBytes(this.data);
    }

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, ClientboundCreativeModeTabCategoriesPacket> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Type<ClientboundCreativeModeTabCategoriesPacket> type() {
        return TYPE;
    }

    @Override
    public void handle(Context context) {
        if (!(context.networkHandler() instanceof ClientPacketListener listener)) return;
        RegistryFriendlyByteBuf byteBuf = new RegistryFriendlyByteBuf(this.data, listener.registryAccess());
        try {
            int formatVersion = byteBuf.readVarInt();
            if (formatVersion != FORMAT_VERSION) {
                CraftEngineFabricMod.instance().logger().warn("Ignored ClientboundCreativeModeTabCategoriesPacket with unknown format version " + formatVersion);
                return;
            }
            int categoryCount = byteBuf.readVarInt();
            List<ItemManager.CategoryData> categories = new ArrayList<>(categoryCount);
            for (int i = 0; i < categoryCount; i++) {
                String id = byteBuf.readUtf();
                Component title = Component.literal(byteBuf.readUtf());
                ItemStack icon = byteBuf.readBoolean() ? ItemStack.OPTIONAL_STREAM_CODEC.decode(byteBuf) : null;
                int entryCount = byteBuf.readVarInt();
                List<ItemManager.CategoryEntry> entries = new ArrayList<>(entryCount);
                for (int j = 0; j < entryCount; j++) {
                    int ref = byteBuf.readVarInt();
                    ItemStack inline = ref == 0 ? ItemStack.OPTIONAL_STREAM_CODEC.decode(byteBuf) : null;
                    entries.add(new ItemManager.CategoryEntry(ref, inline));
                }
                categories.add(new ItemManager.CategoryData(id, title, icon, entries));
            }
            ItemManager.instance().loadCategoriesFromNetwork(categories);
        } catch (Throwable t) {
            CraftEngineFabricMod.instance().logger().warn("Failed to handle ClientboundCreativeModeTabCategoriesPacket", t);
        }
    }
}
