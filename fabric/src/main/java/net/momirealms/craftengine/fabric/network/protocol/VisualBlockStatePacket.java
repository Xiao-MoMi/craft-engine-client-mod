package net.momirealms.craftengine.fabric.network.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.momirealms.craftengine.fabric.block.BlockManager;
import net.momirealms.craftengine.fabric.block.CraftEngineBlock;
import net.momirealms.craftengine.fabric.block.CraftEngineBlockState;
import net.momirealms.craftengine.fabric.mixin.BlockBehaviourAccessor;
import net.momirealms.craftengine.fabric.mixin.BlockStateBaseAccessor;
import net.momirealms.craftengine.fabric.mixin.HolderReferenceInvoker;
import net.momirealms.craftengine.fabric.network.Context;
import net.momirealms.craftengine.fabric.network.ModPacket;
import net.momirealms.craftengine.fabric.network.codec.NetworkCodec;
import net.momirealms.craftengine.fabric.registries.BuiltInRegistries;
import net.momirealms.craftengine.fabric.util.BlockRenderUtils;
import net.momirealms.craftengine.fabric.util.BlockStateUtils;

@Environment(EnvType.CLIENT)
@SuppressWarnings({"unchecked", "DuplicatedCode"})
public record VisualBlockStatePacket(int[] data) implements ModPacket {
    public static final ResourceKey<NetworkCodec<FriendlyByteBuf, ? extends ModPacket>> TYPE = ResourceKey.create(
            BuiltInRegistries.MOD_PACKET.key(), ResourceLocation.tryBuild("craftengine", "visual_block_state")
    );
    public static final NetworkCodec<FriendlyByteBuf, VisualBlockStatePacket> CODEC = ModPacket.codec(
            VisualBlockStatePacket::encode,
            VisualBlockStatePacket::new
    );
    private static final int RLE_THRESHOLD = 3;
    private static final int RLE_TAG = 0;
    private static final int DELTA_TAG = 1;
    private static VisualBlockStatePacket previousPacket;

    private VisualBlockStatePacket(FriendlyByteBuf buf) {
        this(decode(buf));
        previousPacket = this;
    }

    private void encode(FriendlyByteBuf buf) {
        encode(buf, this.data);
    }

    private static void encode(FriendlyByteBuf buf, int[] data) {
        if (data.length == 0) {
            buf.writeVarInt(0);
            return;
        }
        buf.writeVarInt(data.length);
        int i = 0;
        int previousValue = 0;
        while (i < data.length) {
            int currentValue = data[i];
            int repeatCount = 1;
            int j = i + 1;
            while (j < data.length && data[j] == currentValue) {
                repeatCount++;
                j++;
            }
            if (repeatCount >= RLE_THRESHOLD) {
                buf.writeVarInt(RLE_TAG);
                buf.writeVarInt(currentValue);
                buf.writeVarInt(repeatCount);
                i += repeatCount;
                previousValue = currentValue;
            } else {
                buf.writeVarInt(DELTA_TAG);
                int delta = currentValue - previousValue;
                buf.writeVarInt(delta);
                previousValue = currentValue;
                i++;
            }
        }
    }

    private static int[] decode(FriendlyByteBuf buf) {
        int length = buf.readVarInt();
        if (length == 0) return new int[0];
        int[] data = new int[length];
        int previousValue = 0;
        int i = 0;
        while (i < length) {
            int tag = buf.readVarInt();
            if (tag == RLE_TAG) {
                int value = buf.readVarInt();
                int count = buf.readVarInt();
                if (i + count > length) throw new RuntimeException("RLE count exceeds array bounds");
                for (int j = 0; j < count; j++) data[i++] = value;
                previousValue = value;
            } else if (tag == DELTA_TAG) {
                int delta = buf.readVarInt();
                int currentValue = previousValue + delta;
                data[i++] = currentValue;
                previousValue = currentValue;
            } else {
                throw new RuntimeException("Unknown encoding tag: " + tag);
            }
        }
        if (i != length) throw new RuntimeException("Decoded length mismatch");
        return data;
    }

    @Override
    public ResourceKey<NetworkCodec<FriendlyByteBuf, ? extends ModPacket>> type() {
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
            customStateAccessor.spawnParticlesOnBreak(vanillaStateAccessor.spawnParticlesOnBreak());
            customStateAccessor.instrument(vanillaStateAccessor.instrument());
            customStateAccessor.replaceable(vanillaStateAccessor.replaceable());
            customStateAccessor.isRandomlyTicking(vanillaStateAccessor.isRandomlyTicking());
            customStateAccessor.offsetFunction(vanillaStateAccessor.offsetFunction());
            craftEngineBlockState.initCache();
            customStateAccessor.fluidState(vanillaStateAccessor.fluidState());
            customBlockAccessor.isRandomlyTicking(vanillaBlockAccessor.isRandomlyTicking());
            customStateAccessor.legacySolid(vanillaStateAccessor.legacySolid());
            Holder<Block> vanillaBlockHolder = net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(vanillaBlock);
            Holder<Block> customBlockHolder = net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(craftEngineBlock);
            ((HolderReferenceInvoker<Block>) customBlockHolder).tags(((HolderReferenceInvoker<Block>) vanillaBlockHolder).tags());
        }
    }

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
            ((HolderReferenceInvoker<Block>) customBlockHolder).tags(((HolderReferenceInvoker<Block>) vanillaBlockHolder).tags());
        }
    }
}
