package net.momirealms.craftengine.neoforge.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void onPlace(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = context.getItemInHand();
        CustomData customData = stack.getComponents().get(DataComponents.CUSTOM_DATA);
        if (customData == null) return;
        if (customData.contains("craftengine:id")) {
            cir.setReturnValue(InteractionResult.FAIL);
            cir.cancel();
        }
    }
}
