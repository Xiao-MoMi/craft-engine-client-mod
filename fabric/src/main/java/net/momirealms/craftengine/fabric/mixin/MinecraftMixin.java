package net.momirealms.craftengine.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.momirealms.craftengine.fabric.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

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
        long millis = Util.getMillis();
        if (accessor.ce$fadeIn() && accessor.ce$fadeInStart() == -1L) {
            accessor.ce$fadeInStart(millis);
        }
        if (accessor.ce$fadeOutStart() != -1) {
            Minecraft.getInstance().setOverlay(null);
        }
        float fadeInProgress = accessor.ce$fadeInStart() > -1L ? (millis - accessor.ce$fadeInStart()) / 500.0F : -1.0F;
        if (accessor.ce$fadeOutStart() == -1L && accessor.ce$reload().isDone() && (!accessor.ce$fadeIn() || fadeInProgress >= 2.0F)) {
            try {
                accessor.ce$reload().checkExceptions();
                accessor.ce$onFinish().accept(Optional.empty());
            } catch (Throwable throwable) {
                accessor.ce$onFinish().accept(Optional.of(throwable));
            }

            accessor.ce$fadeOutStart(Util.getMillis());
            if (Minecraft.getInstance().screen != null) {
                Window window = Minecraft.getInstance().getWindow();
                Minecraft.getInstance().screen.init(Minecraft.getInstance(), window.getGuiScaledWidth(), window.getGuiScaledHeight());
            }
        }
        return null;
    }
}
