package net.momirealms.craftengine.fabric.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.mixin.CreativeModeTabAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class ItemManager {
    private static final ResourceKey<CreativeModeTab> KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ResourceLocation.tryBuild("craftengine", "tab"));
    private static ItemManager instance;
    private final CraftEngineFabricMod mod;
    private final CreativeModeTab tab;
    private List<ItemStack> creativeTabItems = List.of();

    public ItemManager(CraftEngineFabricMod mod) {
        instance = this;
        this.mod = mod;
        this.tab = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Items.NETHER_STAR))
                .title(Component.literal("CraftEngine"))
                .displayItems(($, output) -> {
                    if (creativeTabItems == null || creativeTabItems.isEmpty()) return;
                    List<ItemStack> temp = creativeTabItems;
                    for (ItemStack itemStack : temp) {
                        output.accept(itemStack);
                    }
                })
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, KEY, this.tab);
        ClientPlayConnectionEvents.DISCONNECT.register(($, $$) -> clearCreativeTabItems());
    }

    public static ItemManager instance() {
        return instance;
    }

    public void loadFromNetwork(@NotNull List<ItemStack> creativeTabItems) {
        this.creativeTabItems = creativeTabItems;
        ((CreativeModeTabAccessor) this.tab).ce$displayItems(creativeTabItems);
        ((CreativeModeTabAccessor) this.tab).ce$displayItemsSearchTab(new HashSet<>(creativeTabItems));
    }

    public List<ItemStack> creativeTabItems() {
        return Collections.unmodifiableList(creativeTabItems);
    }

    public void clearCreativeTabItems() {
        this.creativeTabItems = List.of();
    }
}
