package com.intro.client.render.drawables;

import com.intro.client.OsmiumClient;
import com.intro.client.render.Color;
import com.intro.client.render.Colors;
import com.intro.common.config.Options;
import com.intro.common.config.options.ElementPositionOption;
import com.intro.common.mixin.client.MinecraftAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

public class FpsDisplay extends Scalable {

    private static FpsDisplay INSTANCE;

    private boolean firstRun = true;

    private final int color;

    private final int BG_COLOR = new Color(0.1f, 0.1f, 0.1f, 0.2f).getInt();

    private final Minecraft mc = Minecraft.getInstance();

    protected FpsDisplay(int color) {
        OsmiumClient.options.getElementPositionOption(Options.FpsDisplayPosition).elementPosition.loadToScalable(this);
        this.color = color;
    }

    @Override
    public void render(PoseStack stack) {
        if(OsmiumClient.options.getBooleanOption(Options.FpsEnabled).variable) {

            this.visible = true;

            if(firstRun) {
                this.width = 60;
                this.height = mc.font.lineHeight * 2;
                firstRun = false;
            }
            fill(stack, posX, posY, posX + width, posY + height, BG_COLOR);
            drawCenteredString(stack, mc.font, ((MinecraftAccessor) Minecraft.getInstance()).getFps() + " fps", posX + (width / 2), posY + (height / 4), color);
        } else {
            this.visible = false;
        }
    }

    @Override
    public void destroySelf() {

    }

    @Override
    public void onPositionChange(int newX, int newY, int oldX, int oldY) {
        OsmiumClient.options.put(Options.FpsDisplayPosition, new ElementPositionOption(Options.FpsDisplayPosition, newX, newY, this.scale));
    }

    public static FpsDisplay getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new FpsDisplay(Colors.WHITE.getColor().getInt());
        }
        return INSTANCE;
    }

    @Override
    public void onScaleChange(float oldScale, float newScale) {
        OsmiumClient.options.put(Options.FpsDisplayPosition, new ElementPositionOption(Options.FpsDisplayPosition, this.posX, this.posY, newScale));
    }
}
