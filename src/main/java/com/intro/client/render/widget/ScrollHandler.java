package com.intro.client.render.widget;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;

/**
 * <p>Handles scroll offsets for scrollable tooltips</p>
 */
public class ScrollHandler implements GuiEventListener, NarratableEntry{

    private double scrollOffset = 0D;

    public double getScrollOffset() {
        return scrollOffset;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        System.out.println("scrolling");
        scrollOffset += scrollDelta * 0.1D;
        return GuiEventListener.super.mouseScrolled(mouseX, mouseY, scrollDelta);
    }

    // just return true so mouseScrolled events always get past to the class
    @Override
    public boolean isMouseOver(double d, double e) {
        System.out.println("is over");
        return true;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }
}
