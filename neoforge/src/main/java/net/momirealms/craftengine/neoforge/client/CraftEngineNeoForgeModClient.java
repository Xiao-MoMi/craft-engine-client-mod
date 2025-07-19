package net.momirealms.craftengine.neoforge.client;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.momirealms.craftengine.neoforge.CraftEngineNeoForgeMod;
import net.momirealms.craftengine.neoforge.client.blocks.CustomBlock;
import net.momirealms.craftengine.neoforge.client.network.CraftEnginePayload;
import net.momirealms.craftengine.neoforge.client.util.NetWorkDataTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@Mod(value = CraftEngineNeoForgeMod.MOD_ID, dist = Dist.CLIENT)
public class CraftEngineNeoForgeModClient {
    public static boolean serverInstalled = false;

    public CraftEngineNeoForgeModClient(ModContainer container) {
        NeoForge.EVENT_BUS.register(this);
    }

    @SuppressWarnings("deprecation")
    public static void registerRenderLayer() {
        BuiltInRegistries.BLOCK.forEach(block -> {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            if (block instanceof CustomBlock customBlock) {
                if (customBlock.isTransparent()) {
                    ItemBlockRenderTypes.setRenderLayer(customBlock, RenderType.cutoutMipped());
                }
                if (id.getPath().contains("leaves")) {
                    registerColor(block);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    public static void registerColor(Block block) {
        Minecraft.getInstance().getBlockColors().register(
                (state, world, pos, tintIndex) -> {
                    if (world != null && pos != null) {
                        return BiomeColors.getAverageFoliageColor(world, pos);
                    }
                    return FoliageColor.FOLIAGE_DEFAULT;
                },
                block
        );
    }

    @SubscribeEvent
    public void onClientDisconnected(ClientPlayerNetworkEvent.LoggingOut event) {
        serverInstalled = false;
    }

    public static void handleReceiver(CraftEnginePayload payload, IPayloadContext context) {
        byte[] data = payload.data();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
        NetWorkDataTypes type = buf.readEnum(NetWorkDataTypes.class);
        if (type == NetWorkDataTypes.CANCEL_BLOCK_UPDATE) {
            serverInstalled = type.decode(buf);
        }
    }
}
