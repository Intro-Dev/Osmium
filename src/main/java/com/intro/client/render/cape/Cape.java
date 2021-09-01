package com.intro.client.render.cape;

import com.intro.client.render.texture.DynamicAnimation;

public class Cape {

    public DynamicAnimation texture;

    public Cape(DynamicAnimation texture, boolean isOptifine, boolean isAnimated) {
        this.texture = texture;
        this.isOptifine = isOptifine;
        this.isAnimated = isAnimated;
    }

    public boolean isOptifine = false;
    public boolean isAnimated = false;


}
