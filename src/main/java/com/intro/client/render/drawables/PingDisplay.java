package com.intro.client.render.drawables;

import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventTick;
import com.intro.client.render.Color;
import com.intro.client.render.Colors;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

import java.util.Objects;

public class PingDisplay extends Scalable {

    private static PingDisplay INSTANCE;

    private int currentPing;

    private boolean firstRun = true;

    private int color;

    private final int BG_COLOR = new Color(0.1f, 0.1f, 0.1f, 0.2f).getInt();

    private final Minecraft mc = Minecraft.getInstance();

    public void onEvent(Event event) {
        if(event instanceof EventTick && mc.player != null) {
            ClientPacketListener clientPacketListener = mc.player.connection;
            currentPing = Objects.requireNonNull(clientPacketListener.getPlayerInfo(mc.player.getUUID())).getLatency();
        }
    }

    protected PingDisplay(int x, int y, int color) {
        this.posX = x;
        this.posY = y;
        this.color = color;
    }

    @Override
    public void render(PoseStack stack) {
        if(firstRun) {
            this.width = 40;
            this.height = mc.font.lineHeight * 2;
            firstRun = false;
        }
        fill(stack, posX, posY, posX + width, posY + height, BG_COLOR);
        drawCenteredString(stack, mc.font, currentPing + " ms", posX + (width / 2), posY + (height / 4), color);
    }

    @Override
    public void destroySelf() {

    }

    public static PingDisplay getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PingDisplay(0, 0, Colors.WHITE.getColor().getInt());
        }
        return INSTANCE;
    }
}
