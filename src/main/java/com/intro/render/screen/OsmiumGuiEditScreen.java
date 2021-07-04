package com.intro.render.screen;

import com.intro.config.OptionUtil;
import com.intro.render.RenderManager;
import com.intro.render.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class OsmiumGuiEditScreen extends Screen{

    private Screen parent;
    private MinecraftClient mc = MinecraftClient.getInstance();





    public OsmiumGuiEditScreen(Screen parent) {
        super(new TranslatableText("osmium.guiedit.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        for (Text t : RenderManager.textArrayList) {
            if(t.guiElement) {
                addDrawableChild(t);
            }
        }
    }

    @Override
    public void onClose() {
        OptionUtil.save();
        super.onClose();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        for(Element e : this.children()) {
            if (e instanceof Text) {
                if(!((Text) e).visible) {
                    Text text = (Text) e;
                    mc.textRenderer.drawWithShadow(matrices, new LiteralText(text.text), text.posX, text.posY, text.color);
                }
            }
        }
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
            for(Element e : this.children()) {
                if(e instanceof Text) {
                    if(((Text) e).isPositionWithinBounds((int) mouseX, (int) mouseY)) {
                        e.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
                    }
                }
            }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
}
