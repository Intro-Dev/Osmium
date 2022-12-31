package com.intro.client.render.screen;

import com.intro.client.render.RenderManager;
import com.intro.client.render.drawables.Drawable;
import com.intro.client.render.drawables.Scalable;
import com.intro.client.render.widget.AbstractScalableButton;
import com.intro.common.config.Options;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class OsmiumGuiEditScreen extends Screen {

    private final Screen parent;

    public OsmiumGuiEditScreen(Screen parent) {
        super(Component.translatable("osmium.gui_edit.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button backButton = new AbstractScalableButton(this.width / 2 - 75, this.height - 40, 150, 20, Component.translatable("osmium.options.video_options.back"), (Button) -> Minecraft.getInstance().setScreen(parent));
        this.addRenderableWidget(backButton);
    }

    @Override
    public void onClose() {
        Options.save();
        super.onClose();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        RenderManager.renderHud(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        // love this
        // top tier code
        for(Drawable drawable : RenderManager.drawables) {
            if(drawable.isPositionWithinBounds((int) mouseX, (int) mouseY) && drawable.visible) {
                if(drawable instanceof Scalable scalable) {
                    if(mouseX + scalable.getScaledWidth() < this.width || mouseX - scalable.getScaledHeight() < 0) {
                        scalable.setScaledX((int) mouseX);
                    }
                    if(mouseY + scalable.getScaledHeight() < this.height || mouseY - scalable.getScaledHeight() < 0) {
                        scalable.setScaledY((int) mouseY);
                    }
                } else {
                    if(mouseX + drawable.width < this.width || mouseX - drawable.width < 0) {
                        drawable.posX = (int) mouseX;
                    }
                    if(mouseY + drawable.height < this.height || mouseY - drawable.height < 0) {
                        drawable.posY = (int) mouseY;
                    }
                }
                return super.mouseDragged(mouseX, mouseX, button, deltaX, deltaY);
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        for(Drawable drawable : RenderManager.drawables) {
            if(drawable instanceof Scalable scalable) {
                if(scalable.isPositionWithinBounds((int) mouseX, (int) mouseY) && drawable.visible) {
                    scalable.setScale(scalable.getScale() + scrollDelta * 0.1);
                    scalable.setScale(Mth.clamp(scalable.getScale(), 0.5, 10));
                    return super.mouseScrolled(mouseX, mouseY, scrollDelta);
                }
            }
        }

        return super.mouseScrolled(mouseX, mouseY, scrollDelta);
    }
}
