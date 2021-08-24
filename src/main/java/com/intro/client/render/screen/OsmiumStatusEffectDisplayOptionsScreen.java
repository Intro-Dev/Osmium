package com.intro.client.render.screen;

import com.intro.client.render.widget.DoubleSliderWidget;
import com.intro.client.render.widget.EnumSelectWidget;
import com.intro.client.util.OptionUtil;
import com.intro.common.config.Options;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

// the name, I know
public class OsmiumStatusEffectDisplayOptionsScreen extends Screen {

    private final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();


    public OsmiumStatusEffectDisplayOptionsScreen(Screen parent) {
        super(new TranslatableComponent("osmium.options.status_effect_display_settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button backButton = new Button(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableComponent("osmium.options.video_options.back"), (buttonWidget) -> mc.setScreen(this.parent));

        EnumSelectWidget displayModeWidget = new EnumSelectWidget(this.width / 2 - 175, this.height / 6 + 20, 150, 20, Options.StatusEffectDisplayMode, "osmium.options.status_effect_display_");
        DoubleSliderWidget maxDisplayedWidget = new DoubleSliderWidget(mc, this.width / 2 - 175, this.height / 6 + 60, 150, 20, Options.MaxStatusEffectsDisplayed, "osmium.options.max_status_display", 0, 10, 1);
        DoubleSliderWidget displayScaleWidget = new DoubleSliderWidget(mc, this.width / 2 + 25, this.height / 6 + 20, 150, 20, Options.StatusEffectDisplayScale, "osmium.options.status_display_scale", 0, 10, 1);


        this.addRenderableWidget(backButton);
        this.addRenderableWidget(displayModeWidget);
        this.addRenderableWidget(maxDisplayedWidget);
        this.addRenderableWidget(displayScaleWidget);

    }

    @Override
    public void onClose() {
        super.onClose();
        OptionUtil.save();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredString(matrices, mc.font, new TranslatableComponent("osmium.options.status_effect_display_settings"), this.width / 2, 15, 0xffffff);
        drawString(matrices, mc.font, new TranslatableComponent("osmium.version"), 20, this.height - 20, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
