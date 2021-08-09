package com.intro.render.widget;

import com.intro.Osmium;
import com.intro.config.options.BooleanOption;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class BooleanButtonWidget extends ButtonWidget {

    public BooleanOption attachedOption;
    public final String key;

    public BooleanButtonWidget(int x, int y, int width, int height, BooleanOption attachedOption, String key) {
        super(x, y, width, height, new LiteralText(""), button -> {
            ((BooleanOption) Osmium.options.get(attachedOption.identifier)).variable = !((BooleanOption) Osmium.options.get(attachedOption.identifier)).variable;
            if(((BooleanOption) Osmium.options.get(attachedOption.identifier)).variable) {
                button.setMessage(new TranslatableText(key + "enabled"));
            } else {
                button.setMessage(new TranslatableText(key + "disabled"));
            }
        });
        this.attachedOption = attachedOption;
        this.key = key;
        if(((BooleanOption) Osmium.options.get(attachedOption.identifier)).variable) {
            super.setMessage(new TranslatableText(key + "enabled"));
        } else {
            super.setMessage(new TranslatableText(key + "disabled"));
        }
    }


}
