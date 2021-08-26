package com.intro.client.render.drawables;

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

    public int getScaledX() {
        return (int) (this.posX * scale);
    }

    public int getScaledY() {
        return (int) (this.posY * scale);
    }

    // transformation matrixes are fun
    // right?
    public void scaleWithPositionIntact(PoseStack stack) {
        stack.translate((posX + (width / 2f)), (posY + (height / 2f)), 0);
        stack.scale(scale, scale, 0);
        stack.translate(-(posX + (width / 2f)), -(posY + (height / 2f)), 0);
    }

    @Override
    public boolean isPositionWithinBounds(int x, int y) {
        return x > this.posX - HITBOX_PADDING && x < this.posX + this.getScaledWidth() + HITBOX_PADDING && y > this.posY - HITBOX_PADDING && y < this.posY + getScaledHeight() + HITBOX_PADDING;
    }
}
