package com.intro.render.widget;

import com.intro.Osmium;
import com.intro.config.DoubleOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.OptionSliderWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class DoubleSliderWidget extends OptionSliderWidget {

    private final DoubleOption AttachedOption;
    public final String key;

    public DoubleSliderWidget(MinecraftClient mc, int x, int y, int width, int height, DoubleOption doubleOption, String key) {
        super(mc.options, x, y, width, height, doubleOption.variable);
        this.AttachedOption = doubleOption;
        this.key = key;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(new LiteralText(new TranslatableText(key).getString() + Math.round(AttachedOption.variable * 10)));
    }

    @Override
    protected void applyValue() {
        ((DoubleOption) Osmium.options.get(AttachedOption.identifier)).variable = this.value;
    }
}
