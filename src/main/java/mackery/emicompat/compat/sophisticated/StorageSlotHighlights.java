package mackery.emicompat.compat.sophisticated;

import java.util.List;

import mackery.emicompat.Config;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.RecipeType;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageInventorySlot;

/**
 * Decides which Sophisticated Core slots EMI's recipe tree highlight may draw on.
 * <p>
 * With a recipe tree in crafting mode, EMI tints container slots holding items the tree still needs. It skips every
 * slot a recipe handler reports as an input source, because those items already count toward the tree. Sophisticated
 * Core's handler reports every storage slot as an input source, but only counts the storage's contents when a
 * Crafting Upgrade is installed. Without one, storage items were neither counted nor highlighted.
 */
public final class StorageSlotHighlights {
    private StorageSlotHighlights() {
    }

    /**
     * Narrows a recipe handler's input sources to the slots EMI really counts on this menu, so the remaining storage
     * slots can be highlighted.
     */
    public static List<Slot> countedInputSources(AbstractContainerMenu menu, List<Slot> inputSources) {
        if (!Config.isEnabled(Config.SOPHISTICATED_HIGHLIGHT_STORAGE_SLOTS)
                || !(menu instanceof StorageContainerMenuBase<?> storageMenu)) {
            return inputSources;
        }
        // Same check as Sophisticated Core's EmiGridMenuInfo#getInventory, which is what EMI counts on these menus.
        if (storageMenu.getOpenOrFirstCraftingContainer(RecipeType.CRAFTING).isPresent()) {
            return inputSources;
        }
        return inputSources.stream().filter(slot -> slot.container instanceof Inventory).toList();
    }

    /**
     * Whether Sophisticated has moved a storage slot out of view instead of disabling it: the scroll panel parks
     * scrolled-away slots at y = -100 and the search filter parks filtered-out slots at x = DISABLED_SLOT_X_POS.
     */
    public static boolean isHiddenStorageSlot(Slot slot) {
        return Config.isEnabled(Config.SOPHISTICATED_SKIP_HIDDEN_SLOTS)
                && slot instanceof StorageInventorySlot
                && (slot.x <= StorageScreenBase.DISABLED_SLOT_X_POS || slot.y < 0);
    }
}
