package net.momirealms.craftengine.fabric.util;

import com.mojang.brigadier.StringReader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlUtils {
    public static final Path CONFIG_DIR = Path.of("config/craft-engine-fabric-mod/");
    private static final RegistryWrapper<Block> registryWrapper = BuiltinRegistries.createWrapperLookup().getOrThrow(RegistryKeys.BLOCK);

    public static <T> T loadConfig(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException(filePath.toString());
        }
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            Yaml yaml = new Yaml(new StringKeyConstructor(new LoaderOptions()));
            return yaml.load(inputStream);
        }
    }

    public static void saveDefaultResource() throws IOException {
        if (!Files.exists(CONFIG_DIR)) {
            Files.createDirectories(CONFIG_DIR);
            Path readme = CONFIG_DIR.resolve("README.text");
            Files.writeString(readme, "Please copy 'mappings.yml' & 'additional-real-blocks.yml' to this folder to apply the configs.");
        }
    }

    public static Map<Identifier, Integer> loadMappingsAndAdditionalBlocks() throws IOException {
        Path mappingPath = CONFIG_DIR.resolve("mappings.yml");
        Path additionalYamlPath = CONFIG_DIR.resolve("additional-real-blocks.yml");
        if (!Files.exists(additionalYamlPath) || !Files.exists(mappingPath)) return Map.of();
        Map<String, String> blockStateMappings = loadConfig(mappingPath);
        validateBlockStateMappings(blockStateMappings);
        Map<Identifier, Integer> blockTypeCounter = new LinkedHashMap<>();
        Map<Integer, Integer> appearanceMapper = new HashMap<>();
        for (Map.Entry<String, String> entry : blockStateMappings.entrySet()) {
            processBlockStateMapping(entry, appearanceMapper, blockTypeCounter);
        }
        Map<String, Integer> additionalYaml = loadConfig(additionalYamlPath);
        return buildRegisteredRealBlockSlots(blockTypeCounter, additionalYaml);
    }

    private static void validateBlockStateMappings(Map<String, String> blockStateMappings) {
        Map<String, String> temp = new HashMap<>(blockStateMappings);
        for (Map.Entry<String, String> entry : temp.entrySet()) {
            String state = entry.getValue();
            blockStateMappings.remove(state);
        }
    }

    private static void processBlockStateMapping(
            Map.Entry<String, String> entry,
            Map<Integer, Integer> stateIdMapper,
            Map<Identifier, Integer> blockUsageCounter
    ) {
        final BlockState sourceState = createBlockData(entry.getKey());
        final BlockState targetState = createBlockData(entry.getValue());

        if (sourceState == null || targetState == null) {
            return;
        }

        final int sourceStateId = Block.STATE_IDS.getRawId(sourceState);
        final int targetStateId = Block.STATE_IDS.getRawId(targetState);

        if (stateIdMapper.putIfAbsent(sourceStateId, targetStateId) == null) {
            final Block sourceBlock = sourceState.getBlock();
            final Identifier blockId = Registries.BLOCK.getId(sourceBlock);
            blockUsageCounter.merge(blockId, 1, Integer::sum);
        }
    }

    public static BlockState createBlockData(String blockState) {
        try {
            StringReader reader = new StringReader(blockState);
            BlockArgumentParser.BlockResult arg = BlockArgumentParser.block(registryWrapper, reader, true);
            return arg.blockState();
        } catch (Exception e) {
            return null;
        }
    }

    private static LinkedHashMap<Identifier, Integer> buildRegisteredRealBlockSlots(Map<Identifier, Integer> counter, Map<String, Integer> additionalYaml) {
        LinkedHashMap<Identifier, Integer> map = new LinkedHashMap<>();
        for (Map.Entry<Identifier, Integer> entry : counter.entrySet()) {
            String id = entry.getKey().toString();
            Integer additionalStates = additionalYaml.get(id);
            int internalIds = entry.getValue() + (additionalStates != null ? additionalStates : 0);
            map.put(entry.getKey(), internalIds);
        }
        return map;
    }
}
