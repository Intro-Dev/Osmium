package com.intro.client.render.drawables;

import com.intro.client.OsmiumClient;
import com.intro.client.render.Color;
import com.intro.client.render.Colors;
import com.intro.common.config.Options;
import com.intro.common.config.options.ElementPositionOption;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

import java.util.Timer;
import java.util.TimerTask;

public class CpsDisplay extends Scalable {

    private static CpsDisplay INSTANCE;

    private int cps;

    private boolean firstRun = true;

    private final int color;

    private final int BG_COLOR = new Color(0.1f, 0.1f, 0.1f, 0.2f).getInt();

    private final Minecraft mc = Minecraft.getInstance();

    public void onClick() {
        cps++;
        new RemoveClicksTask().schedule(1000);
    }

    protected CpsDisplay(int color) {
        OsmiumClient.options.getElementPositionOption(Options.CpsDisplayPosition).elementPosition.loadToScalable(this);
        this.color = color;
    }

    @Override
    public void render(PoseStack stack) {
        if(OsmiumClient.options.getBooleanOption(Options.CpsDisplayEnabled).variable) {
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

    @Override
    public void destroySelf() {

    }

    @Override
    public void onPositionChange(int newX, int newY, int oldX, int oldY) {
        OsmiumClient.options.put(Options.CpsDisplayPosition, new ElementPositionOption(Options.CpsDisplayPosition, newX, newY, this.scale));
    }

    public static CpsDisplay getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new CpsDisplay(Colors.WHITE.getColor().getInt());
        }
        return INSTANCE;
    }

    @Override
    public void onScaleChange(float oldScale, float newScale) {
        OsmiumClient.options.put(Options.CpsDisplayPosition, new ElementPositionOption(Options.CpsDisplayPosition, this.posX, this.posY, newScale));
    }

    private class RemoveClicksTask extends TimerTask {

        private final Timer timer = new Timer();

        public void schedule(long delay) {
            timer.schedule(this, delay);
        }

        @Override
        public void run() {
            cps--;
            timer.cancel();
        }
    }
}
