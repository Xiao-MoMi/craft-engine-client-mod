package net.momirealms.craftengine.fabric.mixin;

import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LoadingOverlay.class)
public interface LoadingOverlayAccessor {

    @Accessor("fadeIn")
    boolean fadeIn();

    @Accessor("fadeOutStart")
    long fadeOutStart();

    @Accessor("fadeInStart")
    long fadeInStart();

    @Accessor("fadeInStart")
    void fadeInStart(long fadeInStart);

}
