package dev.lobstershack.client.render.widget;

import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.render.color.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class ProgressBarWidget extends SimpleWidget {

    public final int x, y, width;

    public boolean visible = true;

    private final AtomicDouble progress = new AtomicDouble(0d);

    public ProgressBarWidget(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void setProgress(double d) {
        d = Mth.clamp(d, 0, 1d);
        this.progress.set(d);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if(visible) {
            // fill bar
            int height = 15;
            fill(poseStack, this.x - 2, this.y - 2, this.x + width + 2, this.y + height + 2, Colors.BLACK.getColor().getInt());
            fill(poseStack, this.x, this.y, (int) (this.x + (width * progress.get())), this.y + height, Colors.GREEN.getColor().getInt());
            drawCenteredString(poseStack, Minecraft.getInstance().font, Math.round(progress.get() * 100) + "%", this.x + (width / 2), this.y + (height / 4), Colors.WHITE.getColor().getInt());
        }
    }

}
