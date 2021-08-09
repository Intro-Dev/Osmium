package com.intro.render.drawables;

import com.intro.render.RenderManager;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;

/**
 * <p>Generic class for gui drawables</p>
 *
 * @see RenderManager
 * @see Text
 * @author Intro
 * @since 1.1.1
 */
public abstract class Drawable extends DrawableHelper implements Element, net.minecraft.client.gui.Drawable, Selectable {
    
    public int posX = 0, posY = 0;
    public int width = 0, height = 0;

    public static final int HITBOX_PADDING = 20;

    public boolean visible = true;
    
    public abstract void render(MatrixStack stack);

    public abstract void destroySelf();


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.render(matrices);
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    public boolean isPositionWithinBounds(int x, int y) {
        return x > this.posX - HITBOX_PADDING && x < this.posX + this.width + HITBOX_PADDING && y > this.posY - HITBOX_PADDING && y < this.posY + height + HITBOX_PADDING;
    }


}
