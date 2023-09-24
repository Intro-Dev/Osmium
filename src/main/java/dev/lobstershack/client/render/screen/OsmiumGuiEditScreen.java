package dev.lobstershack.client.render.screen;

import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.render.color.Colors;
import dev.lobstershack.client.render.widget.AbstractScalableButton;
import dev.lobstershack.client.render.widget.drawable.Drawable;
import dev.lobstershack.client.render.widget.drawable.DrawableRenderer;
import dev.lobstershack.client.render.widget.drawable.Scalable;
import dev.lobstershack.client.util.DebugUtil;
import dev.lobstershack.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        DrawableRenderer.renderHud(graphics);
        // make drawable click boxes visible when debug mode is active
        if(DebugUtil.isDebug()) {
            for(Drawable drawable : DrawableRenderer.drawables) {
                if(drawable.visible) {
                    int width = drawable.getWidth();
                    int height = drawable.getHeight();
                    int x = drawable.getX();
                    int y = drawable.getY();
                    graphics.pose().pushPose();
                    if(drawable instanceof Scalable scalable) RenderUtil.positionAccurateScale(graphics.pose(), (float) scalable.getScale(), x, y, width, height);
                    graphics.hLine(x, x + width, y, Colors.WHITE.getColor().getInt());
                    graphics.hLine(x, x + width, y + height, Colors.WHITE.getColor().getInt());

                    graphics.vLine(x, y, y + height, Colors.WHITE.getColor().getInt());
                    graphics.vLine(x + width, y, y + height, Colors.WHITE.getColor().getInt());
                    graphics.pose().popPose();
                }
            }
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        // love this
        // top tier code
        for(Drawable drawable : DrawableRenderer.drawables) {
            if(drawable.isPositionWithinBounds((int) mouseX, (int) mouseY) && drawable.visible) {
                if(drawable instanceof Scalable scalable) {
                    if(mouseX + scalable.getScaledWidth() < this.width || mouseX - scalable.getScaledHeight() < 0) {
                        scalable.setScaledX((int) mouseX);
                    }
                    if(mouseY + scalable.getScaledHeight() < this.height || mouseY - scalable.getScaledHeight() < 0) {
                        scalable.setScaledY((int) mouseY);
                    }
                } else {
                    if(mouseX + drawable.getWidth() < this.width || mouseX - drawable.getWidth() < 0) {
                        drawable.setX((int) mouseX);
                    }
                    if(mouseY + drawable.getHeight() < this.height || mouseY - drawable.getHeight() < 0) {
                        drawable.setY((int) mouseY);
                    }
                }
                return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta, double ignored) {
        for(Drawable drawable : DrawableRenderer.drawables) {
            if(drawable instanceof Scalable scalable) {
                if(scalable.isPositionWithinBounds((int) mouseX, (int) mouseY) && drawable.visible) {
                    scalable.setScale(scalable.getScale() + scrollDelta * 0.1);
                    scalable.setScale(Mth.clamp(scalable.getScale(), 0.5, 10));
                    return super.mouseScrolled(mouseX, mouseY, scrollDelta, ignored);
                }
            }
        }

        return super.mouseScrolled(mouseX, mouseY, scrollDelta, ignored);
    }
}
