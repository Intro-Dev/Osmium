package com.intro.client.util;

import com.intro.common.mixin.client.ResourceTextureAccessor;
import com.intro.common.mixin.client.ResourceTextureSubclassAccessor;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class TextureUtil {

    private static final Minecraft mc = Minecraft.getInstance();


    public static NativeImage getImageAtLocation(ResourceLocation location) {
        NativeImage TEXTURE;
        mc.getTextureManager().bindForSetup(location);
        SimpleTexture texture = (SimpleTexture) mc.getTextureManager().getTexture(location);
        TEXTURE = getNativeImage(mc.getResourceManager(), texture);
        return TEXTURE;
    }

    private static NativeImage getNativeImage(ResourceManager manager, SimpleTexture texture) {
        SimpleTexture.TextureImage textureImage = loadTextureData(manager, texture);
        return ((ResourceTextureSubclassAccessor) textureImage).getImage();
    }

    private static SimpleTexture.TextureImage loadTextureData(ResourceManager resourceManager, SimpleTexture texture) {
        return SimpleTexture.TextureImage.load(resourceManager, ((ResourceTextureAccessor) texture).getLocation());
    }

    public static NativeImage subImage(NativeImage image, int x, int y, int width, int height) {
        NativeImage subImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
        int subY = 0;
        for(int i = y; i < y + height; i++) {
            int subX = 0;
            for(int j = x; j < x + width; j++) {
                subImage.setPixelRGBA(subX, subY, image.getPixelRGBA(j, i));
                subX++;
            }
            subY++;
        }
        return subImage;
    }

    /**
     * Stitches two NativeImages of equal height together on the X-Axis
     * @param image1 An image with the same height as image2
     * @param image2 An image with the same height as image1
     * @return The stitched image
     */
    public static NativeImage stitchImagesOnX(NativeImage image1, NativeImage image2) {
        NativeImage stitchedImage = new NativeImage(image1.getWidth() + image2.getWidth(), image1.getHeight(), false);

        // fill data for image1
        for(int y = 0; y < image1.getHeight(); y++) {
            for(int x = 0; x < image1.getWidth(); x++) {
                stitchedImage.setPixelRGBA(x, y, image1.getPixelRGBA(x, y));
            }
        }

        // fill data for image 2
        for(int y = 0; y < image2.getHeight(); y++) {
            for(int x = image1.getWidth(); x < image1.getWidth() + image2.getWidth(); x++) {
                stitchedImage.setPixelRGBA(x, y, image2.getPixelRGBA(x - image1.getWidth(), y));
            }
        }

        return stitchedImage;

    }

}
