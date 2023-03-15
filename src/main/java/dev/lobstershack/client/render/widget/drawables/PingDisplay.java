package dev.lobstershack.client.render.widget.drawables;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.event.Event;
import dev.lobstershack.client.event.EventTick;
import dev.lobstershack.client.render.color.Color;
import dev.lobstershack.client.render.color.Colors;
import dev.lobstershack.common.config.Options;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

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
        super(Options.PingDisplayPosition);
        this.color = color;
    }

    @Override
    public void render(PoseStack stack) {
        if(Options.PingDisplayEnabled.get()) {
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

    public static PingDisplay getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PingDisplay(Colors.WHITE.getColor().getInt());
        }
        return INSTANCE;
    }

}
