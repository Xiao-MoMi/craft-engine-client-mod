package net.momirealms.craftengine.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.util.Util;
import net.momirealms.craftengine.fabric.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @ModifyReturnValue(method = "getOverlay", at = @At(value = "RETURN"))
    public Overlay ce$blockOverlay(Overlay original) {
        if (!ModConfig.INSTANCE.disableResourcePackLoadingScreen()) {
            return original;
        }
        if (!(original instanceof LoadingOverlayAccessor accessor)) {
            return original;
        }
        if (accessor.ce$fadeIn() && accessor.ce$fadeInStart() == -1L) {
            accessor.ce$fadeInStart(Util.getMillis());
        }
        if (accessor.ce$fadeOutStart() != -1) {
            Minecraft.getInstance().setOverlay(null);
        }
        return null;
    }
}
