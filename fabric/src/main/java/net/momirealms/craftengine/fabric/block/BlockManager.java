package net.momirealms.craftengine.fabric.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.config.ModConfig;
import net.momirealms.craftengine.fabric.mixin.HolderReferenceInvoker;
import net.momirealms.craftengine.fabric.util.BlockStateUtils;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Environment(EnvType.CLIENT)
public class BlockManager {
    private static BlockManager instance;
    private final CraftEngineFabricMod mod;
    private final CraftEngineBlock[] customBlocks;
    private final CraftEngineBlockState[] customBlockStates;
    private final Holder.Reference<Block>[] customBlockHolders;
    private final int[] stateMappings;
    private final AtomicBoolean handleVisualBlockStateBatchFinished = new AtomicBoolean(true);
    private VisualBlockStatesData visualBlockStatesData;

    @SuppressWarnings("unchecked")
    public BlockManager(CraftEngineFabricMod mod) {
        instance = this;
        this.mod = mod;
        this.customBlocks = new CraftEngineBlock[ModConfig.INSTANCE.serverSideBlocks()];
        this.customBlockStates = new CraftEngineBlockState[ModConfig.INSTANCE.serverSideBlocks()];
        this.customBlockHolders = new Holder.Reference[ModConfig.INSTANCE.serverSideBlocks()];
        this.initVanillaRegistry();
        this.stateMappings = new int[BlockStateUtils.vanillaStateSize() + ModConfig.INSTANCE.serverSideBlocks()];
        for (int i = 0; i < this.stateMappings.length; i++) {
            this.stateMappings[i] = i;
        }
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
        int nextStateId = BlockStateUtils.vanillaStateSize();
        for (int i = 0; i < count; i++) {
            ResourceLocation customBlockId = ResourceLocation.fromNamespaceAndPath("craftengine", "custom_" + i);
            CraftEngineBlock customBlock = CraftEngineBlock.generateBlock(customBlockId);
            this.customBlocks[i] = customBlock;
            Holder.Reference<Block> blockHolder = Registry.registerForHolder(BuiltInRegistries.BLOCK, customBlockId, customBlock);
            this.customBlockHolders[i] = blockHolder;
            @SuppressWarnings("unchecked")
            HolderReferenceInvoker<Block> holderReferenceInvoker = (HolderReferenceInvoker<Block>) blockHolder;
            holderReferenceInvoker.ce$callBindValue(customBlock);
            holderReferenceInvoker.ce$tags(Set.of());
            CraftEngineBlockState newBlockState = (CraftEngineBlockState) customBlock.defaultBlockState();
            int newBlockStateId = Block.BLOCK_STATE_REGISTRY.getId(newBlockState);
            if (nextStateId != newBlockStateId) {
                BlockState actualState = Block.BLOCK_STATE_REGISTRY.byId(nextStateId);
                throw new IllegalStateException("BlockState ID mismatch for " + newBlockState + " (expected " + nextStateId + ", got " + newBlockStateId + ", actual state: " + actualState + ")");
            }
            this.customBlockStates[i] = newBlockState;
            nextStateId++;
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

    public int remapState(int state) {
        return this.stateMappings[state];
    }

    public void remapState(int state, int newState) {
        this.stateMappings[state] = newState;
    }

    public void handleVisualBlockStateBatchStart(int size) {
        if (!this.handleVisualBlockStateBatchFinished.compareAndSet(true, false)) return;
        this.visualBlockStatesData = new VisualBlockStatesData(size);
    }

    public void handleVisualBlockStates(int startIndex, int[] data) {
        if (this.visualBlockStatesData == null || this.visualBlockStatesData.isReceived()) return;
        this.visualBlockStatesData.receiveDataChunk(startIndex, data);
    }

    public void handleVisualBlockStateBatchFinished() {
        if (this.visualBlockStatesData == null || this.visualBlockStatesData.isReceived()) return;
        this.visualBlockStatesData.setReceived();
        int[] data = this.visualBlockStatesData.data;
        for (int i = 0; i < data.length; i++) {
            int customId = i + BlockStateUtils.vanillaStateSize();
            int vanillaId = data[i];
            if (vanillaId == -1) continue;
            BlockStateUtils.handleRemap(customId, vanillaId);
        }
        this.handleVisualBlockStateBatchFinished.set(true);
    }

    public void handleTags() {
        if (this.visualBlockStatesData == null || !this.visualBlockStatesData.isReceived()) return;
        int[] data = this.visualBlockStatesData.data;
        for (int i = 0; i < data.length; i++) {
            int customId = i + BlockStateUtils.vanillaStateSize();
            int vanillaId = data[i];
            if (vanillaId == -1) continue;
            BlockStateUtils.handleRemapTag(customId, vanillaId);
        }
    }
}
