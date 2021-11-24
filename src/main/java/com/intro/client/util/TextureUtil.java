package com.intro.client.util;

import com.intro.common.mixin.client.ResourceTextureAccessor;
import com.intro.common.mixin.client.ResourceTextureSubclassAccessor;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import org.lwjgl.system.MemoryUtil;

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

    /**
     * <p>Current implementation is very fast, but very memory unsafe</p>
     * @param image the source image
     * @param x starting x of the subImage
     * @param y starting y of the subImage
     * @param width width of the subImage
     * @param height height of the subImage
     * @return the subImage
     */
    public static NativeImage subImage(NativeImage image, int x, int y, int width, int height) {
        x = Mth.clamp(x, 0, image.getWidth());
        width = Mth.clamp(width, 0, image.getWidth());
        y = Mth.clamp(y, 0, image.getHeight());
        height = Mth.clamp(height, 0, image.getHeight());

        NativeImage subImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);

        int subY = 0;

        for(int i = y; i < y + (height); i++) {
            // memory hack
            // uses a single copy per line, as opposed to the massive amounts of reads and writes previously used
            long imageOffset = (x + (long) i * image.getWidth()) * 4L;
            long subImageOffset = ((long) subY * width) * 4L;
            // System.out.println("copying " + image.pixels + imageOffset + " to " + subImage.pixels + subImageOffset + ", bytes: " + width * 4L + ", copyOpNum: " + subY);
            MemoryUtil.memCopy(image.pixels + imageOffset, subImage.pixels + subImageOffset, width * 4L);
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
        /*
        // fill data for image1
        for(int y = 0; y < image1.getHeight(); y++) {
            long imageOffset = ((long) y * image1.getWidth()) * 4L;
            long stitchedImageOffset = ((long) y * image1.getWidth()) * 4L;
            MemoryUtil.memCopy(image1.pixels + imageOffset, stitchedImage.pixels + stitchedImageOffset, image1.getWidth() * 4L);
            imageOffset = ((long) y * image2.getWidth()) * 4L;
            stitchedImageOffset = ((long) y * image2.getWidth()) * 4L;
            MemoryUtil.memCopy(image1.pixels + imageOffset, stitchedImage.pixels + stitchedImageOffset, image2.getWidth() * 4L);


         */
        return new NativeImage(image1.getWidth() + image2.getWidth(), image1.getHeight(), false);
    }

}
