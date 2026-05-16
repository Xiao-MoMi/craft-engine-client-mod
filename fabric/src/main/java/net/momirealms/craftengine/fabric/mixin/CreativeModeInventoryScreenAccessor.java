package net.momirealms.craftengine.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(CreativeModeInventoryScreen.class)
public interface CreativeModeInventoryScreenAccessor {

    @Accessor("selectedTab")
    static void setSelectedTab(CreativeModeTab tab) {
    }
}
