package dev.lobstershack.client.render.widget.drawable;

import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.render.color.Color;
import dev.lobstershack.client.render.color.Colors;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;

public class PingDisplay extends Scalable {

    private static PingDisplay INSTANCE;

    private int currentPing;

    private final int color;

    private final int BG_COLOR = new Color(0.1f, 0.1f, 0.1f, 0.2f).getInt();

    private final Minecraft mc = Minecraft.getInstance();

    public void registerEventListeners() {
        ClientTickEvents.END_CLIENT_TICK.register((client -> {
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
        }));
    }

    protected PingDisplay(int color) {
        super(Options.PingDisplayPosition, 40, 0, Component.empty());
        this.color = color;
    }

    @Override
    public void render(GuiGraphics graphics) {
        if(Options.PingDisplayEnabled.get()) {
            this.visible = true;
            this.height = Minecraft.getInstance().font.lineHeight * 2;
            graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + height, BG_COLOR);
            graphics.drawCenteredString(mc.font, currentPing + " ms", this.getX() + (width / 2), this.getY() + (height / 4), color);
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

    public static void invalidateInstance() {
        INSTANCE = null;
    }

}
