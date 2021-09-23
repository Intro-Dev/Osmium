package com.intro.client.render.widget;

import com.intro.client.OsmiumClient;
import com.intro.common.config.options.BooleanOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class BooleanButtonWidget extends Button {

    public final String optionId;
    public final String key;

    public BooleanButtonWidget(int x, int y, int width, int height, String optionId, String key) {
        super(x, y, width, height, new TextComponent(""), button -> {
            if(!OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
                ((BooleanOption) OsmiumClient.options.get(optionId)).variable = !((BooleanOption) OsmiumClient.options.get(optionId)).variable;
            } else {
                button.active = false;
            }
            if(((BooleanOption) OsmiumClient.options.get(optionId)).variable) {
                button.setMessage(new TranslatableComponent(key + "enabled"));
            } else {
                button.setMessage(new TranslatableComponent(key + "disabled"));
            }

        });
        this.optionId = optionId;
        this.key = key;
        if(OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
            this.active = false;
        }
        if(((BooleanOption) OsmiumClient.options.get(optionId)).variable) {
            super.setMessage(new TranslatableComponent(key + "enabled"));
        } else {
            super.setMessage(new TranslatableComponent(key + "disabled"));
        }
    }



    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.HOVERED;
    }

}
