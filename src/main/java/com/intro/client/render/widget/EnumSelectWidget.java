package com.intro.client.render.widget;

import com.intro.client.util.EnumUtil;
import com.intro.common.config.options.EnumOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public class EnumSelectWidget extends Button {

    public EnumOption attachedOption;
    public final String key;

    @SuppressWarnings("unsafe")
    public EnumSelectWidget(int x, int y, int width, int height, @NotNull EnumOption attachedOption, String key) {
        super(x, y, width, height, new TextComponent(""), button -> {
            attachedOption.variable = EnumUtil.nextEnum(attachedOption.variable);
            button.setMessage(new TranslatableComponent(key + attachedOption.variable.name().toLowerCase()));
        });

        this.setMessage(new TranslatableComponent(key + attachedOption.variable.name().toLowerCase()));

        this.attachedOption = attachedOption;
        this.key = key;
    }


}
