package net.momirealms.craftengine.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.client.gui.screens.recipebook.GhostSlots.GhostSlot")
public interface GhostSlotAccessor {

    @Mutable
    @Accessor("isResultSlot")
    boolean craftengine$isResultSlot();
}
