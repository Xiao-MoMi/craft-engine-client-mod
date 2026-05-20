package net.momirealms.craftengine.fabric.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.momirealms.craftengine.fabric.block.BlockManager;
import net.momirealms.craftengine.fabric.block.CraftEngineBlock;
import net.momirealms.craftengine.fabric.block.CraftEngineBlockState;
import net.momirealms.craftengine.fabric.mixin.BlockBehaviourAccessor;
import net.momirealms.craftengine.fabric.mixin.BlockStateBaseAccessor;
import net.momirealms.craftengine.fabric.mixin.HolderReferenceInvoker;

@Environment(EnvType.CLIENT)
public final class BlockStateUtils {
    private static int vanillaStateSize;
    private static boolean hasInit;

    public static void init(int size) {
        if (hasInit) {
            throw new IllegalStateException("BlockStateUtils has already been initialized");
        }
        vanillaStateSize = size;
        hasInit = true;
    }

    public static int vanillaStateSize() {
        return vanillaStateSize;
    }

    public static boolean isVanillaBlock(BlockState state) {
        return !(state instanceof CraftEngineBlockState);
    }

    public static boolean isVanillaBlock(int id) {
        return id < vanillaStateSize && id >= 0;
    }

    public static BlockState remap(BlockState state) {
        if (isVanillaBlock(state)) return state;
        int id = Block.BLOCK_STATE_REGISTRY.getId(state);
        int remapped = BlockManager.instance().remapState(id);
        return Block.BLOCK_STATE_REGISTRY.byId(remapped);
    }

    public static void handleRemap(int customId, int vanillaId) {
        BlockManager.instance().remapState(customId, vanillaId);
        BlockState customState = Block.BLOCK_STATE_REGISTRY.byId(customId);
        if (!(customState instanceof CraftEngineBlockState craftEngineBlockState)) return;
        if (!(craftEngineBlockState.getBlock() instanceof CraftEngineBlock craftEngineBlock)) return;
        BlockState vanillaState = Block.BLOCK_STATE_REGISTRY.byId(vanillaId);
        if (vanillaState == null) return;
        craftEngineBlockState.setVisualBlockState(vanillaState);
        Block vanillaBlock = vanillaState.getBlock();
        craftEngineBlock.setVisualBlock(vanillaBlock);
        BlockRenderUtils.registerRenderLayer(craftEngineBlock, vanillaState);
        BlockRenderUtils.registerColor(craftEngineBlock, vanillaBlock);
        BlockBehaviourAccessor customBlockAccessor = (BlockBehaviourAccessor) craftEngineBlock;
        BlockBehaviourAccessor vanillaBlockAccessor = (BlockBehaviourAccessor) vanillaBlock;
        customBlockAccessor.ce$hasCollision(vanillaBlockAccessor.ce$hasCollision());
        customBlockAccessor.ce$explosionResistance(vanillaBlockAccessor.ce$explosionResistance());
        customBlockAccessor.ce$soundType(vanillaBlockAccessor.ce$soundType());
        customBlockAccessor.ce$friction(vanillaBlockAccessor.ce$friction());
        customBlockAccessor.ce$speedFactor(vanillaBlockAccessor.ce$speedFactor());
        customBlockAccessor.ce$jumpFactor(vanillaBlockAccessor.ce$jumpFactor());
        customBlockAccessor.ce$dynamicShape(vanillaBlockAccessor.ce$dynamicShape());
        customBlockAccessor.ce$requiredFeatures(vanillaBlockAccessor.ce$requiredFeatures());
        customBlockAccessor.ce$properties(vanillaBlockAccessor.ce$properties());
        BlockStateBaseAccessor customStateAccessor = (BlockStateBaseAccessor) customState;
        BlockStateBaseAccessor vanillaStateAccessor = (BlockStateBaseAccessor) vanillaState;
        customStateAccessor.ce$lightEmission(vanillaStateAccessor.ce$lightEmission());
        customStateAccessor.ce$useShapeForLightOcclusion(vanillaStateAccessor.ce$useShapeForLightOcclusion());
        customStateAccessor.ce$isAir(vanillaStateAccessor.ce$isAir());
        customStateAccessor.ce$ignitedByLava(vanillaStateAccessor.ce$ignitedByLava());
        customStateAccessor.ce$liquid(vanillaStateAccessor.ce$liquid());
        customStateAccessor.ce$pushReaction(vanillaStateAccessor.ce$pushReaction());
        customStateAccessor.ce$mapColor(vanillaStateAccessor.ce$mapColor());
        customStateAccessor.ce$destroySpeed(vanillaStateAccessor.ce$destroySpeed());
        customStateAccessor.ce$requiresCorrectToolForDrops(vanillaStateAccessor.ce$requiresCorrectToolForDrops());
        customStateAccessor.ce$canOcclude(vanillaStateAccessor.ce$canOcclude());
        customStateAccessor.ce$spawnParticlesOnBreak(vanillaStateAccessor.ce$spawnParticlesOnBreak());
        customStateAccessor.ce$instrument(vanillaStateAccessor.ce$instrument());
        customStateAccessor.ce$replaceable(vanillaStateAccessor.ce$replaceable());
        customStateAccessor.ce$isRandomlyTicking(vanillaStateAccessor.ce$isRandomlyTicking());
        customStateAccessor.ce$offsetFunction(vanillaStateAccessor.ce$offsetFunction());
        craftEngineBlockState.initCache();
        customStateAccessor.ce$fluidState(vanillaStateAccessor.ce$fluidState());
        customBlockAccessor.ce$isRandomlyTicking(vanillaBlockAccessor.ce$isRandomlyTicking());
        customStateAccessor.ce$legacySolid(vanillaStateAccessor.ce$legacySolid());
        rebindTags(craftEngineBlock, vanillaBlock);
    }

    public static void handleRemapTag(int customId, int vanillaId) {
        BlockState vanillaState = Block.BLOCK_STATE_REGISTRY.byId(vanillaId);
        if (vanillaState == null) return;
        Block vanillaBlock = vanillaState.getBlock();
        BlockState customState = Block.BLOCK_STATE_REGISTRY.byId(customId);
        if (customState == null) return;
        if (!(customState.getBlock() instanceof CraftEngineBlock craftEngineBlock)) return;
        rebindTags(craftEngineBlock, vanillaBlock);
    }

    @SuppressWarnings("unchecked")
    private static void rebindTags(CraftEngineBlock craftEngineBlock, Block vanillaBlock) {
        Holder<Block> vanillaBlockHolder = net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(vanillaBlock);
        Holder<Block> customBlockHolder = net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(craftEngineBlock);
        ((HolderReferenceInvoker<Block>) customBlockHolder).ce$tags(((HolderReferenceInvoker<Block>) vanillaBlockHolder).ce$tags());
    }
}
