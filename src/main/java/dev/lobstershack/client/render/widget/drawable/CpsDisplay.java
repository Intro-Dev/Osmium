package dev.lobstershack.client.render.widget.drawable;

import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.render.color.Color;
import dev.lobstershack.client.render.color.Colors;
import dev.lobstershack.client.util.ExecutionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CpsDisplay extends Scalable {

    private static CpsDisplay INSTANCE;

    private final AtomicInteger cps = new AtomicInteger(0);

    private final int color;

    private final int BG_COLOR = new Color(0.1f, 0.1f, 0.1f, 0.2f).getInt();

    private final Minecraft mc = Minecraft.getInstance();


    public void onClick() {
        cps.incrementAndGet();
        ExecutionUtil.submitScheduledTask(new RemoveClicksTask(), 1, TimeUnit.SECONDS);
    }

    protected CpsDisplay(int color) {
        super(Options.CpsDisplayPosition, 40, 0, Component.empty());
        this.color = color;
    }

    @Override
    public void render(GuiGraphics graphics) {
        if(Options.CpsDisplayEnabled.get()) {
            this.visible = true;
            this.height = Minecraft.getInstance().font.lineHeight * 2;
            graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + height, BG_COLOR);
            graphics.drawCenteredString(mc.font, cps + " cps", this.getX() + (width / 2), this.getY() + (height / 4), color);
        } else {
            this.visible = false;
        }
    }

    public static CpsDisplay getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new CpsDisplay(Colors.WHITE.getColor().getInt());
        }
        return INSTANCE;
    }

    public static void invalidateInstance() {
        INSTANCE = null;
    }

    private class RemoveClicksTask extends TimerTask {

        @Override
        public void run() {
            cps.decrementAndGet();
        }
    }
}
