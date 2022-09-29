package com.intro.client.render.widget;

import com.intro.common.config.Options;
import com.intro.common.config.options.Option;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractOptionSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class DoubleSliderWidget extends AbstractOptionSliderButton {

    private final Option<Double> attachedOption;
    public final String key;
    private final double minVal;
    private final double maxVal;

    private final double roundTo;

    public DoubleSliderWidget(Minecraft mc, int x, int y, int width, int height, Option<Double> attachedOption, String key, double minVal, double maxVal, double roundTo) {
        super(mc.options, x, y, width, height, attachedOption.get());
        this.attachedOption = attachedOption;
        this.key = key;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.roundTo = roundTo;
        this.value = attachedOption.get();
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        if(Options.getOverwrittenOptions().containsKey(attachedOption.getIdentifier())) {
            this.active = false;
        }
        double scaledVal = (maxVal - minVal) * attachedOption.get();
        scaledVal = Mth.clamp(scaledVal, minVal, maxVal);
        this.setMessage(Component.literal(Component.translatable(key).getString() + (Math.round(scaledVal * roundTo) / roundTo)));
    }

    @Override
    protected void applyValue() {
        if(!Options.getOverwrittenOptions().containsKey(attachedOption.getIdentifier())) {
            double scaledVal = (maxVal - minVal) * this.value;
            scaledVal = Mth.clamp(scaledVal, minVal, maxVal);
            attachedOption.set(Math.round(scaledVal * roundTo) / roundTo);
        }
    }


    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.value = Mth.clamp(value, 0, 1);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
