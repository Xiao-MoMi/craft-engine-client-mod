package net.momirealms.craftengine.neoforge.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.momirealms.craftengine.neoforge.CraftEngineNeoForgeMod;
import net.momirealms.craftengine.neoforge.client.blocks.CustomBlock;

import java.util.IdentityHashMap;
import java.util.function.Function;

public class RegisterBlocks {
    @SuppressWarnings("UnusedReturnValue")
    public static Block register(String name, boolean canPassThrough,
                                 VoxelShape outlineShape, boolean isTransparent,
                                 int canPush, VoxelShape[] occlusionShapesByFace) {
        BlockBehaviour.Properties settings = BlockBehaviour.Properties.of()
                .strength(canPush != 0 ? 3600000.0F : -1.0F, 3600000.0F);
        if (canPush == 1) settings.pushReaction(PushReaction.NORMAL);
        if (canPush == 2) settings.pushReaction(PushReaction.PUSH_ONLY);
        VoxelShape collisionShape;
        if (isTransparent) settings.noOcclusion();
        if (canPassThrough) {
            collisionShape = Shapes.empty();
            settings.noCollission();
        } else {
            collisionShape = outlineShape;
        }
        return register(name, (settingsParam) -> new CustomBlock(settingsParam, outlineShape, collisionShape, isTransparent, occlusionShapesByFace), settings);
    }

    public static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings) {
        ResourceKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.setId(blockKey));
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    public static void unfreezeRegistry() throws IllegalAccessException {
        Reflections.field$MappedRegistry$frozen.set(BuiltInRegistries.BLOCK, false);
        Reflections.field$MappedRegistry$unregisteredIntrusiveHolders.set(BuiltInRegistries.BLOCK, new IdentityHashMap<>());
    }

    public static void freezeRegistry() throws IllegalAccessException {
        Reflections.field$MappedRegistry$frozen.set(BuiltInRegistries.BLOCK, true);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CraftEngineNeoForgeMod.MOD_ID, name));
    }

}

