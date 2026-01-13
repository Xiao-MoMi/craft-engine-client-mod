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
    int lightEmission();

    @Mutable
    @Accessor("lightEmission")
    void lightEmission(int lightEmission);

    @Mutable
    @Accessor("useShapeForLightOcclusion")
    boolean useShapeForLightOcclusion();

    @Mutable
    @Accessor("useShapeForLightOcclusion")
    void useShapeForLightOcclusion(boolean useShapeForLightOcclusion);

    @Mutable
    @Accessor("isAir")
    boolean isAir();

    @Mutable
    @Accessor("isAir")
    void isAir(boolean isAir);

    @Mutable
    @Accessor("ignitedByLava")
    boolean ignitedByLava();

    @Mutable
    @Accessor("ignitedByLava")
    void ignitedByLava(boolean ignitedByLava);

    @Mutable
    @Accessor("liquid")
    boolean liquid();

    @Mutable
    @Accessor("liquid")
    void liquid(boolean liquid);

    @Mutable
    @Accessor("legacySolid")
    boolean legacySolid();

    @Mutable
    @Accessor("legacySolid")
    void legacySolid(boolean legacySolid);

    @Mutable
    @Accessor("pushReaction")
    PushReaction pushReaction();

    @Mutable
    @Accessor("pushReaction")
    void pushReaction(PushReaction pushReaction);

    @Mutable
    @Accessor("mapColor")
    MapColor mapColor();

    @Mutable
    @Accessor("mapColor")
    void mapColor(MapColor mapColor);

    @Mutable
    @Accessor("destroySpeed")
    float destroySpeed();

    @Mutable
    @Accessor("destroySpeed")
    void destroySpeed(float destroySpeed);

    @Mutable
    @Accessor("requiresCorrectToolForDrops")
    boolean requiresCorrectToolForDrops();

    @Mutable
    @Accessor("requiresCorrectToolForDrops")
    void requiresCorrectToolForDrops(boolean requiresCorrectToolForDrops);

    @Mutable
    @Accessor("canOcclude")
    boolean canOcclude();

    @Mutable
    @Accessor("canOcclude")
    void canOcclude(boolean canOcclude);

    @Mutable
    @Accessor("offsetFunction")
    Optional<BlockBehaviour.OffsetFunction> offsetFunction();

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Mutable
    @Accessor("offsetFunction")
    void offsetFunction(Optional<BlockBehaviour.OffsetFunction> offsetFunction);

    @Mutable
    @Accessor("spawnTerrainParticles")
    boolean spawnTerrainParticles();

    @Mutable
    @Accessor("spawnTerrainParticles")
    void spawnTerrainParticles(boolean spawnTerrainParticles);

    @Mutable
    @Accessor("instrument")
    NoteBlockInstrument instrument();

    @Mutable
    @Accessor("instrument")
    void instrument(NoteBlockInstrument instrument);

    @Mutable
    @Accessor("replaceable")
    boolean replaceable();

    @Mutable
    @Accessor("replaceable")
    void replaceable(boolean replaceable);

    @Mutable
    @Accessor("fluidState")
    FluidState fluidState();

    @Mutable
    @Accessor("fluidState")
    void fluidState(FluidState fluidState);

    @Mutable
    @Accessor("isRandomlyTicking")
    boolean isRandomlyTicking();

    @Mutable
    @Accessor("isRandomlyTicking")
    void isRandomlyTicking(boolean isRandomlyTicking);
}