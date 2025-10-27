package net.momirealms.craftengine.fabric.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.config.ModConfig;
import net.momirealms.craftengine.fabric.mixin.HolderReferenceInvoker;
import net.momirealms.craftengine.fabric.util.BlockStateUtils;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class BlockManager {
    private static BlockManager instance;
    private final CraftEngineFabricMod mod;
    private final CraftEngineBlock[] customBlocks;
    private final CraftEngineBlockState[] customBlockStates;
    private final Holder.Reference<Block>[] customBlockHolders;

    @SuppressWarnings("unchecked")
    public BlockManager(CraftEngineFabricMod mod) {
        instance = this;
        this.mod = mod;
        this.customBlocks = new CraftEngineBlock[ModConfig.INSTANCE.serverSideBlocks()];
        this.customBlockStates = new CraftEngineBlockState[ModConfig.INSTANCE.serverSideBlocks()];
        this.customBlockHolders = new Holder.Reference[ModConfig.INSTANCE.serverSideBlocks()];
        this.initVanillaRegistry();
        this.registerServerSideCustomBlocks(ModConfig.INSTANCE.serverSideBlocks());
    }

    public static BlockManager instance() {
        return instance;
    }

    private void initVanillaRegistry() {
        int vanillaStateCount = Block.BLOCK_STATE_REGISTRY.size();
        this.mod.logger().info("Vanilla block count: " + vanillaStateCount);
        BlockStateUtils.init(vanillaStateCount);
    }


    private void registerServerSideCustomBlocks(int count) {
        for (int i = 0; i < count; i++) {
            ResourceLocation customBlockId = ResourceLocation.fromNamespaceAndPath("craftengine", "custom_" + i);
            CraftEngineBlock customBlock = CraftEngineBlock.generateBlock(customBlockId);
            this.customBlocks[i] = customBlock;
            Holder.Reference<Block> blockHolder = Registry.registerForHolder(BuiltInRegistries.BLOCK, customBlockId, customBlock);
            this.customBlockHolders[i] = blockHolder;
            @SuppressWarnings("unchecked")
            HolderReferenceInvoker<Block> holderReferenceInvoker = (HolderReferenceInvoker<Block>) blockHolder;
            holderReferenceInvoker.callBindValue(customBlock);
            holderReferenceInvoker.setTags(Set.of());
            CraftEngineBlockState newBlockState = (CraftEngineBlockState) customBlock.defaultBlockState();
            this.customBlockStates[i] = newBlockState;
        }
        this.mod.logger().info("Registered " + count + " custom blocks.");
    }

    public CraftEngineBlock[] customBlocks() {
        return this.customBlocks;
    }

    public CraftEngineBlockState[] customBlockStates() {
        return this.customBlockStates;
    }

    public Holder.Reference<Block>[] customBlockHolders() {
        return this.customBlockHolders;
    }

}
