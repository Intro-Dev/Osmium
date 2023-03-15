package dev.lobstershack.client.render.widget;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;

public abstract class SimpleWidget extends GuiComponent implements Renderable, GuiEventListener, NarratableEntry {

    private boolean focused;

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void setFocused(boolean focus) {
        this.focused = focus;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }


}
