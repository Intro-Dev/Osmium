package com.intro.client.render.screen;

import com.intro.client.render.widget.BooleanButtonWidget;
import com.intro.client.render.widget.DoubleSliderWidget;
import com.intro.client.util.OptionUtil;
import com.intro.common.config.Options;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class OsmiumToggleSneakOptionsScreen extends Screen {

    private final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();


    public OsmiumToggleSneakOptionsScreen(Screen parent) {
        super(new TranslatableComponent("osmium.options.toggle_sneak_settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button backButton = new Button(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableComponent("osmium.options.video_options.back"), (buttonWidget) -> mc.setScreen(this.parent));


        BooleanButtonWidget toggleSprintToggleWidget = new BooleanButtonWidget(this.width / 2 - 175, this.height / 6 + 20, 150, 20, Options.ToggleSprintEnabled, "osmium.options.toggle_sprint_");
        BooleanButtonWidget toggleSneakToggleWidget = new BooleanButtonWidget(this.width / 2 + 25, this.height / 6 + 20, 150, 20, Options.ToggleSneakEnabled, "osmium.options.toggle_sneak_");
        BooleanButtonWidget flyBoostEnabledWidget = new BooleanButtonWidget(this.width / 2 - 175, this.height / 6 + 60, 150, 20, Options.FlyBoostEnabled, "osmium.options.fly_boost_");
        DoubleSliderWidget flyBoostAmountWidget = new DoubleSliderWidget(mc, this.width / 2 + 25, this.height / 6 + 60, 150, 20, Options.FlyBoostAmount, "osmium.options.fly_boost_amount", 0, 10, 10);


        this.addRenderableWidget(toggleSneakToggleWidget);
        this.addRenderableWidget(toggleSprintToggleWidget);
        this.addRenderableWidget(backButton);
        this.addRenderableWidget(flyBoostAmountWidget);
        this.addRenderableWidget(flyBoostEnabledWidget);
    }

    @Override
    public void onClose() {
        super.onClose();
        OptionUtil.save();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredString(matrices, mc.font, new TranslatableComponent("osmium.options.toggle_sneak_settings"), this.width / 2, 15, 0xffffff);
        drawString(matrices, mc.font, new TranslatableComponent("osmium.version"), 20, this.height - 20, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
