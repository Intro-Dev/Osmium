package com.intro.client.render.drawables;

import com.intro.client.render.color.Color;
import com.intro.client.render.color.Colors;
import com.intro.common.config.Options;
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
        super(Options.FpsDisplayPosition);
        this.color = color;
    }

    @Override
    public void render(PoseStack stack) {
        if(Options.FpsEnabled.get()) {

            this.visible = true;

            if(firstRun) {
                this.width = 60;
                this.height = mc.font.lineHeight * 2;
                firstRun = false;
            }
            fill(stack, posX, posY, posX + width, posY + height, BG_COLOR);
            drawCenteredString(stack, mc.font, MinecraftAccessor.getFps() + " fps", posX + (width / 2), posY + (height / 4), color);
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

}
