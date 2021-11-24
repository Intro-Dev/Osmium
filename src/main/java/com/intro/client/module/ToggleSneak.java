package com.intro.client.module;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventTick;
import com.intro.client.render.color.Colors;
import com.intro.client.render.drawables.Text;
import com.intro.client.util.ElementPosition;
import com.intro.common.config.Options;
import com.intro.common.config.options.Option;
import net.minecraft.client.Minecraft;

public class ToggleSneak {

    private boolean sprinting = false;

    public static boolean sneaking = false;

    private final Text sprintingText;

    private final Minecraft mc = Minecraft.getInstance();

    public ToggleSneak() {
        sprintingText = new Text(5, 5, "", Colors.WHITE.getColor().getInt());
        OsmiumClient.options.getElementPositionOption(Options.ToggleSprintPosition).get().loadToDrawable(sprintingText);
    }

    public void onEvent(Event event) {
        if(mc.player != null) {
            if(OsmiumClient.options.getBooleanOption(Options.ToggleSprintEnabled).get() || OsmiumClient.options.getBooleanOption(Options.ToggleSneakEnabled).get()) {
                boolean toggleSprintEnabled = OsmiumClient.options.getBooleanOption(Options.ToggleSprintEnabled).get();
                boolean toggleSneakEnabled = OsmiumClient.options.getBooleanOption(Options.ToggleSneakEnabled).get();
                if(event instanceof EventTick && event.isPre()) {
                    OsmiumClient.options.put(Options.ToggleSprintPosition, new Option<>(Options.ToggleSprintPosition, new ElementPosition(sprintingText.posX, sprintingText.posY, sprintingText.scale)));
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
                        sprintingText.visible = true;
                        sprintingText.text = "Sprinting(Toggled)";
                    } else if (mc.options.keySprint.isDown() && toggleSprintEnabled) {
                        sprintingText.visible = true;
                        sprintingText.text = "Sprinting(Key Down)";
                    } else if(sneaking && toggleSneakEnabled) {
                        sprintingText.visible = true;
                        sprintingText.text = "Sneaking(Toggled)";
                    } else if (mc.options.keyShift.isDown() && toggleSneakEnabled) {
                        sprintingText.visible = true;
                        sprintingText.text = "Sneaking(Key Down)";
                    } else {
                        sprintingText.text = "";
                        sprintingText.visible = false;
                    }
                }
            } else if(sprintingText.visible) {
                sprintingText.text = "";
                sprintingText.visible = false;
            }
        }
    }
}
