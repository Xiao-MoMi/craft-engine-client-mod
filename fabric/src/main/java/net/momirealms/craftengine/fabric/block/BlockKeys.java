package net.momirealms.craftengine.fabric.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class BlockKeys {
    private BlockKeys() {}

    public static final ResourceLocation NOTE_BLOCK = ResourceLocation.tryParse("minecraft:note_block");
}
