package com.intro.client.render.drawables;

import com.intro.client.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;

/**
 * Indicates a drawable can be scaled
 */
public abstract class Scalable extends Drawable {

    public float scale = 1f;

    public int getScaledWidth() {
        return (int) (this.width * scale);
    }

    public int getScaledHeight() {
        return (int) (this.height * scale);
    }

    /**
     * <p>Gets the scale accounted x value</p>
     * <p>Only to be used to gui editing</p>
     */
    // position accurate scaling
    // very fun
    public int getScaledX() {
        return (int) (posX + (this.getScaledWidth() / (2 * scale)) - this.getScaledWidth() / 2);
    }

    /**
     * <p>Gets the scale accounted y value</p>
     * <p>Only to be used to gui editing</p>
     */
    public int getScaledY() {
        return (int) (posY + (this.getScaledHeight() / (2 * scale)) - this.getScaledHeight() / 2);
    }

    public abstract void onScaleChange(float oldScale, float newScale);

    // transformation matrices are fun
    // right?
    public void scaleWithPositionIntact(PoseStack stack) {
        RenderUtil.positionAccurateScale(stack, scale, posX, posY, width, height);
    }

    @Override
    public boolean isPositionWithinBounds(int x, int y) {
        return x > this.getScaledX() - HITBOX_PADDING && x < this.getScaledX() + this.getScaledWidth() + HITBOX_PADDING && y > this.getScaledY() - HITBOX_PADDING && y < this.getScaledY() + getScaledHeight() + HITBOX_PADDING;
    }
}
