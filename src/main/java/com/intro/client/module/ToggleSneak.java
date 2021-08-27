package com.intro.client.module;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventTick;
import com.intro.client.render.Colors;
import com.intro.client.render.drawables.Text;
import com.intro.common.config.Options;
import com.intro.common.config.options.ElementPositionOption;
import net.minecraft.client.Minecraft;

public class ToggleSneak {

    private boolean sprinting = false;

    public static boolean sneaking = false;

    private final Text SprintingText;

    private final Minecraft mc = Minecraft.getInstance();

    public ToggleSneak() {
        SprintingText = new Text(5, 5, "", Colors.WHITE.getColor().getInt());
        OsmiumClient.options.getElementPositionOption(Options.ToggleSprintPosition).elementPosition.loadToDrawable(SprintingText);
    }

    public void onEvent(Event event) {
        if(mc.player != null) {
            if(OsmiumClient.options.getBooleanOption(Options.ToggleSprintEnabled).variable || OsmiumClient.options.getBooleanOption(Options.ToggleSneakEnabled).variable) {
                boolean toggleSprintEnabled = OsmiumClient.options.getBooleanOption(Options.ToggleSprintEnabled).variable;
                boolean toggleSneakEnabled = OsmiumClient.options.getBooleanOption(Options.ToggleSneakEnabled).variable;
                if(event instanceof EventTick && event.isPre()) {
                    OsmiumClient.options.put(Options.ToggleSprintPosition, new ElementPositionOption(Options.ToggleSprintPosition, SprintingText.posX, SprintingText.posY));
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
                        SprintingText.visible = true;
                        SprintingText.text = "Sprinting(Toggled)";
                    } else if (mc.options.keySprint.isDown() && toggleSprintEnabled) {
                        SprintingText.visible = true;
                        SprintingText.text = "Sprinting(Key Down)";
                    } else if(sneaking && toggleSneakEnabled) {
                        SprintingText.visible = true;
                        SprintingText.text = "Sneaking(Toggled)";
                    } else if (mc.options.keyShift.isDown() && toggleSneakEnabled) {
                        SprintingText.visible = true;
                        SprintingText.text = "Sneaking(Key Down)";
                    } else {
                        SprintingText.visible = false;
                    }

                }
            } else if(SprintingText.visible) {
                SprintingText.visible = false;
            }
        }
    }
}
