package com.intro.client.render.drawables;

import com.intro.client.OsmiumClient;
import com.intro.client.render.color.Color;
import com.intro.client.render.color.Colors;
import com.intro.client.render.texture.DynamicAnimation;
import com.intro.common.config.Options;
import com.intro.common.config.options.ElementPositionOption;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

public class DebugDisplay extends Scalable {

    private static DebugDisplay INSTANCE;

    private boolean firstRun = true;

    private DynamicAnimation animation;

    private final int BG_COLOR = new Color(0.1f, 0.1f, 0.1f, 0.2f).getInt();

    private final Minecraft mc = Minecraft.getInstance();

    protected DebugDisplay(int color) {
    }

    @Override
    public void render(PoseStack stack) {

    }

    @Override
    public void destroySelf() {

    }

    @Override
    public void onPositionChange(int newX, int newY, int oldX, int oldY) {
        OsmiumClient.options.put(Options.FpsDisplayPosition, new ElementPositionOption(Options.FpsDisplayPosition, newX, newY, this.scale));
    }

    public static DebugDisplay getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DebugDisplay(Colors.WHITE.getColor().getInt());
        }
        return INSTANCE;
    }

    @Override
    public void onScaleChange(double oldScale, double newScale) {
        OsmiumClient.options.put(Options.FpsDisplayPosition, new ElementPositionOption(Options.FpsDisplayPosition, this.posX, this.posY, newScale));
    }
}
