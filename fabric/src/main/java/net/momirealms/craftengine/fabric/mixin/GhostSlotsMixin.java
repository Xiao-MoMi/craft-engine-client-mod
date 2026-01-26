package net.momirealms.craftengine.fabric.mixin;

import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.momirealms.craftengine.fabric.config.ModConfig;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GhostSlots.class)
public class GhostSlotsMixin {

    @Redirect(
            method = "method_62030",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot;isResultSlot:Z",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1
            )
    )
    private boolean modifyIsResultSlot(GhostSlots.GhostSlot instance) {
        return ModConfig.INSTANCE.forceGhostRecipeShowInputItemStackCount() || instance.isResultSlot;
    }
}
