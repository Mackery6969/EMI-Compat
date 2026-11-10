package mackery.emicompat.compat.travelersbackpack;

import java.util.List;

import com.tiviacz.travelersbackpack.inventory.menu.BackpackBaseMenu;
import com.tiviacz.travelersbackpack.inventory.upgrades.crafting.CraftingUpgrade;

import mackery.emicompat.Config;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/**
 * Decides which Traveler's Backpack slots count as EMI recipe ingredient sources.
 * <p>
 * With a recipe tree in crafting mode, EMI tints container slots holding items the tree still needs. It skips every
 * slot a recipe handler reports as an input source, and counts those items toward the tree. Traveler's Backpack's
 * handler reports every backpack slot as an input source, even though EMI can only craft from the backpack when a
 * Crafting Upgrade is installed. Without one, backpack items counted toward the tree and were never highlighted.
 */
public final class BackpackSlotHighlights {
    private BackpackSlotHighlights() {
    }

    /**
     * Narrows the backpack handler's input sources to the player's inventory when the backpack has no Crafting
     * Upgrade, so the backpack's slots are highlighted like a vanilla chest's.
     */
    public static List<Slot> countedInputSources(BackpackBaseMenu menu, List<Slot> inputSources) {
        if (!Config.isEnabled(Config.TRAVELERS_BACKPACK_HIGHLIGHT_BACKPACK_SLOTS)) {
            return inputSources;
        }
        // Same check as Traveler's Backpack's own EmiCompat.GridMenuInfo#canCraft.
        if (menu.getWrapper().getUpgradeManager().getUpgrade(CraftingUpgrade.class).isPresent()) {
            return inputSources;
        }
        return inputSources.stream().filter(slot -> slot.container instanceof Inventory).toList();
    }
}
