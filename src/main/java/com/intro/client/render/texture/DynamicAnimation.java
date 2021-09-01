package com.intro.client.render.texture;

import com.intro.client.util.TextureUtil;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DynamicAnimation implements Cloneable {

    public int maxAnimationFrames;
    public int frameWidth, frameHeight;
    private int currentFrame = 0;

    private final ResourceLocation registryName;
    private final NativeImage image;

    private final Minecraft mc = Minecraft.getInstance();

    public DynamicAnimation(NativeImage image, String registryName, int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.registryName = mc.getTextureManager().register(registryName, new DynamicTexture(image));
        // offset by 1 to account for the fact that frames start at 0
        this.maxAnimationFrames = (image.getWidth() / frameWidth) - 1;
        System.out.println(maxAnimationFrames);
        this.image = image;
    }

    public NativeImage getCurrentFrameImage() {
        return TextureUtil.subImage(this.image, this.currentFrame * frameWidth, this.currentFrame * frameHeight, frameWidth, frameHeight);
    }

    public AbstractTexture getTexture() {
        return mc.getTextureManager().getTexture(registryName);
    }

    public ResourceLocation getAnimationLocation() {
        return this.registryName;
    }

    public int getAnimationU() {
        return currentFrame * frameWidth;
    }

    public int getAnimationV() {
        return frameHeight;
    }

    public void nextFrame() {
        currentFrame++;
        currentFrame = Mth.clamp(currentFrame, 0, this.maxAnimationFrames);
    }

    private int packUvToInt(int i, int j) {
        return i | j << 16;
    }

    public int getPackedAnimationUV() {
        return packUvToInt(this.getAnimationU(), this.getAnimationV());
    }

    public int getPackedAnimationUVWithOffset(int offU, int offV) {
        return packUvToInt(this.getAnimationU() + offU, this.getAnimationV() + offV);
    }

    public void free() {
        mc.getTextureManager().release(registryName);
        this.image.close();
    }

    @Override
    public DynamicAnimation clone() {
        try {
            return (DynamicAnimation) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
