package com.intro.client.render.texture;

import com.intro.client.util.TextureUtil;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

// the way this is done is really not the best, but minecraft rendering code has forced my hand
// it registers individual frames and doesn't use UV coordinates
public class DynamicAnimation implements Cloneable {

    public int maxAnimationFrames;
    public int frameWidth, frameHeight;

    public int getFrameDelay() {
        return frameDelay;
    }

    private final int frameDelay;
    private int frameDelayTicker = 0;

    private int currentFrame = 0;

    public String getRegistryName() {
        return registryName;
    }

    private final String registryName;

    private final HashMap<Integer, ResourceLocation> frames = new HashMap<>();
    public final NativeImage image;

    private final Minecraft mc = Minecraft.getInstance();

    public DynamicAnimation(NativeImage image, String registryName, int frameWidth, int frameHeight, int frameDelay) {
        this.image = image;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameDelay = frameDelay;
        // offset by 1 to account for the fact that frames start at 0
        this.maxAnimationFrames = (image.getWidth() / frameWidth) - 1;

        this.registryName = registryName;

        for(int i = 0; i <= maxAnimationFrames; i++) {
            frames.put(i, mc.getTextureManager().register(registryName + "i", new DynamicTexture(TextureUtil.subImage(image, i * frameWidth, 0, frameWidth, frameHeight))));
        }
    }

    public ResourceLocation getFrameLocation(int frame) {
        return frames.get(frame);
    }


    public AbstractTexture getTexture() {
        return mc.getTextureManager().getTexture(getCurrentFrameLocation());
    }

    public ResourceLocation getCurrentFrameLocation()    {
        return frames.get(this.currentFrame);
    }


    public void tick() {
        if(frameDelayTicker > frameDelay) {
            frameDelayTicker = 0;
            currentFrame++;
            if(currentFrame > this.maxAnimationFrames) {
                currentFrame = 0;
            }
        } else {
            frameDelayTicker++;
        }

    }


    public void free() {
        for(ResourceLocation location : frames.values()) {
            mc.getTextureManager().release(location);
        }
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
