package com.intro.client.render.drawables;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventTick;
import com.intro.client.render.color.Color;
import com.intro.client.render.color.Colors;
import com.intro.common.config.Options;
import com.intro.common.config.options.ElementPositionOption;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

import java.util.Objects.requireNonNull;

public class PingDisplay extends Scalable {

    private static PingDisplay INSTANCE;

    private int currentPing;

    private boolean firstRun = true;

    private final int color;

    private final int BG_COLOR = new Color(0.1f, 0.1f, 0.1f, 0.2f).getInt();

    private final Minecraft mc = Minecraft.getInstance();

    public void onEvent(Event event) {
        if(event instanceof EventTick && mc.player != null) {
            if((mc.getCurrentServer() != null)) {
                ClientPacketListener clientPacketListener = mc.player.connection;
                try {
                    currentPing = clientPacketListener.getPlayerInfo(mc.player.getUUID()).getLatency();
                } catch(NullPointerException ignored) {
                    currentPing = 0;
                }

            } else {
                currentPing = 0;
            }
        }
    }

    protected PingDisplay(int color) {
        OsmiumClient.options.getElementPositionOption(Options.PingDisplayPosition).elementPosition.loadToScalable(this);
        this.color = color;
    }

    @Override
    public void render(PoseStack stack) {
        if(OsmiumClient.options.getBooleanOption(Options.PingDisplayEnabled).variable) {
            this.visible = true;
            if(firstRun) {
                this.width = 40;
                this.height = mc.font.lineHeight * 2;
                firstRun = false;
            }
            fill(stack, posX, posY, posX + width, posY + height, BG_COLOR);
            drawCenteredString(stack, mc.font, currentPing + " ms", posX + (width / 2), posY + (height / 4), color);
        } else {
            this.visible = false;
        }
    }

    @Override
    public void destroySelf() {

    }

    @Override
    public void onPositionChange(int newX, int newY, int oldX, int oldY) {
        OsmiumClient.options.put(Options.PingDisplayPosition, new ElementPositionOption(Options.PingDisplayPosition, newX, newY, this.scale));

    }

    public static PingDisplay getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PingDisplay(Colors.WHITE.getColor().getInt());
        }
        return INSTANCE;
    }

    @Override
    public void onScaleChange(double oldScale, double newScale) {
        OsmiumClient.options.put(Options.PingDisplayPosition, new ElementPositionOption(Options.PingDisplayPosition, this.posX, this.posY, newScale));
    }
}
