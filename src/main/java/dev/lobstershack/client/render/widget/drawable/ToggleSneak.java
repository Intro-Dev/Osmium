package dev.lobstershack.client.render.widget.drawable;

import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.render.color.Color;
import dev.lobstershack.client.render.color.Colors;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ToggleSneak extends Scalable {

    private final Minecraft mc = Minecraft.getInstance();

    private boolean sprinting = false;
    private boolean sneaking = false;
    private boolean visible = true;

    private final int BG_COLOR = new Color(0.1f, 0.1f, 0.1f, 0.2f).getInt();

    private String text = "";

    private static ToggleSneak INSTANCE;

    public static ToggleSneak getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ToggleSneak();
        }
        return INSTANCE;
    }

    public ToggleSneak() {
        super(Options.ToggleSprintPosition, 0, 0, Component.empty());
    }

    @Override
    public void render(GuiGraphics graphics) {
        if (visible) {
            this.setWidth(Minecraft.getInstance().font.width("Sprinting(Key Down)"));
            this.height = Minecraft.getInstance().font.lineHeight * 2;
            graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + height, BG_COLOR);
            graphics.drawCenteredString(Minecraft.getInstance().font, Component.literal(this.text), this.getX() + this.width / 2, this.getY() + this.height / 4, Colors.WHITE.getColor().getInt());
        }
    }

    public void registerEventListeners() {
        ClientTickEvents.START_WORLD_TICK.register((client -> {
            if (Options.ToggleSprintEnabled.get() || Options.ToggleSneakEnabled.get()) {
                boolean toggleSprintEnabled = Options.ToggleSprintEnabled.get();
                boolean toggleSneakEnabled = Options.ToggleSneakEnabled.get();

                if (mc.player.zza > 0 && !mc.player.isUsingItem() && !mc.player.isShiftKeyDown() && !mc.player.horizontalCollision && this.sprinting)
                    mc.player.setSprinting(true);

                // why is sneak called keyShift?
                // the world may never know
                if (mc.options.keyShift.consumeClick() && toggleSneakEnabled) {
                    sneaking = !sneaking;
                }
                if (mc.options.keySprint.consumeClick() && toggleSprintEnabled) {
                    this.sprinting = !this.sprinting;
                }

                if ((this.sprinting && toggleSprintEnabled) && (!sneaking)) {
                    visible = true;
                    text = "Sprinting(Toggled)";
                } else if (mc.options.keySprint.isDown() && toggleSprintEnabled) {
                    visible = true;
                    text = "Sprinting(Key Down)";
                } else if (sneaking && toggleSneakEnabled) {
                    visible = true;
                    text = "Sneaking(Toggled)";
                } else if (mc.options.keyShift.isDown() && toggleSneakEnabled) {
                    visible = true;
                    text = "Sneaking(Key Down)";
                } else {
                    visible = false;
                    text = "";
                }
            } else if (visible) {
                text = "";
                visible = false;
            }
        }));
    }

    public boolean shouldSneak() {
        return sneaking;
    }

    public static void invalidateInstance() {
        INSTANCE = null;
    }
}
