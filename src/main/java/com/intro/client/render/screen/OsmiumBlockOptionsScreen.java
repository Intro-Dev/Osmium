package com.intro.client.render.screen;

import com.intro.client.render.widget.ColorOptionWidget;
import com.intro.client.render.widget.DoubleSliderWidget;
import com.intro.client.render.widget.EnumSelectWidget;
import com.intro.client.util.OptionUtil;
import com.intro.common.config.Options;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class OsmiumBlockOptionsScreen extends Screen {

    private final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();

    public OsmiumBlockOptionsScreen(Screen parent) {
        super(Component.translatable("osmium.options.block_option_settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button backButton;
        ColorOptionWidget colorSelectWidget;
        if(mc.options.guiScale().get() > 4) {
            backButton = new Button(this.width / 2 - 175, this.height / 4 + 120, 150, 20, Component.translatable("osmium.options.video_options.back"), (buttonWidget) -> mc.setScreen(this.parent));
            colorSelectWidget = new ColorOptionWidget(this.width / 2 + 25, this.height / 4, 128, 128, Options.BlockOutlineColor);
        } else {
            backButton = new Button(this.width / 2 - 100, this.height / 4 + 225, 200, 20, Component.translatable("osmium.options.video_options.back"), (buttonWidget) -> mc.setScreen(this.parent));
            colorSelectWidget = new ColorOptionWidget(this.width / 2 + 25, this.height / 4 - 60, 256, 256, Options.BlockOutlineColor);

        }


        EnumSelectWidget toggleOverlayButton = new EnumSelectWidget(this.width / 2 - 175, this.height / 4 + 40, 150, 20, Options.BlockOutlineMode, "osmium.options.overlay_");

        DoubleSliderWidget alphaSelectWidget = new DoubleSliderWidget(mc, this.width / 2 - 175, this.height / 4 + 80, 150, 20, Options.BlockOutlineAlpha, "osmium.options.block_overlay_alpha", 0, 1, 10);

        this.addRenderableWidget(backButton);
        this.addRenderableWidget(toggleOverlayButton);
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
        drawCenteredString(matrices, mc.font, Component.translatable("osmium.options.block_option_settings"), this.width / 2, 15, 0xffffff);
        drawString(matrices, mc.font, Component.translatable("osmium.version"), 20, this.height - 20, 0xffffff);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
