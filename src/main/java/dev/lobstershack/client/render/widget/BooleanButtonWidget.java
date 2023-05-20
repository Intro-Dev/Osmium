package dev.lobstershack.client.render.widget;

import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.config.options.Option;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;


public class BooleanButtonWidget extends AbstractScalableButton {

    public final Option<Boolean> attachedOption;
    public final String key;

    public BooleanButtonWidget(int x, int y, int width, int height, Option<Boolean> attachedOption, String key) {
        super(x, y, width, height, Component.literal(""), button -> {
            if(!Options.getOverwrittenOptions().containsKey(attachedOption.getIdentifier())) {
                attachedOption.set(!attachedOption.get());
            } else {
                button.active = false;
            }
            if(attachedOption.get()) {
                button.setMessage(Component.translatable(key + "enabled"));
            } else {
                button.setMessage(Component.translatable(key + "disabled"));
            }

        }, 1f);
        this.attachedOption = attachedOption;
        this.key = key;
        if(Options.getOverwrittenOptions().containsKey(attachedOption.getIdentifier())) {
            this.active = false;
        }
        if(attachedOption.get()) {
            super.setMessage(Component.translatable(key + "enabled"));
        } else {
            super.setMessage(Component.translatable(key + "disabled"));
        }
    }

    public BooleanButtonWidget(int x, int y, int width, int height, Option<Boolean> attachedOption, String key, float scale) {
        super(x, y, width, height, Component.literal(""), button -> {
            if(!Options.getOverwrittenOptions().containsKey(attachedOption.getIdentifier())) {
                attachedOption.set(!attachedOption.get());
            } else {
                button.active = false;
            }
            if(attachedOption.get()) {
                button.setMessage(Component.translatable(key + "enabled"));
            } else {
                button.setMessage(Component.translatable(key + "disabled"));
            }

        }, scale);
        this.attachedOption = attachedOption;
        this.key = key;
        if(Options.getOverwrittenOptions().containsKey(attachedOption.getIdentifier())) {
            this.active = false;
        }
        if(attachedOption.get()) {
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
