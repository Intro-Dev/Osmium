package com.intro.client.render.widget;

import com.intro.client.OsmiumClient;
import com.intro.client.util.EnumUtil;
import com.intro.common.config.options.EnumOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class EnumSelectWidget extends Button {

    public final String optionId;
    public final String key;

    @SuppressWarnings("unsafe")
    public EnumSelectWidget(int x, int y, int width, int height, String optionId, String key) {
        super(x, y, width, height, new TextComponent(""), button -> {
            if(!OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
                Enum<?> attachedEnum;
                attachedEnum = EnumUtil.nextEnum(OsmiumClient.options.getEnumOption(optionId).variable);
                OsmiumClient.options.put(optionId, new EnumOption(optionId, attachedEnum));
                button.setMessage(new TranslatableComponent(key + attachedEnum.name().toLowerCase()));
            } else {
                System.out.println("inactive");
                button.active = false;
            }
        });


        if(OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
            this.active = false;
        }
        Enum<?> attachedEnum = OsmiumClient.options.getEnumOption(optionId).variable;
        this.setMessage(new TranslatableComponent(key + attachedEnum.name().toLowerCase()));

        this.optionId = optionId;
        this.key = key;
    }


}
