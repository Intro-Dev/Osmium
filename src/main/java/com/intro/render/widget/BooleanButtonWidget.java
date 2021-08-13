package com.intro.render.widget;

import com.intro.Osmium;
import com.intro.config.options.BooleanOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class BooleanButtonWidget extends Button {

    public BooleanOption attachedOption;
    public final String key;

    public BooleanButtonWidget(int x, int y, int width, int height, BooleanOption attachedOption, String key) {
        super(x, y, width, height, new TextComponent(""), button -> {
            ((BooleanOption) Osmium.options.get(attachedOption.identifier)).variable = !((BooleanOption) Osmium.options.get(attachedOption.identifier)).variable;
            if(((BooleanOption) Osmium.options.get(attachedOption.identifier)).variable) {
                button.setMessage(new TranslatableComponent(key + "enabled"));
            } else {
                button.setMessage(new TranslatableComponent(key + "disabled"));
            }
        });
        this.attachedOption = attachedOption;
        this.key = key;
        if(((BooleanOption) Osmium.options.get(attachedOption.identifier)).variable) {
            super.setMessage(new TranslatableComponent(key + "enabled"));
        } else {
            super.setMessage(new TranslatableComponent(key + "disabled"));
        }
    }


}
