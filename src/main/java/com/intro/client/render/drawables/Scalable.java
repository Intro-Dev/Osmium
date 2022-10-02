package com.intro.client.render.drawables;

import com.intro.client.util.ElementPosition;
import com.intro.client.util.RenderUtil;
import com.intro.common.config.options.Option;
import com.mojang.blaze3d.vertex.PoseStack;

/**
 * Indicates a drawable can be scaled
 */
public abstract class Scalable extends Drawable {

    public Scalable(Option<ElementPosition> boundOption) {
        this.boundOption = boundOption;
        this.posX = boundOption.get().x;
        this.posY = boundOption.get().y;
        boundOption.addChangeListener((newPos) -> {
            this.posX = newPos.x;
            this.posY = newPos.y;
        });
    }

    public Option<ElementPosition> boundOption;

    public int getScaledWidth() {
        return (int) (this.width * boundOption.get().scale);
    }

    public int getScaledHeight() {
        return (int) (this.height * boundOption.get().scale);
    }

    /**
     * <p>Gets the scale accounted x value</p>
     * <p>Only to be used to gui editing</p>
     */
    // position accurate scaling
    // very fun
    // this algorithm was not fun to make
    // probably online somewhere, but I can't find it, so I had to make it myself
    // x: scaledX, y: x, z: scaledHeight, a: scale
    // x = y + (z / (2 * a)) - z / 2;
    public int getScaledX() {
        return (int) (posX + (this.getScaledWidth() / (2 * boundOption.get().scale)) - this.getScaledWidth() / 2);
    }

    /**
     * <p>Gets the scale accounted y value</p>
     * <p>Only to be used to gui editing</p>
     */
    public int getScaledY() {
        return (int) (posY + (this.getScaledHeight() / (2 * boundOption.get().scale)) - this.getScaledHeight() / 2);
    }

    // x: scaledX, y: x, z: scaledWidth, a: scale
    // -y = -x + (z / (2 * a)) - z / 2;
    // y = x + -(z / (2 * a)) + (z / 2)

    public void setScaledX(int x) {
        this.posX = (int) -((-x + this.getScaledWidth() / (2 * boundOption.get().scale)) - this.getScaledWidth() / 2);
        boundOption.get().x = posX;
    }

    public void setScaledY(int y) {
        this.posY = (int) -((-y + this.getScaledHeight() / (2 * boundOption.get().scale)) - this.getScaledHeight() / 2);
        boundOption.get().y = posY;
    }

    public void setScale(double scale) {
        boundOption.get().scale = scale;
    }

    public double getScale() { return boundOption.get().scale; }

    // transformation matrices are fun
    // right?
    public void scaleWithPositionIntact(PoseStack stack) {
        RenderUtil.positionAccurateScale(stack, (float) boundOption.get().scale, posX, posY, width, height);
    }

    @Override
    public boolean isPositionWithinBounds(int x, int y) {
        return x > this.getScaledX() - HITBOX_PADDING && x < this.getScaledX() + this.getScaledWidth() + HITBOX_PADDING && y > this.getScaledY() - HITBOX_PADDING && y < this.getScaledY() + getScaledHeight() + HITBOX_PADDING;
    }
}
