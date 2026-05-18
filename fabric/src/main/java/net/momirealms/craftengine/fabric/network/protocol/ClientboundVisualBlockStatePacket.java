package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.momirealms.craftengine.fabric.block.BlockManager;
import net.momirealms.craftengine.fabric.block.CraftEngineBlock;
import net.momirealms.craftengine.fabric.block.CraftEngineBlockState;
import net.momirealms.craftengine.fabric.mixin.BlockBehaviourAccessor;
import net.momirealms.craftengine.fabric.mixin.BlockStateBaseAccessor;
import net.momirealms.craftengine.fabric.mixin.HolderReferenceInvoker;
import net.momirealms.craftengine.fabric.network.ClientCustomPacket;
import net.momirealms.craftengine.fabric.network.Context;
import net.momirealms.craftengine.fabric.util.BlockRenderUtils;
import net.momirealms.craftengine.fabric.util.BlockStateUtils;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
@SuppressWarnings({"unchecked", "DuplicatedCode"})
public record ClientboundVisualBlockStatePacket(int[] data) implements ClientCustomPacket {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("craftengine", "visual_block_state");
    public static final Type<ClientboundVisualBlockStatePacket> TYPE = new Type<>(ID);
    private static ClientboundVisualBlockStatePacket previousPacket;
    public static final StreamCodec<FriendlyByteBuf, ClientboundVisualBlockStatePacket> CODEC = ClientCustomPacket.codec(
            (packet, buf) -> buf.writeVarIntArray(packet.data),
            buf -> previousPacket = new ClientboundVisualBlockStatePacket(buf.readVarIntArray())
    );

    public static void handleTags() {
        if (previousPacket == null) return;
        for (int i = 0; i < previousPacket.data.length; i++) {
            int customId = i + BlockStateUtils.vanillaStateSize();
            int vanillaId = previousPacket.data[i];
            if (vanillaId == 0) continue;
            BlockState vanillaState = Block.BLOCK_STATE_REGISTRY.byId(vanillaId);
            if (vanillaState == null) continue;
            Block vanillaBlock = vanillaState.getBlock();
            BlockState customState = Block.BLOCK_STATE_REGISTRY.byId(customId);
            if (customState == null) continue;
            if (!(customState.getBlock() instanceof CraftEngineBlock craftEngineBlock)) continue;
            Holder<Block> vanillaBlockHolder = net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(vanillaBlock);
            Holder<Block> customBlockHolder = net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(craftEngineBlock);
            ((HolderReferenceInvoker<Block>) customBlockHolder).ce$tags(((HolderReferenceInvoker<Block>) vanillaBlockHolder).ce$tags());
        }
    }

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, ClientboundVisualBlockStatePacket> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Type<ClientboundVisualBlockStatePacket> type() {
        return TYPE;
    }

    @Override
    public void handle(Context context) {
        for (int i = 0; i < data.length; i++) {
            int customId = i + BlockStateUtils.vanillaStateSize();
            int vanillaId = data[i];
            if (vanillaId == 0) continue;
            BlockManager.instance().remapState(customId, vanillaId);
            BlockState customState = Block.BLOCK_STATE_REGISTRY.byId(customId);
            if (!(customState instanceof CraftEngineBlockState craftEngineBlockState)) continue;
            if (!(craftEngineBlockState.getBlock() instanceof CraftEngineBlock craftEngineBlock)) continue;
            BlockState vanillaState = Block.BLOCK_STATE_REGISTRY.byId(vanillaId);
            if (vanillaState == null) continue;
            craftEngineBlockState.setVisualBlockState(vanillaState);
            Block vanillaBlock = vanillaState.getBlock();
            craftEngineBlock.setVisualBlock(vanillaBlock);
            BlockRenderUtils.registerColor(craftEngineBlock, vanillaState);
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
            customBlockAccessor.ce$drops(vanillaBlockAccessor.ce$drops());
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
            customStateAccessor.ce$spawnTerrainParticles(vanillaStateAccessor.ce$spawnTerrainParticles());
            customStateAccessor.ce$instrument(vanillaStateAccessor.ce$instrument());
            customStateAccessor.ce$replaceable(vanillaStateAccessor.ce$replaceable());
            customStateAccessor.ce$isRandomlyTicking(vanillaStateAccessor.ce$isRandomlyTicking());
            customStateAccessor.ce$offsetFunction(vanillaStateAccessor.ce$offsetFunction());
            craftEngineBlockState.initCache();
            customStateAccessor.ce$fluidState(vanillaStateAccessor.ce$fluidState());
            customBlockAccessor.ce$isRandomlyTicking(vanillaBlockAccessor.ce$isRandomlyTicking());
            customStateAccessor.ce$legacySolid(vanillaStateAccessor.ce$legacySolid());
            customStateAccessor.ce$occlusionShape(vanillaStateAccessor.ce$occlusionShape());
            customStateAccessor.ce$solidRender(vanillaStateAccessor.ce$solidRender());
            customStateAccessor.ce$occlusionShapesByFace(vanillaStateAccessor.ce$occlusionShapesByFace());
            customStateAccessor.ce$propagatesSkylightDown(vanillaStateAccessor.ce$propagatesSkylightDown());
            customStateAccessor.ce$lightDampening(vanillaStateAccessor.ce$lightDampening());
            Holder<Block> vanillaBlockHolder = net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(vanillaBlock);
            Holder<Block> customBlockHolder = net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(craftEngineBlock);
            ((HolderReferenceInvoker<Block>) customBlockHolder).ce$tags(((HolderReferenceInvoker<Block>) vanillaBlockHolder).ce$tags());
        }
    }
}
