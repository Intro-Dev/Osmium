package com.intro.client.render.widget;

import com.intro.client.OsmiumClient;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;


public class BooleanButtonWidget extends AbstractScalableButton {

    public final String optionId;
    public final String key;

    public BooleanButtonWidget(int x, int y, int width, int height, String optionId, String key) {
        super(x, y, width, height, Component.literal(""), button -> {
            if(!OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
                OsmiumClient.options.getBooleanOption(optionId).set(!OsmiumClient.options.getBooleanOption(optionId).get());
            } else {
                button.active = false;
            }
            if(OsmiumClient.options.getBooleanOption(optionId).get()) {
                button.setMessage(Component.translatable(key + "enabled"));
            } else {
                button.setMessage(Component.translatable(key + "disabled"));
            }

        }, 1f);
        this.optionId = optionId;
        this.key = key;
        if(OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
            this.active = false;
        }
        if(OsmiumClient.options.getBooleanOption(optionId).get()) {
            super.setMessage(Component.translatable(key + "enabled"));
        } else {
            super.setMessage(Component.translatable(key + "disabled"));
        }
    }

    public BooleanButtonWidget(int x, int y, int width, int height, String optionId, String key, float scale) {
        super(x, y, width, height, Component.literal(""), button -> {
            if(!OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
                OsmiumClient.options.getBooleanOption(optionId).set(!OsmiumClient.options.getBooleanOption(optionId).get());
            } else {
                button.active = false;
            }
            if(OsmiumClient.options.getBooleanOption(optionId).get()) {
                button.setMessage(Component.translatable(key + "enabled"));
            } else {
                button.setMessage(Component.translatable(key + "disabled"));
            }

        }, scale);
        this.optionId = optionId;
        this.key = key;
        if(OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
            this.active = false;
        }
        if(OsmiumClient.options.getBooleanOption(optionId).get()) {
            super.setMessage(Component.translatable(key + "enabled"));
        } else {
            super.setMessage(Component.translatable(key + "disabled"));
        }
    }



    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarrationPriority.HOVERED;
    }

}
