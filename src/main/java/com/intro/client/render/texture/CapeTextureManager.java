package com.intro.client.render.texture;

import com.intro.client.util.TextureUtil;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class CapeTextureManager {

    private static final HashMap<String, DynamicAnimation> stitchedCapes = new HashMap<>();

    private static final Minecraft mc = Minecraft.getInstance();

    public static void stitchCapeTextures() {
        System.out.println("stitching textures!");
        put(new ResourceLocation("osmium", "textures/cape/debug_cape.png"), stitchDynamicAnimation(new ResourceLocation("osmium", "textures/cape/debug_cape.png"), "debug_cape"));
    }

    public static DynamicAnimation getStitchedCape(ResourceLocation location) {
        return stitchedCapes.get(location.getNamespace() + location.getPath());
    }

    private static void put(ResourceLocation location, DynamicAnimation image) {
        stitchedCapes.put(location.getNamespace() + location.getPath(), image);
    }

    public static DynamicAnimation stitchDynamicAnimation(ResourceLocation location, String registryName) {
        NativeImage image = TextureUtil.getImageAtLocation(location);
        NativeImage stitchedImage = new NativeImage(image.getWidth(), image.getWidth(), false);
        int textureFrames = image.getWidth() / 64;

        if(textureFrames == 1) {
            return new DynamicAnimation(image, registryName, 64, 32);
        }

        while(textureFrames > 0) {
            NativeImage subImage = TextureUtil.subImage(image, textureFrames * 64 - 64, 0, 64, 32);
            stitchedImage = TextureUtil.stitchImagesOnX(stitchedImage, subImage);
            textureFrames--;
        }

        return new DynamicAnimation(stitchedImage, registryName, 64, 32);
    }


}
