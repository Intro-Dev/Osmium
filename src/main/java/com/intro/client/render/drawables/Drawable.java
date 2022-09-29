package com.intro.client.render.drawables;

import com.intro.client.render.RenderManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;

/**
 * <p>Generic class for gui drawables</p>
 *
 * @see RenderManager
 * @see RenderableText
 * @author Intro
 * @since 1.1.1
 */
public abstract class Drawable extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {
    
    public int posX = 0, posY = 0;
    public int width = 0, height = 0;

    public static final int HITBOX_PADDING = 40;
    public boolean visible = true;
    
    public abstract void render(PoseStack stack);

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.render(matrices);
    }

    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput builder) {

    }

    public boolean isPositionWithinBounds(int x, int y) {
        return x > this.posX - HITBOX_PADDING && x < this.posX + this.width + HITBOX_PADDING && y > this.posY - HITBOX_PADDING && y < this.posY + height + HITBOX_PADDING;
    }




}
