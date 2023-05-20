package dev.lobstershack.client.render.widget;

import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.config.options.Option;
import dev.lobstershack.client.util.DebugUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractOptionSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.Level;

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
        this.value = (attachedOption.get() / (maxVal - minVal)) - minVal;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        if(Options.getOverwrittenOptions().containsKey(attachedOption.getIdentifier())) {
            this.active = false;
        }
        this.setMessage(Component.literal(Component.translatable(key).getString() + (Math.round(attachedOption.get() * roundTo) / roundTo)));
        DebugUtil.logIfDebug(this.getMessage(), Level.INFO);
    }

    @Override
    protected void applyValue() {
        if(!Options.getOverwrittenOptions().containsKey(attachedOption.getIdentifier())) {
            double scaledVal = (maxVal - minVal) * this.value;
            scaledVal = Mth.clamp(scaledVal, minVal, maxVal);
            attachedOption.set(Math.round(scaledVal * roundTo) / roundTo);
            updateMessage();
        }
    }


    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.value = Mth.clamp(value, 0, 1);
        super.renderWidget(graphics, mouseX, mouseY, delta);
    }
}
