package dev.lobstershack.client.render.widget.drawables;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.render.color.Color;
import dev.lobstershack.client.render.color.Colors;
import dev.lobstershack.client.util.ExecutionUtil;
import dev.lobstershack.common.config.Options;
import net.minecraft.client.Minecraft;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CpsDisplay extends Scalable {

    private static CpsDisplay INSTANCE;

    private final AtomicInteger cps = new AtomicInteger(0);

    private boolean firstRun = true;

    private final int color;

    private final int BG_COLOR = new Color(0.1f, 0.1f, 0.1f, 0.2f).getInt();

    private final Minecraft mc = Minecraft.getInstance();


    public void onClick() {
        cps.incrementAndGet();
        ExecutionUtil.submitScheduledTask(new RemoveClicksTask(), 1, TimeUnit.SECONDS);
    }

    protected CpsDisplay(int color) {
        super(Options.CpsDisplayPosition);
        this.color = color;
    }

    @Override
    public void render(PoseStack stack) {
        if(Options.CpsDisplayEnabled.get()) {
            this.visible = true;
            if(firstRun) {
                this.width = 40;
                this.height = mc.font.lineHeight * 2;
                firstRun = false;
            }
            fill(stack, posX, posY, posX + width, posY + height, BG_COLOR);
            drawCenteredString(stack, mc.font, cps + " cps", posX + (width / 2), posY + (height / 4), color);
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

    private class RemoveClicksTask extends TimerTask {

        @Override
        public void run() {
            cps.decrementAndGet();
        }
    }
}
