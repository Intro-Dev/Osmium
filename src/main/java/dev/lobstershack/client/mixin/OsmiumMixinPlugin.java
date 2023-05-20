package dev.lobstershack.client.mixin;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class OsmiumMixinPlugin implements IMixinConfigPlugin {

    OsmiumMixinConfiguration configuration;

    private final Logger logger = LogManager.getLogger(OsmiumMixinPlugin.class);

    private final HashMap<String, ImmutableSet<String>> incompatibleMixins = new HashMap<>();

    @Override
    public void onLoad(String mixinPackage) {
        incompatibleMixins.put("colormeoutlines", ImmutableSet.of("com.intro.common.mixin.client.LevelRendererCompatibilityMixin"));
        incompatibleMixins.put("sneaktweak", ImmutableSet.of("com.intro.common.mixin.client.CameraMixin"));

        try {
            configuration = new OsmiumMixinConfiguration(FabricLoader.getInstance().getConfigDir().resolve("osmium-mixin-config.json").toString());
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        for(String modID : incompatibleMixins.keySet()) {
            if(FabricLoader.getInstance().isModLoaded(modID)) {
                if(incompatibleMixins.get(modID).contains(mixinClassName)) {
                    logger.log(Level.INFO, "Disabling mixin: " + mixinClassName + " targeting class " + targetClassName + ", this might break something");
                    return false;
                }
            }
        }
        boolean enabled = configuration.isEnabledInConfig(mixinClassName);

        if(!enabled) logger.log(Level.INFO, "Disabling mixin: " + mixinClassName + " targeting class " + targetClassName + ", this might break something");
        return enabled;
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
