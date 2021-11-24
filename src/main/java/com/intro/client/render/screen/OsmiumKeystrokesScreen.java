package com.intro.client.render.screen;

import com.intro.client.OsmiumClient;
import com.intro.client.render.color.Color;
import com.intro.client.render.widget.BooleanButtonWidget;
import com.intro.client.render.widget.ColorOptionWidget;
import com.intro.client.render.widget.DoubleSliderWidget;
import com.intro.client.util.OptionUtil;
import com.intro.common.config.Options;
import com.intro.common.config.options.Option;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class OsmiumKeystrokesScreen extends Screen {

    private final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();

    public OsmiumKeystrokesScreen(Screen parent) {
        super(new TranslatableComponent("osmium.options.keystrokes_settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button backButton;
        ColorOptionWidget colorSelectWidget;
        if(mc.options.guiScale > 4) {
            backButton = new Button(this.width / 2 - 175, this.height / 4 + 120, 150, 20, new TranslatableComponent("osmium.options.video_options.back"), (buttonWidget) -> mc.setScreen(this.parent));
            colorSelectWidget = new ColorOptionWidget(this.width / 2 + 25, this.height / 4, 128, 128, Options.KeystrokesColor);
        } else {
            backButton = new Button(this.width / 2 - 100, this.height / 4 + 225, 200, 20, new TranslatableComponent("osmium.options.video_options.back"), (buttonWidget) -> mc.setScreen(this.parent));
            colorSelectWidget = new ColorOptionWidget(this.width / 2 + 25, this.height / 4 - 60, 256, 256, Options.KeystrokesColor);
        }

        Button resetColorWidget = new Button(this.width / 2 - 175, this.height / 4 + 120, 150, 20, new TranslatableComponent("osmium.options.reset_color"), (buttonWidget) -> OsmiumClient.options.put(Options.KeystrokesColor, new Option<>(Options.KeystrokesColor, new Color(0.1f, 0.1f, 0.1f, 0.2f))));
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
        OptionUtil.save();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredString(matrices, mc.font, new TranslatableComponent("osmium.options.keystrokes_settings"), this.width / 2, 15, 0xffffff);
        drawString(matrices, mc.font, new TranslatableComponent("osmium.version"), 20, this.height - 20, 0xffffff);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
