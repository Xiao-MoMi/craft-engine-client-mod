package net.momirealms.craftengine.fabric.mixin;

import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(LoadingOverlay.class)
public interface LoadingOverlayAccessor {

    @Accessor("reload")
    ReloadInstance reload();

    @Accessor("onFinish")
    Consumer<Optional<Throwable>> onFinish();

    @Accessor("fadeIn")
    boolean fadeIn();

    @Accessor("fadeOutStart")
    long fadeOutStart();

    @Accessor("fadeOutStart")
    void fadeOutStart(long fadeOutStart);

    @Accessor("fadeInStart")
    long fadeInStart();

    @Accessor("fadeInStart")
    void fadeInStart(long fadeInStart);

}
