package com.intro.render.widget;

import com.intro.Osmium;
import com.intro.config.options.DoubleOption;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractOptionSliderButton;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;

public class DoubleSliderWidget extends AbstractOptionSliderButton {

    private final DoubleOption AttachedOption;
    public final String key;
    private double minVal;
    private double maxVal;

    private double roundTo;

    public DoubleSliderWidget(Minecraft mc, int x, int y, int width, int height, DoubleOption doubleOption, String key, double minVal, double maxVal, double roundTo) {
        super(mc.options, x, y, width, height, doubleOption.variable);
        this.AttachedOption = doubleOption;
        this.key = key;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.roundTo = roundTo;
        this.updateMessage();
        this.applyValue();
    }

    @Override
    protected void updateMessage() {
        double scaledVal = (maxVal - minVal) * this.value;
        scaledVal = Mth.clamp(scaledVal, minVal, maxVal);
        this.setMessage(new TextComponent(new TranslatableComponent(key).getString() + (Math.round(scaledVal * roundTo) / roundTo)));
    }

    @Override
    protected void applyValue() {
        double scaledVal = (maxVal - minVal) * this.value;
        scaledVal = Mth.clamp(scaledVal, minVal, maxVal);
        ((DoubleOption) Osmium.options.get(AttachedOption.identifier)).variable = Math.round(scaledVal * roundTo) / roundTo;
    }


    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.value = Mth.clamp(value, 0, 1);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
