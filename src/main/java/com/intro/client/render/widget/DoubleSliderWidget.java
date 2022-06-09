package com.intro.client.render.widget;

import com.intro.client.OsmiumClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractOptionSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class DoubleSliderWidget extends AbstractOptionSliderButton {

    private final String optionId;
    public final String key;
    private final double minVal;
    private final double maxVal;

    private final double roundTo;

    public DoubleSliderWidget(Minecraft mc, int x, int y, int width, int height, String optionId, String key, double minVal, double maxVal, double roundTo) {
        super(mc.options, x, y, width, height, OsmiumClient.options.getDoubleOption(optionId).get());
        this.optionId = optionId;
        this.key = key;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.roundTo = roundTo;
        this.updateMessage();
        this.applyValue();
    }

    @Override
    protected void updateMessage() {
        if(OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
            this.active = false;
        }
        double scaledVal = (maxVal - minVal) * this.value;
        scaledVal = Mth.clamp(scaledVal, minVal, maxVal);
        this.setMessage(Component.literal(Component.translatable(key).getString() + (Math.round(scaledVal * roundTo) / roundTo)));
    }

    @Override
    protected void applyValue() {
        if(!OsmiumClient.options.getOverwrittenOptions().containsKey(optionId)) {
            double scaledVal = (maxVal - minVal) * this.value;
            scaledVal = Mth.clamp(scaledVal, minVal, maxVal);
            OsmiumClient.options.getDoubleOption(optionId).set(Math.round(scaledVal * roundTo) / roundTo);
        }
    }


    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.value = Mth.clamp(value, 0, 1);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
