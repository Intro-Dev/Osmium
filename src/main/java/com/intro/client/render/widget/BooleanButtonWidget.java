package com.intro.client.render.widget;

import com.intro.client.OsmiumClient;
import com.intro.common.config.options.BooleanOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class BooleanButtonWidget extends Button {

    public BooleanOption attachedOption;
    public final String key;

    public BooleanButtonWidget(int x, int y, int width, int height, BooleanOption attachedOption, String key) {
        super(x, y, width, height, new TextComponent(""), button -> {
            if(!OsmiumClient.options.overwrittenOptions.containsValue(attachedOption)) {
                ((BooleanOption) OsmiumClient.options.get(attachedOption.identifier)).variable = !((BooleanOption) OsmiumClient.options.get(attachedOption.identifier)).variable;
            } else {
                button.active = false;
            }
            if(((BooleanOption) OsmiumClient.options.get(attachedOption.identifier)).variable) {
                button.setMessage(new TranslatableComponent(key + "enabled"));
            } else {
                button.setMessage(new TranslatableComponent(key + "disabled"));
            }
        });
        this.attachedOption = attachedOption;
        this.key = key;
        if(OsmiumClient.options.overwrittenOptions.containsValue(attachedOption)) {
            this.active = false;
        }
        if(((BooleanOption) OsmiumClient.options.get(attachedOption.identifier)).variable) {
            super.setMessage(new TranslatableComponent(key + "enabled"));
        } else {
            super.setMessage(new TranslatableComponent(key + "disabled"));
        }
    }



    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarrationPriority.HOVERED;
    }

}
