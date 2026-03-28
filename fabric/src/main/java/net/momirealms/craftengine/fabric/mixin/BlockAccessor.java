package net.momirealms.craftengine.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(Block.class)
public interface BlockAccessor {

    @Mutable
    @Accessor("stateDefinition")
    void ce$setStateDefinition(StateDefinition<Block, @NotNull BlockState> stateDefinition);

    @Accessor("defaultBlockState")
    void ce$setDefaultBlockState(BlockState defaultBlockState);
}
