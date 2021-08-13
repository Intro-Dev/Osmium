package com.intro.render.screen;

import com.intro.Osmium;
import com.intro.config.options.BooleanOption;
import com.intro.config.options.DoubleOption;
import com.intro.render.widget.BooleanButtonWidget;
import com.intro.render.widget.DoubleSliderWidget;
import com.intro.util.OptionUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class OsmiumToggleSneakOptionsScreen extends Screen {

    private final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();


    public OsmiumToggleSneakOptionsScreen(Screen parent) {
        super(new TranslatableComponent("osmium.options.togglesneaksettings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button backButton = new Button(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableComponent("osmium.options.videooptions.back"), (buttonWidget) -> {
            mc.setScreen(this.parent);
        });


        BooleanButtonWidget toggleSprintToggleWidget = new BooleanButtonWidget(this.width / 2 - 175, this.height / 6 + 20, 150, 20, ((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()), "osmium.options.togglesprint");
        BooleanButtonWidget toggleSneakToggleWidget = new BooleanButtonWidget(this.width / 2 + 25, this.height / 6 + 20, 150, 20, ((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()), "osmium.options.togglesneak");
        BooleanButtonWidget flyBoostEnabledWidget = new BooleanButtonWidget(this.width / 2 - 175, this.height / 6 + 60, 150, 20, ((BooleanOption) OptionUtil.Options.FlyBoostEnabled.get()), "osmium.options.flyboost");
        DoubleSliderWidget flyBoostAmountWidget = new DoubleSliderWidget(mc, this.width / 2 + 25, this.height / 6 + 60, 150, 20, ((DoubleOption) Osmium.options.get(Osmium.options.FlyBoostAmount.identifier)), "osmium.options.flyboostamount", 0, 10, 10);


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
        drawCenteredString(matrices, mc.font, new TranslatableComponent("osmium.options.togglesneaksettings"), this.width / 2, 15, 0xffffff);
        drawString(matrices, mc.font, new TranslatableComponent("osmium.version"), 20, this.height - 20, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
