package dev.lobstershack.client.render.widget;

import com.google.common.util.concurrent.AtomicDouble;
import dev.lobstershack.client.render.color.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ProgressBarWidget extends SimpleWidget {


    public boolean visible = true;

    private final AtomicDouble progress = new AtomicDouble(0d);

    public ProgressBarWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    public void setProgress(double d) {
        d = Mth.clamp(d, 0, 1d);
        this.progress.set(d);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if(visible) {
            // fill bar
            int height = 15;
            graphics.fill(this.getX() - 2, this.getY() - 2, this.getX() + width + 2, this.getY() + height + 2, Colors.BLACK.getColor().getInt());
            graphics.fill(this.getX(), this.getY(), (int) (this.getX() + (width * progress.get())), this.getY() + height, Colors.GREEN.getColor().getInt());
            graphics.drawCenteredString(Minecraft.getInstance().font, Math.round(progress.get() * 100) + "%", this.getX() + (width / 2), this.getY() + (height / 4), Colors.WHITE.getColor().getInt());
        }
    }

}
