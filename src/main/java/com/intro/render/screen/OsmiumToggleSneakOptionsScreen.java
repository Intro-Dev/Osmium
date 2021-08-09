package com.intro.render.screen;

import com.intro.Osmium;
import com.intro.config.options.BooleanOption;
import com.intro.config.options.DoubleOption;
import com.intro.render.widget.BooleanButtonWidget;
import com.intro.render.widget.DoubleSliderWidget;
import com.intro.util.OptionUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class OsmiumToggleSneakOptionsScreen extends Screen {

    private final Screen parent;
    private final MinecraftClient mc = MinecraftClient.getInstance();


    public OsmiumToggleSneakOptionsScreen(Screen parent) {
        super(new TranslatableText("osmium.options.togglesneaksettings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ButtonWidget backButton = new ButtonWidget(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableText("osmium.options.videooptions.back"), (buttonWidget) -> {
            mc.openScreen(this.parent);
        });


        ButtonWidget toggleSprintToggleWidget = new BooleanButtonWidget(this.width / 2 - 175, this.height / 6 + 20, 150, 20, ((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()), "osmium.options.togglesprintdisabled");
        ButtonWidget toggleSneakToggleWidget = new BooleanButtonWidget(this.width / 2 + 25, this.height / 6 + 20, 150, 20, ((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()), "osmium.options.togglesneakenabled");
        ButtonWidget flyBoostEnabledWidget = new BooleanButtonWidget(this.width / 2 - 175, this.height / 6 + 60, 150, 20, ((BooleanOption) OptionUtil.Options.FlyBoostEnabled.get()), "osmium.options.flyboostenabled");
        DoubleSliderWidget flyBoostAmountWidget = new DoubleSliderWidget(mc, this.width / 2 + 25, this.height / 6 + 60, 150, 20, ((DoubleOption) Osmium.options.get(Osmium.options.FlyBoostAmount.identifier)), "osmium.options.flyboostamount", 0, 10, 10);


        this.addDrawableChild(toggleSneakToggleWidget);
        this.addDrawableChild(toggleSprintToggleWidget);
        this.addDrawableChild(backButton);
        this.addDrawableChild(flyBoostAmountWidget);
        this.addDrawableChild(flyBoostEnabledWidget);
    }

    @Override
    public void onClose() {
        super.onClose();
        OptionUtil.save();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, mc.textRenderer, new TranslatableText("osmium.options.togglesneaksettings"), this.width / 2, 15, 0xffffff);
        drawTextWithShadow(matrices, mc.textRenderer, new TranslatableText("osmium.version"), 20, this.height - 20, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
