package net.momirealms.craftengine.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.momirealms.craftengine.fabric.client.config.ModConfig.enableCancelBlockUpdate;
import static net.momirealms.craftengine.fabric.network.NetworkManager.serverInstalled;

@Environment(EnvType.CLIENT)
@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(method = "place*", at = @At("HEAD"), cancellable = true)
    private void onPlace(BlockPlaceContext blockPlaceContext, CallbackInfoReturnable<InteractionResult> cir) {
        if (!enableCancelBlockUpdate || !serverInstalled) return;
        ItemStack stack = blockPlaceContext.getItemInHand();
        CompoundTag tag = stack.getTag();
        if (tag == null) return;
        if (tag.contains("craftengine:id")) {
            cir.setReturnValue(InteractionResult.FAIL);
            cir.cancel();
        }
    }
}
