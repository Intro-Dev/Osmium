package com.intro.client.render.cape;

import com.intro.client.render.texture.DynamicAnimation;
import net.minecraft.resources.ResourceLocation;

public class Cape implements Cloneable {

    private final DynamicAnimation texture;
    public boolean isOptifine;
    public boolean isAnimated;

    public String source;
    public String registryName;

    public String creator = "unknown";


    public Cape(DynamicAnimation texture, boolean isOptifine, boolean isAnimated, String source, String registryName, String creator) {
        this.registryName = registryName;
        this.texture = texture;
        this.isOptifine = isOptifine;
        this.isAnimated = isAnimated;
        this.source = source;
        this.creator = creator;
    }

    public DynamicAnimation getTexture() {
        return texture;
    }

    public ResourceLocation getFrameTexture() {
        return texture.getCurrentFrameLocation();
    }

    public ResourceLocation getFirstFrameTexture() {
        return texture.getFrameLocation(0);
    }

    public void nextFrame() {
        texture.tick();
    }

    public void free() {
        texture.free();
    }


    @Override
    public Cape clone() {
        try {
            return (Cape) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
