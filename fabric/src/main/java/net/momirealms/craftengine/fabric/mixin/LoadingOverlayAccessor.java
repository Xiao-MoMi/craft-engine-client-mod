package net.momirealms.craftengine.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(LoadingOverlay.class)
public interface LoadingOverlayAccessor {

    @Accessor("fadeIn")
    boolean ce$fadeIn();

    @Accessor("fadeOutStart")
    long ce$fadeOutStart();

    @Accessor("fadeInStart")
    long ce$fadeInStart();

    @Accessor("fadeInStart")
    void ce$fadeInStart(long fadeInStart);

}
