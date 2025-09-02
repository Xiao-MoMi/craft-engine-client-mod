package net.momirealms.craftengine.fabric.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.momirealms.craftengine.fabric.CraftEngineFabricMod;
import net.momirealms.craftengine.fabric.util.BlockStateUtils;
import net.momirealms.craftengine.fabric.util.Reflections;
import net.momirealms.craftengine.fabric.util.StringKeyConstructor;
import net.momirealms.craftengine.fabric.util.Tuple;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Environment(EnvType.CLIENT)
public class BlockManager {
    private static BlockManager instance;
    private final CraftEngineFabricMod mod;

    // The total amount of blocks registered
    private int customBlockCount;
    // Minecraft objects
    // Cached new blocks $ holders
    private Map<Integer, Holder.Reference<Block>> stateId2BlockHolder;
    // This map is used to change the block states that are not necessarily needed into a certain block state
    private Map<Integer, Integer> blockAppearanceMapper;
    // Record the amount of real blocks by block type
    private Map<ResourceLocation, Integer> registeredRealBlockSlots;
    // A list to record the order of registration
    private List<ResourceLocation> blockRegisterOrder = new ObjectArrayList<>();

    private final List<Tuple<Block, ResourceLocation, Boolean>> blocksToDeceive = new ArrayList<>();

    // Used to automatically arrange block states for strings such as minecraft:note_block:0
    protected Map<ResourceLocation, List<Integer>> blockAppearanceArranger;
    protected Map<ResourceLocation, List<Integer>> realBlockArranger;
    protected Map<ResourceLocation, Integer> internalId2StateId;
    protected Map<ResourceLocation, CraftEngineBlock> registeredBlocks;

    public BlockManager(CraftEngineFabricMod mod) {
        instance = this;
        this.mod = mod;
        this.initVanillaRegistry();
        this.loadMappingsAndAdditionalBlocks();
        this.registerBlocks();
    }

    public static BlockManager instance() {
        return instance;
    }

    private void initVanillaRegistry() {
        int vanillaStateCount = Block.BLOCK_STATE_REGISTRY.size();
        this.mod.logger().info("Vanilla block count: " + vanillaStateCount);
        BlockStateUtils.init(vanillaStateCount);
    }

    private void loadMappingsAndAdditionalBlocks() {
        this.mod.logger().info("Loading mappings.yml.");
        Path mappingsFile = this.mod.dataFolderPath().resolve("mappings.yml");
        Path additionalFile = this.mod.dataFolderPath().resolve("additional-real-blocks.yml");
        if (!Files.exists(mappingsFile) || !Files.exists(additionalFile)) {
            this.registeredRealBlockSlots = Map.of();
            return;
        }
        Yaml yaml = new Yaml(new StringKeyConstructor(new LoaderOptions()));
        Map<ResourceLocation, Integer> blockTypeCounter = new LinkedHashMap<>();
        try (InputStream is = Files.newInputStream(mappingsFile)) {
            Map<String, String> blockStateMappings = loadBlockStateMappings(yaml.load(is));
            this.validateBlockStateMappings(mappingsFile, blockStateMappings);
            Map<Integer, String> stateMap = new Int2ObjectOpenHashMap<>();
            Map<Integer, Integer> appearanceMapper = new Int2IntOpenHashMap();
            Map<ResourceLocation, List<Integer>> appearanceArranger = new HashMap<>();
            for (Map.Entry<String, String> entry : blockStateMappings.entrySet()) {
                this.processBlockStateMapping(mappingsFile, entry, stateMap, blockTypeCounter, appearanceMapper, appearanceArranger);
            }
            this.blockAppearanceMapper = ImmutableMap.copyOf(appearanceMapper);
            this.blockAppearanceArranger = ImmutableMap.copyOf(appearanceArranger);
            this.mod.logger().info("Loader " + this.blockAppearanceMapper.size() + " block state appearances.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to init mappings.yml", e);
        }
        try (InputStream is = Files.newInputStream(additionalFile)) {
            this.registeredRealBlockSlots = this.buildRegisteredRealBlockSlots(blockTypeCounter, yaml.load(is));
        } catch (IOException e) {
            throw new RuntimeException("Failed to init additional-real-blocks.yml", e);
        }
    }

