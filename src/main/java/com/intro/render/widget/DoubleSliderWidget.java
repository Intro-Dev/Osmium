package com.intro.render.widget;

import com.intro.Osmium;
import com.intro.config.DoubleOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.OptionSliderWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;

public class DoubleSliderWidget extends OptionSliderWidget {

    private final DoubleOption AttachedOption;
    public final String key;
    private double minVal;
    private double maxVal;

    public DoubleSliderWidget(MinecraftClient mc, int x, int y, int width, int height, DoubleOption doubleOption, String key, double minVal, double maxVal) {
        super(mc.options, x, y, width, height, doubleOption.variable);
        this.AttachedOption = doubleOption;
        this.key = key;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        double scaledVal = (maxVal - minVal) * this.value;
        scaledVal = MathHelper.clamp(scaledVal, minVal, maxVal);
        this.setMessage(new LiteralText(new TranslatableText(key).getString() + Math.round(scaledVal)));
    }

    @Override
    protected void applyValue() {
        double scaledVal = (maxVal - minVal) * this.value;
        scaledVal = MathHelper.clamp(scaledVal, minVal, maxVal);
        ((DoubleOption) Osmium.options.get(AttachedOption.identifier)).variable = scaledVal;
    }
}
