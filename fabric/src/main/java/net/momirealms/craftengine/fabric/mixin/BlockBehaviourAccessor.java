package net.momirealms.craftengine.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(BlockBehaviour.class)
public interface BlockBehaviourAccessor {

    @Mutable
    @Accessor("hasCollision")
    boolean ce$hasCollision();

    @Mutable
    @Accessor("explosionResistance")
    float ce$explosionResistance();

    @Mutable
    @Accessor("isRandomlyTicking")
    boolean ce$isRandomlyTicking();

    @Mutable
    @Accessor("soundType")
    SoundType ce$soundType();

    @Mutable
    @Accessor("friction")
    float ce$friction();

    @Mutable
    @Accessor("speedFactor")
    float ce$speedFactor();

    @Mutable
    @Accessor("jumpFactor")
    float ce$jumpFactor();

    @Mutable
    @Accessor("dynamicShape")
    boolean ce$dynamicShape();

    @Mutable
    @Accessor("requiredFeatures")
    FeatureFlagSet ce$requiredFeatures();

    @Mutable
    @Accessor("properties")
    BlockBehaviour.Properties ce$properties();

    @Mutable
    @Accessor("hasCollision")
    void ce$hasCollision(boolean hasCollision);

    @Mutable
    @Accessor("explosionResistance")
    void ce$explosionResistance(float explosionResistance);

    @Mutable
    @Accessor("isRandomlyTicking")
    void ce$isRandomlyTicking(boolean isRandomlyTicking);

    @Mutable
    @Accessor("soundType")
    void ce$soundType(SoundType soundType);

    @Mutable
    @Accessor("friction")
    void ce$friction(float friction);

    @Mutable
    @Accessor("speedFactor")
    void ce$speedFactor(float speedFactor);

    @Mutable
    @Accessor("jumpFactor")
    void ce$jumpFactor(float jumpFactor);

    @Mutable
    @Accessor("dynamicShape")
    void ce$dynamicShape(boolean dynamicShape);

    @Mutable
    @Accessor("requiredFeatures")
    void ce$requiredFeatures(FeatureFlagSet requiredFeatures);

    @Mutable
    @Accessor("properties")
    void ce$properties(BlockBehaviour.Properties properties);
}
