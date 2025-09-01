package net.momirealms.craftengine.fabric.block;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class CraftEngineStateFactory implements StateDefinition.Factory<Block, BlockState> {
    public static final CraftEngineStateFactory INSTANCE = new CraftEngineStateFactory();

    @Override
    public @NotNull BlockState create(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> reference2ObjectArrayMap, MapCodec<BlockState> mapCodec) {
        return new CraftEngineBlockState(block, reference2ObjectArrayMap, mapCodec);
    }
}
