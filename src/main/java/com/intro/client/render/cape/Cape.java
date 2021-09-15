package com.intro.client.render.cape;

import com.intro.client.render.texture.DynamicAnimation;
import net.minecraft.resources.ResourceLocation;

public class Cape {

    private final DynamicAnimation texture;
    public boolean isOptifine;
    public boolean isAnimated;


    public Cape(DynamicAnimation texture, boolean isOptifine, boolean isAnimated) {
        this.texture = texture;
        this.isOptifine = isOptifine;
        this.isAnimated = isAnimated;
    }

    public ResourceLocation getFrameTexture() {
        return texture.getCurrentFrameLocation();
    }

    public void nextFrame() {
        texture.tick();
    }

    public void free() {
        texture.free();
    }




}
