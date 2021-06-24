package com.intro.module;

import com.intro.OsmiumOptions;
import com.intro.render.Text;
import com.intro.module.event.Event;
import com.intro.module.event.EventTick;
import net.minecraft.client.MinecraftClient;

public class ToggleSneak extends Module{

    private boolean sprinting = false;
    //private boolean sneaking = false;
    private final Text SprintingText;



    public ToggleSneak() {
        super("ToggleSneak");
        SprintingText = new Text(100, 100, "", 0xffffff);
    }

    public void OnEvent(Event event) {
        if(mc.player != null) {
            if(OsmiumOptions.ToggleSprintEnabled) {
                if(event instanceof EventTick) {
                    if(mc.player.forwardSpeed > 0 && !mc.player.isUsingItem() && !mc.player.isSneaking() && !mc.player.horizontalCollision && this.sprinting)
                        mc.player.setSprinting(true);
                    //if(this.sneaking)
                    //    mc.player.setSneaking(true);
                    if(mc.options.keySprint.wasPressed()) {
                        this.sprinting = !this.sprinting;
                    }
                    //if(mc.options.keySneak.wasPressed()) {
                    //    this.sneaking = !this.sneaking;
                    //}
                    if(this.sprinting) {
                        SprintingText.text = "Sprinting(Toggled)";
                    } else if (mc.options.keySprint.isPressed()) {
                        SprintingText.text = "Sprinting(Key Down)";
                        //} else if (this.sneaking) {
                        //    SprintingText.text = "Sneaking(Toggled)";
                        //} else if (mc.options.keySneak.isPressed()) {
                        //    SprintingText.text = "Sneaking(Key Down)";
                    } else {
                        SprintingText.text = "";
                    }
                    if(SprintingText.posY != OsmiumOptions.tsY || SprintingText.posX != OsmiumOptions.tsX) {
                        SprintingText.posY = OsmiumOptions.tsY;
                        SprintingText.posX = OsmiumOptions.tsX;
                    }
                }
            } else if(!SprintingText.text.equals("")) {
                SprintingText.text = "";
            }
        }
    }
}
