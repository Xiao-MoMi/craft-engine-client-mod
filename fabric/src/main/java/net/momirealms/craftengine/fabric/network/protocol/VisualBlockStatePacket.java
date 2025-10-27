package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.momirealms.craftengine.fabric.block.CraftEngineBlock;
import net.momirealms.craftengine.fabric.block.CraftEngineBlockState;
import net.momirealms.craftengine.fabric.mixin.BlockBehaviourAccessor;
import net.momirealms.craftengine.fabric.mixin.BlockStateBaseAccessor;
import net.momirealms.craftengine.fabric.network.Context;
import net.momirealms.craftengine.fabric.network.ModPacket;
import net.momirealms.craftengine.fabric.registries.BuiltInRegistries;
import net.momirealms.craftengine.fabric.util.BlockRenderUtils;

@Environment(EnvType.CLIENT)
public record VisualBlockStatePacket(int[] data) implements ModPacket {
    public static final ResourceKey<StreamCodec<FriendlyByteBuf, ? extends ModPacket>> TYPE = ResourceKey.create(
            BuiltInRegistries.MOD_PACKET.key(), ResourceLocation.fromNamespaceAndPath("craftengine", "visual_block_state")
    );
    public static final StreamCodec<FriendlyByteBuf, VisualBlockStatePacket> CODEC = ModPacket.codec(
            VisualBlockStatePacket::encode,
            VisualBlockStatePacket::new
    );

    private VisualBlockStatePacket(FriendlyByteBuf buf) {
        this(buf.readVarIntArray());
    }

    private void encode(FriendlyByteBuf buf) {
        buf.writeVarIntArray(this.data);
    }

    @Override
    public ResourceKey<StreamCodec<FriendlyByteBuf, ? extends ModPacket>> type() {
        return TYPE;
    }

    @Override
    public void handle(Context context) {
        for (int customId = 0; customId < data.length; customId++) {
            int vanillaId = data[customId];
            if (vanillaId == 0) {
                data[customId] = customId;
                continue;
            }
            BlockState customState = Block.BLOCK_STATE_REGISTRY.byId(customId);
            if (!(customState instanceof CraftEngineBlockState craftEngineBlockState)) continue;
            if (!(craftEngineBlockState.getBlock() instanceof CraftEngineBlock craftEngineBlock)) continue;
            BlockState vanillaState = Block.BLOCK_STATE_REGISTRY.byId(vanillaId);
            if (vanillaState == null) continue;
            craftEngineBlockState.setVisualBlockState(vanillaState);
            Block vanillaBlock = vanillaState.getBlock();
            BlockRenderUtils.registerRenderLayer(craftEngineBlock, vanillaState);
            BlockRenderUtils.registerColor(craftEngineBlock, vanillaBlock);
            BlockBehaviourAccessor customBlockAccessor = (BlockBehaviourAccessor) craftEngineBlock;
            BlockBehaviourAccessor vanillaBlockAccessor = (BlockBehaviourAccessor) vanillaBlock;
            customBlockAccessor.hasCollision(vanillaBlockAccessor.hasCollision());
            customBlockAccessor.explosionResistance(vanillaBlockAccessor.explosionResistance());
            customBlockAccessor.soundType(vanillaBlockAccessor.soundType());
            customBlockAccessor.friction(vanillaBlockAccessor.friction());
            customBlockAccessor.speedFactor(vanillaBlockAccessor.speedFactor());
            customBlockAccessor.jumpFactor(vanillaBlockAccessor.jumpFactor());
            customBlockAccessor.dynamicShape(vanillaBlockAccessor.dynamicShape());
            customBlockAccessor.requiredFeatures(vanillaBlockAccessor.requiredFeatures());
            customBlockAccessor.properties(vanillaBlockAccessor.properties());
            customBlockAccessor.drops(vanillaBlockAccessor.drops());
            BlockStateBaseAccessor customStateAccessor = (BlockStateBaseAccessor) customState;
            BlockStateBaseAccessor vanillaStateAccessor = (BlockStateBaseAccessor) vanillaState;
            customStateAccessor.lightEmission(vanillaStateAccessor.lightEmission());
            customStateAccessor.useShapeForLightOcclusion(vanillaStateAccessor.useShapeForLightOcclusion());
            customStateAccessor.isAir(vanillaStateAccessor.isAir());
            customStateAccessor.ignitedByLava(vanillaStateAccessor.ignitedByLava());
            customStateAccessor.liquid(vanillaStateAccessor.liquid());
            customStateAccessor.pushReaction(vanillaStateAccessor.pushReaction());
            customStateAccessor.mapColor(vanillaStateAccessor.mapColor());
            customStateAccessor.destroySpeed(vanillaStateAccessor.destroySpeed());
            customStateAccessor.requiresCorrectToolForDrops(vanillaStateAccessor.requiresCorrectToolForDrops());
            customStateAccessor.canOcclude(vanillaStateAccessor.canOcclude());
            customStateAccessor.isRedstoneConductor(vanillaStateAccessor.isRedstoneConductor());
            customStateAccessor.isSuffocating(vanillaStateAccessor.isSuffocating());
            customStateAccessor.isViewBlocking(vanillaStateAccessor.isViewBlocking());
            customStateAccessor.hasPostProcess(vanillaStateAccessor.hasPostProcess());
            customStateAccessor.emissiveRendering(vanillaStateAccessor.emissiveRendering());
            customStateAccessor.spawnTerrainParticles(vanillaStateAccessor.spawnTerrainParticles());
            customStateAccessor.instrument(vanillaStateAccessor.instrument());
            customStateAccessor.replaceable(vanillaStateAccessor.replaceable());
            customStateAccessor.isRandomlyTicking(vanillaStateAccessor.isRandomlyTicking());
            customStateAccessor.offsetFunction(vanillaStateAccessor.offsetFunction());
            craftEngineBlockState.initCache();
            customStateAccessor.fluidState(vanillaStateAccessor.fluidState());
            customBlockAccessor.isRandomlyTicking(vanillaBlockAccessor.isRandomlyTicking());
            customStateAccessor.legacySolid(vanillaStateAccessor.legacySolid());
            customStateAccessor.occlusionShape(vanillaStateAccessor.occlusionShape());
            customStateAccessor.solidRender(vanillaStateAccessor.solidRender());
            customStateAccessor.occlusionShapesByFace(vanillaStateAccessor.occlusionShapesByFace());
            customStateAccessor.propagatesSkylightDown(vanillaStateAccessor.propagatesSkylightDown());
            customStateAccessor.lightBlock(vanillaStateAccessor.lightBlock());
        }
    }
}
