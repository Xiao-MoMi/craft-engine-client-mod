package net.momirealms.craftengine.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.momirealms.craftengine.fabric.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

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
        long millis = Util.getMillis();
        if (accessor.fadeIn() && accessor.fadeInStart() == -1L) {
            accessor.fadeInStart(millis);
        }
        if (accessor.fadeOutStart() != -1) {
            Minecraft.getInstance().setOverlay(null);
        }
        float fadeInProgress = accessor.fadeInStart() > -1L ? (millis - accessor.fadeInStart()) / 500.0F : -1.0F;
        if (accessor.fadeOutStart() == -1L && accessor.reload().isDone() && (!accessor.fadeIn() || fadeInProgress >= 2.0F)) {
            try {
                accessor.reload().checkExceptions();
                accessor.onFinish().accept(Optional.empty());
            } catch (Throwable throwable) {
                accessor.onFinish().accept(Optional.of(throwable));
            }

            accessor.fadeOutStart(Util.getMillis());
            if (Minecraft.getInstance().screen != null) {
                Window window = Minecraft.getInstance().getWindow();
                Minecraft.getInstance().screen.init(Minecraft.getInstance(), window.getGuiScaledWidth(), window.getGuiScaledHeight());
            }
        }
        return null;
    }
}
