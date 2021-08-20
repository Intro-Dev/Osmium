package com.intro.client.render.screen;

import com.intro.client.render.RenderManager;
import com.intro.client.render.drawables.Drawable;
import com.intro.client.util.OptionUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class OsmiumGuiEditScreen extends Screen {

    public OsmiumGuiEditScreen() {
        super(new TranslatableComponent("osmium.gui_edit.title"));
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClose() {
        OptionUtil.save();
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
        for(Drawable drawable : RenderManager.drawables) {
            if(drawable.isPositionWithinBounds((int) mouseX, (int) mouseY) && drawable.visible) {
                if(mouseX + drawable.width < this.width || mouseX - drawable.width < 0) {
                    drawable.posX = (int) mouseX;
                }
                if(mouseY + drawable.height < this.height || mouseY - drawable.height < 0) {
                    drawable.posY = (int) mouseY;
                }
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
}
