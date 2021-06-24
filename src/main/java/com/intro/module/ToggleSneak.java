package com.intro.module;

import com.intro.config.BooleanOption;
import com.intro.config.OptionUtil;
import com.intro.render.Text;
import com.intro.module.event.Event;
import com.intro.module.event.EventTick;

public class ToggleSneak extends Module{

    private boolean sprinting = false;
    //private boolean sneaking = false;
    private final Text SprintingText;



    public ToggleSneak() {
        super("ToggleSneak");
        SprintingText = new Text(5, 5, "", 0xffffff);
    }

    public void OnEvent(Event event) {
        if(mc.player != null) {
            if(((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable) {
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

                }
            } else if(!SprintingText.text.equals("")) {
                SprintingText.text = "";
            }
        }
    }
}
