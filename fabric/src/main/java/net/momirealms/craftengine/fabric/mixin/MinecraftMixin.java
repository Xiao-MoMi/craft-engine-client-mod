package net.momirealms.craftengine.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.util.Util;
import net.momirealms.craftengine.fabric.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @ModifyReturnValue(method = "getOverlay", at = @At(value = "RETURN"))
    public Overlay blockOverlay(Overlay original) {
        if (!ModConfig.INSTANCE.disableResourcePackLoadingScreen()) {
            return original;
        }
        if (!(original instanceof LoadingOverlayAccessor accessor)) {
            return original;
        }
        if (accessor.fadeIn() && accessor.fadeInStart() == -1L) {
            accessor.fadeInStart(Util.getMillis());
        }
        if (accessor.fadeOutStart() != -1) {
            Minecraft.getInstance().setOverlay(null);
        }
        return null;
    }
}
