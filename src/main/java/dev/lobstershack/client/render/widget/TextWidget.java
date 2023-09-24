package dev.lobstershack.client.render.widget;

import dev.lobstershack.client.render.color.Color;
import dev.lobstershack.client.render.color.Colors;
import dev.lobstershack.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class TextWidget extends SimpleWidget {

    private final boolean centered;
    private final Color color;

    public TextWidget(int x, int y, int width, int height, Component message, boolean centered) {
        super(x, y, width, height, message);
        this.centered = centered;
        this.color = Colors.WHITE.getColor();
    }

    public TextWidget(int x, int y, int width, int height, Component message, boolean centered, Color color) {
        super(x, y, width, height, message);
        this.centered = centered;
        this.color = color;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        if(centered) {
            RenderUtil.renderCenteredScaledText(guiGraphics, Minecraft.getInstance().font, guiGraphics.bufferSource(), this.getMessage().getString(), i, j, color.getInt(), 1f);
        }
    }
}
