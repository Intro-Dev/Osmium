package com.intro.render.screen;

import com.intro.render.RenderManager;
import com.intro.render.drawables.Drawable;
import com.intro.util.OptionUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class OsmiumGuiEditScreen extends Screen{

    private final Screen parent;
    private final MinecraftClient mc = MinecraftClient.getInstance();


    public OsmiumGuiEditScreen(Screen parent) {
        super(new TranslatableText("osmium.guiedit.title"));
        this.parent = parent;
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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
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

    /**
     * <p>remember to return super.mouseDragged(), or gui editing will break!</p>
     */
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for(Drawable drawable : RenderManager.drawables) {
            if(drawable.isPositionWithinBounds((int) mouseX, (int) mouseY)) {
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
