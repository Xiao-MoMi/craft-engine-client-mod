package net.momirealms.craftengine.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Collection;
import java.util.Set;

@Environment(EnvType.CLIENT)
@Mixin(CreativeModeTab.class)
public interface CreativeModeTabAccessor {

    @Accessor("displayItems")
    void ce$displayItems(Collection<ItemStack> displayItems);

    @Accessor("displayItemsSearchTab")
    void ce$displayItemsSearchTab(Set<ItemStack> displayItems);

    @Mutable
    @Accessor("displayName")
    void ce$displayName(Component displayName);

    @Accessor("iconItemStack")
    void ce$iconItemStack(ItemStack iconItemStack);
}
