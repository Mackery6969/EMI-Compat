package mackery.emicompat.mixin.sophisticated;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.emi.emi.screen.EmiScreenManager;
import mackery.emicompat.compat.sophisticated.StorageSlotHighlights;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

/**
 * Sophisticated Core: patches the pass where EMI draws recipe tree highlights and search highlights over container
 * slots. Only applied when Sophisticated Core is installed (see {@link mackery.emicompat.mixin.EmiCompatMixinPlugin}).
 */
@Mixin(value = EmiScreenManager.class, remap = false)
public abstract class EmiScreenManagerMixin {

    @WrapOperation(
            method = "renderSlotOverlays",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/emi/emi/api/recipe/handler/StandardRecipeHandler;getInputSources(Lnet/minecraft/world/inventory/AbstractContainerMenu;)Ljava/util/List;"
            )
    )
    private static List<Slot> emicompat$onlyCountedInputSources(StandardRecipeHandler<?> handler,
            AbstractContainerMenu menu, Operation<List<Slot>> original) {
        return StorageSlotHighlights.countedInputSources(menu, original.call(handler, menu));
    }

    @WrapOperation(
            method = "renderSlotOverlays",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/Slot;isActive()Z"
            )
    )
    private static boolean emicompat$skipHiddenStorageSlots(Slot slot, Operation<Boolean> original) {
        return original.call(slot) && !StorageSlotHighlights.isHiddenStorageSlot(slot);
    }
}
