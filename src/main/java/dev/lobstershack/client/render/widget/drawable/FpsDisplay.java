package dev.lobstershack.client.render.widget.drawable;

import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.mixin.client.MinecraftAccessor;
import dev.lobstershack.client.render.color.Color;
import dev.lobstershack.client.render.color.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class FpsDisplay extends Scalable {

    private static FpsDisplay INSTANCE;

    private final int color;

    private final int BG_COLOR = new Color(0.1f, 0.1f, 0.1f, 0.2f).getInt();

    private final Minecraft mc = Minecraft.getInstance();

    protected FpsDisplay(int color) {
        super(Options.FpsDisplayPosition, 60, 0, Component.empty());
        this.color = color;
    }

    @Override
    public void render(GuiGraphics graphics) {
        if(Options.FpsEnabled.get()) {
            this.visible = true;
            this.height = Minecraft.getInstance().font.lineHeight * 2;
            graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + height, BG_COLOR);
            graphics.drawCenteredString(mc.font, MinecraftAccessor.getFps() + " fps", this.getX() + (width / 2), this.getY() + (height / 4), color);
        } else {
            this.visible = false;
        }
    }


    public static FpsDisplay getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new FpsDisplay(Colors.WHITE.getColor().getInt());
        }
        return INSTANCE;
    }

    public static void invalidateInstance() {
        INSTANCE = null;
    }

}
