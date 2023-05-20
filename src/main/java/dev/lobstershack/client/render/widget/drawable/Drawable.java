package dev.lobstershack.client.render.widget.drawable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

/**
 * <p>Generic class for gui drawables</p>
 *
 * @see DrawableRenderer
 * @author Intro
 * @since 1.1.1
 */
public abstract class Drawable extends AbstractWidget implements Renderable, GuiEventListener, NarratableEntry {

    public static final int HITBOX_PADDING = 40;
    public boolean visible = true;

    private boolean focused = false;

    public Drawable(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    public abstract void render(GuiGraphics graphics);

    @Override
    public final void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.render(graphics);
    }

    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }


    public boolean isPositionWithinBounds(int x, int y) {
        return x > this.getX() - HITBOX_PADDING && x < this.getX() + this.width + HITBOX_PADDING && y > this.getY() - HITBOX_PADDING && y < this.getY() + height + HITBOX_PADDING;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void setFocused(boolean focus) {
        focused = focus;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }
}
