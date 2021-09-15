package com.intro.client.render.texture;

import com.intro.client.util.TextureUtil;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class CapeTextureManager {

    private static final HashMap<String, DynamicAnimation> stitchedCapes = new HashMap<>();

    public static void genCapes() {
        putAnimatedCape(new ResourceLocation("osmium", "textures/cape/osmium_dev_cape.png"), genDynamicCape(new ResourceLocation("osmium", "textures/cape/osmium_dev_cape.png"), "osmium_dev_cape", 1));
    }

    public static DynamicAnimation getAnimatedCape(ResourceLocation location) {
        return stitchedCapes.get(location.getNamespace() + location.getPath());
    }

    private static void putAnimatedCape(ResourceLocation location, DynamicAnimation image) {
        stitchedCapes.put(location.getNamespace() + location.getPath(), image);
    }

    public static DynamicAnimation genDynamicCape(ResourceLocation location, String registryName, int frameDelay) {
        return new DynamicAnimation(TextureUtil.getImageAtLocation(location), registryName, 64, 32, frameDelay);
    }


}
