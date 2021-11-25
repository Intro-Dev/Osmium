package com.intro.client.render.widget;

import com.intro.client.OsmiumClient;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class BooleanButtonWidget extends AbstractScalableButton {

    public final String optionId;
    public final String key;

    public BooleanButtonWidget(int x, int y, int width, int height, String optionId, String key) {
        super(x, y, width, height, new TextComponent(""), button -> {
            if(!OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
                OsmiumClient.options.getBooleanOption(optionId).set(!OsmiumClient.options.getBooleanOption(optionId).get());
            } else {
                button.active = false;
            }
            if(OsmiumClient.options.getBooleanOption(optionId).get()) {
                button.setMessage(new TranslatableComponent(key + "enabled"));
            } else {
                button.setMessage(new TranslatableComponent(key + "disabled"));
            }

        }, 1f);
        this.optionId = optionId;
        this.key = key;
        if(OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
            this.active = false;
        }
        if(OsmiumClient.options.getBooleanOption(optionId).get()) {
            super.setMessage(new TranslatableComponent(key + "enabled"));
        } else {
            super.setMessage(new TranslatableComponent(key + "disabled"));
        }
    }

    public BooleanButtonWidget(int x, int y, int width, int height, String optionId, String key, float scale) {
        super(x, y, width, height, new TextComponent(""), button -> {
            if(!OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
                OsmiumClient.options.getBooleanOption(optionId).set(!OsmiumClient.options.getBooleanOption(optionId).get());
            } else {
                button.active = false;
            }
            if(OsmiumClient.options.getBooleanOption(optionId).get()) {
                button.setMessage(new TranslatableComponent(key + "enabled"));
            } else {
                button.setMessage(new TranslatableComponent(key + "disabled"));
            }

        }, scale);
        this.optionId = optionId;
        this.key = key;
        if(OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
            this.active = false;
        }
        if(OsmiumClient.options.getBooleanOption(optionId).get()) {
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
