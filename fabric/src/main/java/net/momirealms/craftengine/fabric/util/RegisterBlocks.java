package net.momirealms.craftengine.fabric.util;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.client.blocks.CustomBlock;

import java.util.function.Function;

public class RegisterBlocks {
    @SuppressWarnings("UnusedReturnValue")
    public static Block register(String name, boolean canPassThrough,
                                 VoxelShape outlineShape, boolean isTransparent,
                                 int canPush) {
        AbstractBlock.Settings settings = Block.Settings.create()
                .strength(canPush != 0 ? 3600000.0F : -1.0F, 3600000.0F);
        if (canPush == 1) settings.pistonBehavior(PistonBehavior.NORMAL);
        if (canPush == 2) settings.pistonBehavior(PistonBehavior.PUSH_ONLY);
        VoxelShape collisionShape;
        if (isTransparent) settings.nonOpaque();
        if (canPassThrough) {
            collisionShape = VoxelShapes.empty();
            settings.noCollision();
        } else {
            collisionShape = outlineShape;
        }
        return register(name, (settingsParam) -> new CustomBlock(settingsParam, outlineShape, collisionShape, isTransparent), settings);
    }

    public static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
        RegistryKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.registryKey(blockKey));

        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(CraftEngineFabricMod.MOD_ID, name));
    }

}

