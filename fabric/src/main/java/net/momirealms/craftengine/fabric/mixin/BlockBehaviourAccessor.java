package net.momirealms.craftengine.fabric.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(BlockBehaviour.class)
public interface BlockBehaviourAccessor {

    @Mutable
    @Accessor("hasCollision")
    boolean hasCollision();

    @Mutable
    @Accessor("explosionResistance")
    float explosionResistance();

    @Mutable
    @Accessor("isRandomlyTicking")
    boolean isRandomlyTicking();

    @Mutable
    @Accessor("soundType")
    SoundType soundType();

    @Mutable
    @Accessor("friction")
    float friction();

    @Mutable
    @Accessor("speedFactor")
    float speedFactor();

    @Mutable
    @Accessor("jumpFactor")
    float jumpFactor();

    @Mutable
    @Accessor("dynamicShape")
    boolean dynamicShape();

    @Mutable
    @Accessor("requiredFeatures")
    FeatureFlagSet requiredFeatures();

    @Mutable
    @Accessor("properties")
    BlockBehaviour.Properties properties();

    @Mutable
    @Accessor("drops")
    Optional<ResourceKey<LootTable>> drops();

    @Mutable
    @Accessor("descriptionId")
    String descriptionId();

    @Mutable
    @Accessor("hasCollision")
    void hasCollision(boolean hasCollision);

    @Mutable
    @Accessor("explosionResistance")
    void explosionResistance(float explosionResistance);

    @Mutable
    @Accessor("isRandomlyTicking")
    void isRandomlyTicking(boolean isRandomlyTicking);

    @Mutable
    @Accessor("soundType")
    void soundType(SoundType soundType);

    @Mutable
    @Accessor("friction")
    void friction(float friction);

    @Mutable
    @Accessor("speedFactor")
    void speedFactor(float speedFactor);

    @Mutable
    @Accessor("jumpFactor")
    void jumpFactor(float jumpFactor);

    @Mutable
    @Accessor("dynamicShape")
    void dynamicShape(boolean dynamicShape);

    @Mutable
    @Accessor("requiredFeatures")
    void requiredFeatures(FeatureFlagSet requiredFeatures);

    @Mutable
    @Accessor("properties")
    void properties(BlockBehaviour.Properties properties);

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Mutable
    @Accessor("drops")
    void drops(Optional<ResourceKey<LootTable>> drops);

    @Mutable
    @Accessor("descriptionId")
    void descriptionId(String descriptionId);
}
