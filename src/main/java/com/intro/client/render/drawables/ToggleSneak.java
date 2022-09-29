package com.intro.client.render.drawables;

import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventTick;
import com.intro.client.render.color.Colors;
import com.intro.common.config.Options;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class ToggleSneak extends Scalable {

    private final Minecraft mc = Minecraft.getInstance();

    private boolean sprinting = false;
    private boolean sneaking = false;
    private boolean visible = true;

    private String text = "";

    private static ToggleSneak INSTANCE;

    public static ToggleSneak getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ToggleSneak();
        }
        return INSTANCE;
    }

    public ToggleSneak() {
        super(Options.ToggleSprintPosition);
    }

    @Override
    public void render(PoseStack stack) {
        if(visible) {
            this.width = Minecraft.getInstance().font.width(this.text);
            this.height = Minecraft.getInstance().font.lineHeight;
            Font renderer = Minecraft.getInstance().font;
            renderer.drawShadow(stack, Component.literal(this.text), this.posX, this.posY, Colors.WHITE.getColor().getInt());
        }
    }

    public void onEvent(Event event) {
        if(mc.player != null) {
            if(Options.ToggleSprintEnabled.get() ||Options.ToggleSneakEnabled.get()) {
                boolean toggleSprintEnabled = Options.ToggleSprintEnabled.get();
                boolean toggleSneakEnabled = Options.ToggleSneakEnabled.get();
                if(event instanceof EventTick && event.isPre()) {
                    if(mc.player.zza > 0 && !mc.player.isUsingItem() && !mc.player.isShiftKeyDown() && !mc.player.horizontalCollision && this.sprinting)
                        mc.player.setSprinting(true);

                    // why is sneak called keyShift?
                    // the world may never know
                    if(mc.options.keyShift.consumeClick() && toggleSneakEnabled) {
                        sneaking = !sneaking;
                    }
                    if(mc.options.keySprint.consumeClick() && toggleSprintEnabled) {
                        this.sprinting = !this.sprinting;
                    }

                    if((this.sprinting && toggleSprintEnabled) && (!sneaking)) {
                        visible = true;
                        text = "Sprinting(Toggled)";
                    } else if (mc.options.keySprint.isDown() && toggleSprintEnabled) {
                        visible = true;
                        text = "Sprinting(Key Down)";
                    } else if(sneaking && toggleSneakEnabled) {
                        visible = true;
                        text = "Sneaking(Toggled)";
                    } else if (mc.options.keyShift.isDown() && toggleSneakEnabled) {
                        visible = true;
                        text = "Sneaking(Key Down)";
                    } else {
                        visible = false;
                        text = "";
                    }
                }
            } else if(visible) {
                text = "";
                visible = false;
            }
        }
    }

    public boolean shouldSneak() {
        return sneaking;
    }
}
