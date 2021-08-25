package com.intro.client.util;

import com.intro.client.render.drawables.Drawable;
import com.intro.client.render.drawables.Scalable;

public class ElementPosition {

    public int x, y;
    public float scale;

    public ElementPosition(int x, int y, float scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public void loadToDrawable(Drawable drawable) {
        drawable.posX = x;
        drawable.posY = y;
    }

    public void loadToScalable(Scalable scalable) {
        scalable.posX = x;
        scalable.posY = y;
        scalable.scale = scale;
    }
}
