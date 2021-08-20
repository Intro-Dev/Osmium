package com.intro.client.render.widget;

import com.intro.client.OsmiumClient;
import com.intro.client.util.EnumUtil;
import com.intro.common.config.options.EnumOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public class EnumSelectWidget extends Button {

    public final EnumOption attachedOption;
    public final String key;

    @SuppressWarnings("unsafe")
    public EnumSelectWidget(int x, int y, int width, int height, @NotNull EnumOption attachedOption, String key) {
        super(x, y, width, height, new TextComponent(""), button -> {
            if(!OsmiumClient.options.getOverwrittenOptions().containsKey(attachedOption.identifier)) {
                attachedOption.variable = EnumUtil.nextEnum(attachedOption.variable);
                button.setMessage(new TranslatableComponent(key + attachedOption.variable.name().toLowerCase()));
            } else {
                System.out.println("inactive");
                button.active = false;
            }
        });


        if(OsmiumClient.options.getOverwrittenOptions().containsKey(attachedOption.identifier)) {
            System.out.println("inactive");
            this.active = false;
        }
        this.setMessage(new TranslatableComponent(key + attachedOption.variable.name().toLowerCase()));

        this.attachedOption = attachedOption;
        this.key = key;
    }


}
