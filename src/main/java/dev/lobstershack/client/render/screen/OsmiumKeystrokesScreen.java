package dev.lobstershack.client.render.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.render.color.Color;
import dev.lobstershack.client.render.widget.AbstractScalableButton;
import dev.lobstershack.client.render.widget.BooleanButtonWidget;
import dev.lobstershack.client.render.widget.ColorOptionWidget;
import dev.lobstershack.client.render.widget.DoubleSliderWidget;
import dev.lobstershack.common.config.Options;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class OsmiumKeystrokesScreen extends Screen {

    private final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();

    public OsmiumKeystrokesScreen(Screen parent) {
        super(Component.translatable("osmium.options.keystrokes_settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button backButton;
        ColorOptionWidget colorSelectWidget;
        if(mc.options.guiScale().get() > 4) {
            backButton = new AbstractScalableButton(this.width / 2 - 175, this.height / 4 + 120, 150, 20, Component.translatable("osmium.options.video_options.back"), (buttonWidget) -> mc.setScreen(this.parent));
            colorSelectWidget = new ColorOptionWidget(this.width / 2 + 25, this.height / 4, 128, 128, Options.KeystrokesColor);
        } else {
            backButton = new AbstractScalableButton(this.width / 2 - 100, this.height / 4 + 225, 200, 20, Component.translatable("osmium.options.video_options.back"), (buttonWidget) -> mc.setScreen(this.parent));
            colorSelectWidget = new ColorOptionWidget(this.width / 2 + 25, this.height / 4 - 60, 256, 256, Options.KeystrokesColor);
        }

        Button resetColorWidget = new AbstractScalableButton(this.width / 2 - 175, this.height / 4 + 120, 150, 20, Component.translatable("osmium.options.reset_color"), (buttonWidget) -> Options.KeystrokesColor.set(new Color(0.1f, 0.1f, 0.1f, 0.2f)));
        BooleanButtonWidget enabledWidget = new BooleanButtonWidget(this.width / 2 - 175, this.height / 4, 150, 20, Options.KeystrokesEnabled, "osmium.options.keystrokes_");
        BooleanButtonWidget rgbSelectWidget = new BooleanButtonWidget(this.width / 2 - 175, this.height / 4 + 40, 150, 20, Options.KeystrokesRgb, "osmium.options.rgb_");
        DoubleSliderWidget alphaSelectWidget = new DoubleSliderWidget(mc, this.width / 2 - 175, this.height / 4 + 80, 150, 20, Options.KeystrokesAlpha, "osmium.options.transparency", 0, 1, 10);

        this.addRenderableWidget(resetColorWidget);
        this.addRenderableWidget(enabledWidget);
        this.addRenderableWidget(backButton);
        this.addRenderableWidget(rgbSelectWidget);
        this.addRenderableWidget(colorSelectWidget);
        this.addRenderableWidget(alphaSelectWidget);
    }

    @Override
    public void onClose() {
        super.onClose();
        Options.save();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredString(matrices, mc.font, Component.translatable("osmium.options.keystrokes_settings"), this.width / 2, 15, 0xffffff);
        drawString(matrices, mc.font, Component.translatable("osmium.version"), 20, this.height - 20, 0xffffff);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
