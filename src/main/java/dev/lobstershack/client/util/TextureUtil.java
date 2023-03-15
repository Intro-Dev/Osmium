package dev.lobstershack.client.util;

import com.mojang.blaze3d.platform.NativeImage;
import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.common.mixin.client.ResourceTextureAccessor;
import dev.lobstershack.common.mixin.client.ResourceTextureSubclassAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;
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
    public static @NotNull NativeImage subImage(@NotNull NativeImage image, int x, int y, int width, int height) {
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
            if(subImage.pixels + subImageOffset + width * 4L > subImage.pixels + (subImage.getWidth() * subImage.getHeight() * 4L)) {
                OsmiumClient.LOGGER.log(Level.WARN, "Tried to copy texture data to invalid address!");
                return subImage;
            }
            MemoryUtil.memCopy(image.pixels + imageOffset, subImage.pixels + subImageOffset, width * 4L);
            subY++;
        }
        return subImage;
    }

    public static @NotNull NativeImage overlayOnImage(@NotNull NativeImage base, @NotNull NativeImage overlay) {
        if(overlay.getHeight() > base.getHeight() || overlay.getWidth() > overlay.getWidth()) throw new IllegalArgumentException("Overlay image larger than base!");
        NativeImage returnImage = new NativeImage(base.getWidth(), base.getHeight(), false);
        long baseImageByteSize = ((long) base.getWidth() * base.getHeight()) * 4L;

        MemoryUtil.memCopy(base.pixels, returnImage.pixels, baseImageByteSize);

        long overlayByteCountPerLine = (overlay.getWidth()) * 4L;

        for(int y = 1; y < overlay.getHeight(); y++) {
            long overlayOffset = ((long) y * overlay.getWidth()) * 4L;
            long returnOffset = ((long) y * returnImage.getWidth()) * 4L;
            // System.out.println("Copying " + overlayByteCountPerLine + " bytes from " + overlayOffset + overlay.pixels + " to " + returnOffset + returnImage.pixels + " , y: " + y);
            MemoryUtil.memCopy(overlayOffset + overlay.pixels, returnOffset + returnImage.pixels, overlayByteCountPerLine);
        }

        return returnImage;
    }

}
