package mackery.emicompat;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // --- Sophisticated Backpacks / Sophisticated Storage ---

    public static final ModConfigSpec.BooleanValue SOPHISTICATED_HIGHLIGHT_STORAGE_SLOTS = BUILDER
            .comment("Sophisticated Backpacks and Sophisticated Storage. Only used when Sophisticated Core is installed.")
            .translation("emicompat.configuration.sophisticated")
            .push("sophisticated")
            .comment(
                    "With an EMI recipe tree in crafting mode, highlight Sophisticated Backpacks and Sophisticated",
                    "Storage slots that hold items the tree still needs, the same way EMI does for a vanilla chest.",
                    "Storages with a Crafting Upgrade are left alone, since EMI already counts their contents as",
                    "available.",
                    "Default: true")
            .translation("emicompat.configuration.sophisticated.highlightStorageSlots")
            .define("highlightStorageSlots", true);

    public static final ModConfigSpec.BooleanValue SOPHISTICATED_SKIP_HIDDEN_SLOTS = BUILDER
            .comment(
                    "Sophisticated moves storage slots that are scrolled out of view or hidden by its search box",
                    "off to the side instead of disabling them, so EMI's slot overlays (recipe tree highlights and",
                    "search highlighting) get drawn as stray squares above the screen. This skips those slots.",
                    "Default: true")
            .translation("emicompat.configuration.sophisticated.skipHiddenSlots")
            .define("skipHiddenSlots", true);

    static {
        BUILDER.pop();
    }

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean isEnabled(ModConfigSpec.BooleanValue value) {
        return !SPEC.isLoaded() || value.get();
    }
}
