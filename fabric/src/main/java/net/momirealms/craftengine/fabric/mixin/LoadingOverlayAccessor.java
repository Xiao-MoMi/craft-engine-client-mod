package net.momirealms.craftengine.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(LoadingOverlay.class)
public interface LoadingOverlayAccessor {

    @Accessor("reload")
    ReloadInstance ce$reload();

    @Accessor("onFinish")
    Consumer<Optional<Throwable>> ce$onFinish();

    @Accessor("fadeIn")
    boolean ce$fadeIn();

    @Accessor("fadeOutStart")
    long ce$fadeOutStart();

    @Accessor("fadeOutStart")
    void ce$fadeOutStart(long fadeOutStart);

    @Accessor("fadeInStart")
    long ce$fadeInStart();

    @Accessor("fadeInStart")
    void ce$fadeInStart(long fadeInStart);

}
