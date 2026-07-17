package net.momirealms.craftengine.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.momirealms.craftengine.fabric.config.ModConfig;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(GhostSlots.class)
public class GhostSlotsMixin {

    @ModifyExpressionValue(
            method = "method_62030",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot;isResultSlot:Z",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1
            )
    )
    private boolean ce$modifyIsResultSlot(boolean original) {
        return ModConfig.INSTANCE.forceGhostRecipeShowInputItemStackCount() || original;
    }
}
