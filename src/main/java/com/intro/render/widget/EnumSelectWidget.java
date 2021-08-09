package com.intro.render.widget;

import com.intro.config.options.EnumOption;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.EnumSet;
import java.util.Set;

public class EnumSelectWidget extends ButtonWidget {

    public EnumOption attachedOption;
    Set<Enum<?>> optionsEnums;
    public final String key;

    @SuppressWarnings("unsafe")
    public EnumSelectWidget(int x, int y, int width, int height, EnumOption attachedOption, String key) {
        super(x, y, width, height, new LiteralText(""), button -> {
            Set<Enum<?>> enums = EnumSet.allOf(attachedOption.variable.getClass());

            System.out.println("changing");
            for(Enum<?> e : enums) {
                button.setMessage(new TranslatableText(key + e.name().toLowerCase()));
            }
        });
        optionsEnums = EnumSet.allOf(attachedOption.variable.getClass());
        for(Enum<?> e : optionsEnums) {
            this.setMessage(new TranslatableText(key + e.name().toLowerCase()));
        }
        this.attachedOption = attachedOption;
        this.key = key;
    }


}
