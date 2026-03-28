package net.momirealms.craftengine.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(BlockBehaviour.BlockStateBase.class)
public interface BlockStateBaseAccessor {
    @Mutable
    @Accessor("lightEmission")
    int ce$lightEmission();

    @Mutable
    @Accessor("lightEmission")
    void ce$lightEmission(int lightEmission);

    @Mutable
    @Accessor("useShapeForLightOcclusion")
    boolean ce$useShapeForLightOcclusion();

    @Mutable
    @Accessor("useShapeForLightOcclusion")
    void ce$useShapeForLightOcclusion(boolean useShapeForLightOcclusion);

    @Mutable
    @Accessor("isAir")
    boolean ce$isAir();

    @Mutable
    @Accessor("isAir")
    void ce$isAir(boolean isAir);

    @Mutable
    @Accessor("ignitedByLava")
    boolean ce$ignitedByLava();

    @Mutable
    @Accessor("ignitedByLava")
    void ce$ignitedByLava(boolean ignitedByLava);

    @Mutable
    @Accessor("liquid")
    boolean ce$liquid();

    @Mutable
    @Accessor("liquid")
    void ce$liquid(boolean liquid);

    @Mutable
    @Accessor("legacySolid")
    boolean ce$legacySolid();

    @Mutable
    @Accessor("legacySolid")
    void ce$legacySolid(boolean legacySolid);

    @Mutable
    @Accessor("pushReaction")
    PushReaction ce$pushReaction();

    @Mutable
    @Accessor("pushReaction")
    void ce$pushReaction(PushReaction pushReaction);

    @Mutable
    @Accessor("mapColor")
    MapColor ce$mapColor();

    @Mutable
    @Accessor("mapColor")
    void ce$mapColor(MapColor mapColor);

    @Mutable
    @Accessor("destroySpeed")
    float ce$destroySpeed();

    @Mutable
    @Accessor("destroySpeed")
    void ce$destroySpeed(float destroySpeed);

    @Mutable
    @Accessor("requiresCorrectToolForDrops")
    boolean ce$requiresCorrectToolForDrops();

    @Mutable
    @Accessor("requiresCorrectToolForDrops")
    void ce$requiresCorrectToolForDrops(boolean requiresCorrectToolForDrops);

    @Mutable
    @Accessor("canOcclude")
    boolean ce$canOcclude();

    @Mutable
    @Accessor("canOcclude")
    void ce$canOcclude(boolean canOcclude);

    @Mutable
    @Accessor("offsetFunction")
    Optional<BlockBehaviour.OffsetFunction> ce$offsetFunction();

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Mutable
    @Accessor("offsetFunction")
    void ce$offsetFunction(Optional<BlockBehaviour.OffsetFunction> offsetFunction);

    @Mutable
    @Accessor("spawnTerrainParticles")
    boolean ce$spawnTerrainParticles();

    @Mutable
    @Accessor("spawnTerrainParticles")
    void ce$spawnTerrainParticles(boolean spawnTerrainParticles);

    @Mutable
    @Accessor("instrument")
    NoteBlockInstrument ce$instrument();

    @Mutable
    @Accessor("instrument")
    void ce$instrument(NoteBlockInstrument instrument);

    @Mutable
    @Accessor("replaceable")
    boolean ce$replaceable();

    @Mutable
    @Accessor("replaceable")
    void ce$replaceable(boolean replaceable);

    @Mutable
    @Accessor("fluidState")
    FluidState ce$fluidState();

    @Mutable
    @Accessor("fluidState")
    void ce$fluidState(FluidState fluidState);

    @Mutable
    @Accessor("isRandomlyTicking")
    boolean ce$isRandomlyTicking();

    @Mutable
    @Accessor("isRandomlyTicking")
    void ce$isRandomlyTicking(boolean isRandomlyTicking);
}