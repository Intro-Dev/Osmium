package com.intro.render.widget;

import com.intro.config.options.EnumOption;
import com.intro.util.EnumUtil;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

public class EnumSelectWidget extends ButtonWidget {

    public EnumOption attachedOption;
    public final String key;

    @SuppressWarnings("unsafe")
    public EnumSelectWidget(int x, int y, int width, int height, @NotNull EnumOption attachedOption, String key) {
        super(x, y, width, height, new LiteralText(""), button -> {
            attachedOption.variable = EnumUtil.nextEnum(attachedOption.variable);
            button.setMessage(new TranslatableText(key + attachedOption.variable.name().toLowerCase()));
        });

        this.setMessage(new TranslatableText(key + attachedOption.variable.name().toLowerCase()));

        this.attachedOption = attachedOption;
        this.key = key;
    }


}
