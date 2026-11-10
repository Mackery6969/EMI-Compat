package mackery.emicompat.mixin.travelersbackpack;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.tiviacz.travelersbackpack.inventory.menu.BackpackBaseMenu;

import mackery.emicompat.compat.travelersbackpack.BackpackSlotHighlights;
import net.minecraft.world.inventory.Slot;

/**
 * Traveler's Backpack: patches the EMI recipe handler it registers for its backpack menus. Only applied when
 * Traveler's Backpack is installed (see {@link mackery.emicompat.mixin.EmiCompatMixinPlugin}).
 * <p>
 * The handler's input sources decide both what EMI counts toward the recipe tree (through
 * {@code StandardRecipeHandler#getInventory}) and which slots its highlight skips, so narrowing them fixes both.
 */
@Mixin(targets = "com.tiviacz.travelersbackpack.compat.emi.EmiCompat$GridMenuInfo", remap = false)
public abstract class GridMenuInfoMixin {

    @ModifyReturnValue(
            method = "getInputSources(Lcom/tiviacz/travelersbackpack/inventory/menu/BackpackBaseMenu;)Ljava/util/List;",
            at = @At("RETURN")
    )
    private List<Slot> emicompat$onlyCountedInputSources(List<Slot> inputSources, BackpackBaseMenu menu) {
        return BackpackSlotHighlights.countedInputSources(menu, inputSources);
    }
}
