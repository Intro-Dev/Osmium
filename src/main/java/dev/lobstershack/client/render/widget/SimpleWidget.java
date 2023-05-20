package dev.lobstershack.client.render.widget;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class SimpleWidget extends AbstractWidget implements Renderable, GuiEventListener, NarratableEntry {

    private boolean focused;

    public SimpleWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void setFocused(boolean focus) {
        this.focused = focus;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    @Override
    public String toString() {
        return "SimpleWidget{type=" + this.getClass().getName() + ", x=" + this.getX() + ", y=" + this.getY() + ", width=" + this.getWidth() + ", height=" + this.getHeight() + "}";
    }
}