    private Map<String, String> loadBlockStateMappings(Map<String, Object> mappings) {
        Map<String, String> blockStateMappings = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : mappings.entrySet()) {
            if (entry.getValue() instanceof String afterValue) {
                blockStateMappings.put(entry.getKey(), afterValue);
            }
        }
        return blockStateMappings;
    }

    private void validateBlockStateMappings(Path mappingFile, Map<String, String> blockStateMappings) {
        Map<String, String> temp = new HashMap<>(blockStateMappings);
        for (Map.Entry<String, String> entry : temp.entrySet()) {
            String state = entry.getValue();
            if (blockStateMappings.containsKey(state)) {
                String after = blockStateMappings.remove(state);
                this.mod.logger().warn(mappingFile, "'" + state + ": " + after + "' is invalid because '" + state + "' has already been used as a base block.");
            }
        }
    }

    private BlockState createBlockState(Path mappingFile, String state) {
        try {
            return BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK, state, false).blockState();
        } catch (Exception e) {
            this.mod.logger().warn(mappingFile, "'" + state + "' is not a valid block state.");
            return null;
        }
    }

    private ResourceLocation blockOwnerFromString(String stateString) {
        int index = stateString.indexOf('[');
        if (index == -1) {
            return ResourceLocation.parse(stateString);
        } else {
            return ResourceLocation.parse(stateString.substring(0, index));
        }
    }

    private void processBlockStateMapping(Path mappingFile,
                                          Map.Entry<String, String> entry,
                                          Map<Integer, String> stateMap,
                                          Map<ResourceLocation, Integer> counter,
                                          Map<Integer, Integer> mapper,
                                          Map<ResourceLocation, List<Integer>> arranger) {
        BlockState before = createBlockState(mappingFile, entry.getKey());
        BlockState after = createBlockState(mappingFile, entry.getValue());
        if (before == null || after == null) return;

        int beforeId = Block.BLOCK_STATE_REGISTRY.getId(before);
        int afterId = Block.BLOCK_STATE_REGISTRY.getId(after);

        Integer previous = mapper.put(beforeId, afterId);
        if (previous == null) {
            ResourceLocation key = blockOwnerFromString(entry.getKey());
            counter.compute(key, (k, count) -> count == null ? 1 : count + 1);
            stateMap.put(beforeId, entry.getKey());
            stateMap.put(afterId, entry.getValue());
            arranger.computeIfAbsent(key, (k) -> new IntArrayList()).add(beforeId);
        } else {
            String previousState = stateMap.get(previous);
            this.mod.logger().warn(mappingFile, "Duplicate entry: '" + previousState + "' equals '" + entry.getKey() + "'");
        }
    }

    private LinkedHashMap<ResourceLocation, Integer> buildRegisteredRealBlockSlots(Map<ResourceLocation, Integer> counter, Map<String, Object> additionalYaml) {
        LinkedHashMap<ResourceLocation, Integer> map = new LinkedHashMap<>(counter);
        for (Map.Entry<String, Object> entry : additionalYaml.entrySet()) {
            ResourceLocation blockType = ResourceLocation.parse(entry.getKey());
            if (entry.getValue() instanceof Integer i) {
                int previous = map.getOrDefault(blockType, 0);
                if (previous == 0) {
                    map.put(blockType, i);
                    this.mod.logger().info("Loaded " + blockType + " with " + i + " real block states");
                } else {
                    map.put(blockType, i + previous);
                    this.mod.logger().info("Loaded " + blockType + " with " + previous + " appearances and " + (i + previous) + " real block states");
                }
            }
        }
        return map;
    }

    private void registerBlocks() {
        this.mod.logger().info("Registering blocks. Please wait...");
        try {
            ImmutableMap.Builder<ResourceLocation, Integer> builder1 = ImmutableMap.builder();
            ImmutableMap.Builder<Integer, Holder.Reference<Block>> builder2 = ImmutableMap.builder();
            ImmutableMap.Builder<ResourceLocation, List<Integer>> builder3 = ImmutableMap.builder();
            ImmutableMap.Builder<ResourceLocation, CraftEngineBlock> builder4 = ImmutableMap.builder();
            Set<SoundType> affectedBlockSounds = new HashSet<>();
            List<ResourceLocation> order = new ArrayList<>();
            int counter = 0;
            for (Map.Entry<ResourceLocation, Integer> baseBlockAndItsCount : this.registeredRealBlockSlots.entrySet()) {
                counter = registerBlockVariants(baseBlockAndItsCount, counter, builder1, builder2, builder3, builder4, affectedBlockSounds, order);
            }
            this.mod.logger().info("Registered block count: " + counter);
            this.customBlockCount = counter;
            this.internalId2StateId = builder1.build();
            this.stateId2BlockHolder = builder2.build();
            this.realBlockArranger = builder3.build();
            this.registeredBlocks = builder4.build();
            this.blockRegisterOrder = ImmutableList.copyOf(order);
        } catch (Throwable e) {
            this.mod.logger().warn("Failed to register blocks.", e);
        }
    }

    private int registerBlockVariants(Map.Entry<ResourceLocation, Integer> blockWithCount,
                                      int counter,
                                      ImmutableMap.Builder<ResourceLocation, Integer> builder1,
                                      ImmutableMap.Builder<Integer, Holder.Reference<Block>> builder2,
                                      ImmutableMap.Builder<ResourceLocation, List<Integer>> builder3,
                                      ImmutableMap.Builder<ResourceLocation, CraftEngineBlock> builder4,
                                      Set<SoundType> affectSoundTypes,
                                      List<ResourceLocation> order) throws Exception {
        ResourceLocation clientSideBlockType = blockWithCount.getKey();
        boolean isNoteBlock = clientSideBlockType.equals(BlockKeys.NOTE_BLOCK);
        Block clientSideBlock = BuiltInRegistries.BLOCK.getValue(clientSideBlockType);
        int amount = blockWithCount.getValue();
        List<Integer> stateIds = new IntArrayList();
        List<Integer> blockStateIds = blockAppearanceArranger.get(clientSideBlockType);
        if (blockStateIds == null) {
            // 为什么拿不到
            blockStateIds = new ArrayList<>();
            blockStateIds.add(Block.BLOCK_STATE_REGISTRY.getId(clientSideBlock.defaultBlockState()));
        }

        for (int i = 0; i < amount; i++) {
            ResourceLocation realBlockKey = ResourceLocation.fromNamespaceAndPath("craftengine", clientSideBlockType.getPath() + "_" + i);
            BlockBehaviour.Properties blockProperties = createBlockProperties(realBlockKey);

            Block newRealBlock;
            Holder.Reference<Block> blockHolder;

            try {
                int blockStateId = blockStateIds.get(i % blockStateIds.size());
                BlockState clientSideBlockState = Objects.requireNonNull(Block.BLOCK_STATE_REGISTRY.byId(blockStateId));
                newRealBlock = CraftEngineBlock.generateBlock(clientSideBlockType, clientSideBlock, clientSideBlockState, blockProperties);
            } catch (ReflectiveOperationException throwable) {
                this.mod.logger().warn("Failed to generate block", throwable);
                continue;
            }
            blockHolder = Registry.registerForHolder(BuiltInRegistries.BLOCK, realBlockKey, newRealBlock);;
            Reflections.method$Holder$Reference$bindValue.invoke(blockHolder, newRealBlock);
            Reflections.field$Holder$Reference$tags.set(blockHolder, Set.of());

            int stateId = BlockStateUtils.vanillaStateSize() + counter;

            builder1.put(realBlockKey, stateId);
            builder2.put(stateId, blockHolder);
            builder4.put(realBlockKey, (CraftEngineBlock) newRealBlock);
            stateIds.add(stateId);

            this.blocksToDeceive.add(Tuple.of(newRealBlock, clientSideBlockType, isNoteBlock));
            order.add(realBlockKey);
            counter++;
        }

        builder3.put(clientSideBlockType, stateIds);
        SoundType soundType = (SoundType) Reflections.field$BlockBehaviour$soundType.get(clientSideBlock);
        affectSoundTypes.add(soundType);
        return counter;
    }

    private BlockBehaviour.Properties createBlockProperties(ResourceLocation realBlockKey) {
        BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties.of();
        ResourceKey<Block> realBlockResourceKey = ResourceKey.create(Registries.BLOCK, realBlockKey);
        blockProperties.setId(realBlockResourceKey);
        return blockProperties;
    }

    public int customBlockCount() {
        return customBlockCount;
    }

    public Map<Integer, Holder.Reference<Block>> stateId2BlockHolder() {
        return stateId2BlockHolder;
    }

    public Map<Integer, Integer> blockAppearanceMapper() {
        return blockAppearanceMapper;
    }

    public Map<ResourceLocation, Integer> registeredRealBlockSlots() {
        return registeredRealBlockSlots;
    }

    public List<ResourceLocation> blockRegisterOrder() {
        return blockRegisterOrder;
    }

    public List<Tuple<Block, ResourceLocation, Boolean>> blocksToDeceive() {
        return blocksToDeceive;
    }

    public Map<ResourceLocation, List<Integer>> blockAppearanceArranger() {
        return blockAppearanceArranger;
    }
    public Map<ResourceLocation, List<Integer>> realBlockArranger() {
        return realBlockArranger;
    }
    public Map<ResourceLocation, Integer> internalId2StateId() {
        return internalId2StateId;
    }
    public Map<ResourceLocation, CraftEngineBlock> registeredBlocks() {
        return registeredBlocks;
    }
}
