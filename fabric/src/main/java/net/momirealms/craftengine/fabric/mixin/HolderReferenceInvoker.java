package net.momirealms.craftengine.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Environment(EnvType.CLIENT)
@Mixin(Holder.Reference.class)
public interface HolderReferenceInvoker<T> {

    @Invoker("bindValue")
    void callBindValue(T object);

    @Accessor("tags")
    Set<TagKey<T>> tags();

    @Accessor("tags")
    void tags(Set<TagKey<T>> tags);
}
