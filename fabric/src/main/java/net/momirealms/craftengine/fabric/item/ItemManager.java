package net.momirealms.craftengine.fabric.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import net.minecraft.world.item.Items;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.config.ModConfig;
import net.momirealms.craftengine.fabric.mixin.CreativeModeInventoryScreenAccessor;
import net.momirealms.craftengine.fabric.mixin.CreativeModeTabAccessor;
import net.momirealms.craftengine.fabric.mixin.CreativeModeTabsAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
public final class ItemManager {
    private static ItemManager instance;
    private final CraftEngineFabricMod mod;
    private final CreativeModeTab[] categoryTabs;
    private final Category[] slotCategories;
    // 服务器发送的物品列表(SET+ADD累计)
    private List<ItemStack> items = List.of();
    // 服务器发送的分类, null表示未收到分类
    private @Nullable List<Category> categories;

    public ItemManager(CraftEngineFabricMod mod) {
        instance = this;
        this.mod = mod;
        int slots = ModConfig.INSTANCE.creativeTabCategorySlots();
        this.categoryTabs = new CreativeModeTab[slots];
        this.slotCategories = new Category[slots];
        this.registerCategoryTabs(slots);
        ClientPlayConnectionEvents.DISCONNECT.register((_, client) -> client.execute(this::clearCreativeTabItems));
    }

    public static ItemManager instance() {
        return instance;
    }

    // 必须在注册表冻结前注册, 路径补零以保证Fabric按字典序排序后的顺序与服务器顺序一致
    private void registerCategoryTabs(int count) {
        for (int i = 0; i < count; i++) {
            int slot = i;
            CreativeModeTab categoryTab = FabricCreativeModeTab.builder()
                    .icon(() -> categoryIcon(slot))
                    .title(placeholderTitle(slot))
                    .displayItems((_, output) -> {
                        Category category = this.slotCategories[slot];
                        if (category != null) acceptAll(output, category.items());
                    })
                    .build();
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath("craftengine", "category_%03d".formatted(i)), categoryTab);
            this.categoryTabs[i] = categoryTab;
        }
        this.mod.logger().info("Registered " + count + " creative tab category slots.");
    }

    private static Component placeholderTitle(int slot) {
        return Component.literal("CraftEngine #" + (slot + 1));
    }

    private ItemStack categoryIcon(int slot) {
        Category category = this.slotCategories[slot];
        if (category != null) {
            if (category.icon() != null && !category.icon().isEmpty()) return category.icon();
            if (!category.items().isEmpty()) return category.items().getFirst();
        }
        return new ItemStack(Items.NETHER_STAR);
    }

    // 同一标签内重复的物品或数量不为1的物品会让原版构建标签内容时抛出异常
    private static void acceptAll(CreativeModeTab.Output output, List<ItemStack> itemStacks) {
        Set<ItemStack> accepted = ItemStackLinkedSet.createTypeAndComponentsSet();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack.isEmpty()) continue;
            ItemStack stack = itemStack.getCount() == 1 ? itemStack : itemStack.copyWithCount(1);
            if (accepted.add(stack)) {
                output.accept(stack);
            }
        }
    }

    public void setCreativeTabItems(@NotNull List<ItemStack> creativeTabItems) {
        this.items = List.copyOf(creativeTabItems);
        this.categories = null;
        this.refresh();
    }

    public void addCreativeTabItems(@NotNull List<ItemStack> creativeTabItems) {
        List<ItemStack> newList = new ArrayList<>(this.items.size() + creativeTabItems.size());
        newList.addAll(this.items);
        newList.addAll(creativeTabItems);
        this.items = List.copyOf(newList);
        this.refresh();
    }

    public void clearCreativeTabItems() {
        this.items = List.of();
        this.categories = null;
        this.refresh();
    }

    public void loadCategoriesFromNetwork(@NotNull List<CategoryData> categoryData) {
        List<Category> list = new ArrayList<>(categoryData.size());
        for (CategoryData data : categoryData) {
            List<ItemStack> categoryItems = new ArrayList<>(data.entries().size());
            for (CategoryEntry entry : data.entries()) {
                if (entry.ref() == 0) {
                    if (entry.inline() != null) categoryItems.add(entry.inline());
                    continue;
                }
                int index = entry.ref() - 1;
                if (index < 0 || index >= this.items.size()) {
                    this.mod.logger().warn("Skipped out of range item ref " + entry.ref() + " in creative tab category " + data.id() + " (items: " + this.items.size() + ")");
                    continue;
                }
                categoryItems.add(this.items.get(index));
            }
            list.add(new Category(data.id(), data.title(), data.icon(), List.copyOf(categoryItems)));
        }
        this.categories = List.copyOf(list);
        int slots = this.categoryTabs.length;
        if (list.size() > slots) {
            this.mod.logger().warn("Hidden " + (list.size() - slots) + " creative tab categories exceeding " + slots + " category slots. Increase creative-tab-category-slots to show them.");
        }
        this.refresh();
    }

    public List<ItemStack> creativeTabItems() {
        return new ArrayList<>(this.items);
    }

    private void refresh() {
        Arrays.fill(this.slotCategories, null);
        int slots = this.categoryTabs.length;
        if (this.categories != null) {
            for (int i = 0; i < Math.min(this.categories.size(), slots); i++) {
                this.slotCategories[i] = this.categories.get(i);
            }
        }
        for (int i = 0; i < slots; i++) {
            Category category = this.slotCategories[i];
            CreativeModeTabAccessor accessor = (CreativeModeTabAccessor) this.categoryTabs[i];
            accessor.ce$displayName(category != null ? category.title() : placeholderTitle(i));
            accessor.ce$iconItemStack(null);
        }
        this.resetSelectedTabIfEmpty();
        // 使缓存失效, 下次打开创造模式物品栏或containerTick时会重建所有标签内容, 同时更新Fabric分页和搜索树
        CreativeModeTabsAccessor.ce$setCachedParameters(null);
    }

    // 物品栏打开时由原版tryRefreshInvalidatedTabs处理
    private void resetSelectedTabIfEmpty() {
        if (Minecraft.getInstance().gui.screen() instanceof CreativeModeInventoryScreen) return;
        CreativeModeTab selectedTab = CreativeModeInventoryScreenAccessor.getSelectedTab();
        for (int i = 0; i < this.categoryTabs.length; i++) {
            if (selectedTab == this.categoryTabs[i]) {
                Category category = this.slotCategories[i];
                if (category == null || category.items().isEmpty()) {
                    CreativeModeInventoryScreenAccessor.setSelectedTab(CreativeModeTabs.getDefaultTab());
                }
                return;
            }
        }
    }

    public record CategoryEntry(int ref, @Nullable ItemStack inline) {
    }

    public record CategoryData(String id, Component title, @Nullable ItemStack icon, List<CategoryEntry> entries) {
    }

    private record Category(String id, Component title, @Nullable ItemStack icon, List<ItemStack> items) {
    }
}
