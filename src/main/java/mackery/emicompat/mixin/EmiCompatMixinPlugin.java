package mackery.emicompat.mixin;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.mojang.logging.LogUtils;

import mackery.emicompat.EmiCompat;
import net.neoforged.fml.loading.LoadingModList;

/**
 * Each integration's mixins live in their own sub-package and only apply when the mod they integrate with is
 * installed, so EMI is the only hard dependency.
 */
public class EmiCompatMixinPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<String, String> INTEGRATION_MODS = Map.of(
            "mackery.emicompat.mixin.sophisticated.", EmiCompat.SOPHISTICATEDCORE_MODID);

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        for (Map.Entry<String, String> integration : INTEGRATION_MODS.entrySet()) {
            if (mixinClassName.startsWith(integration.getKey())) {
                boolean present = LoadingModList.get().getModFileById(integration.getValue()) != null;
                if (!present) {
                    LOGGER.info("Skipping {} - {} is not installed", mixinClassName, integration.getValue());
                }
                return present;
            }
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
