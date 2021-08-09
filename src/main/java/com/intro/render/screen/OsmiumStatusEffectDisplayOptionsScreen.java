package com.intro.render.screen;

import com.intro.Osmium;
import com.intro.config.options.DoubleOption;
import com.intro.config.options.EnumOption;
import com.intro.render.widget.DoubleSliderWidget;
import com.intro.render.widget.EnumSelectWidget;
import com.intro.util.OptionUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

// the name, I know
public class OsmiumStatusEffectDisplayOptionsScreen extends Screen {

    private final Screen parent;
    private final MinecraftClient mc = MinecraftClient.getInstance();


    public OsmiumStatusEffectDisplayOptionsScreen(Screen parent) {
        super(new TranslatableText("osmium.options.statuseffectdisplaysettings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ButtonWidget backButton = new ButtonWidget(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableText("osmium.options.videooptions.back"), (buttonWidget) -> {
            mc.openScreen(this.parent);
        });

        EnumSelectWidget displayModeWidget = new EnumSelectWidget(this.width / 2 - 175, this.height / 6 + 20, 150, 20, ((EnumOption) OptionUtil.Options.StatusEffectDisplayMode.get()), "osmium.options.statuseffectdisplay");
        DoubleSliderWidget maxDisplayedWidget = new DoubleSliderWidget(mc, this.width / 2 - 175, this.height / 6 + 60, 150, 20, ((DoubleOption) Osmium.options.get(Osmium.options.MaxStatusEffectsDisplayed.identifier)), "osmium.options.maxstatusdisplay", 0, 10, 1);
        DoubleSliderWidget displayScaleWidget = new DoubleSliderWidget(mc, this.width / 2 + 25, this.height / 6 + 20, 150, 20, ((DoubleOption) OptionUtil.Options.StatusEffectDisplayScale.get()), "osmium.options.statusdisplayscale", 0, 10, 1);


        this.addDrawableChild(backButton);
        this.addDrawableChild(displayModeWidget);
        this.addDrawableChild(maxDisplayedWidget);
        this.addDrawableChild(displayScaleWidget);

    }

    @Override
    public void onClose() {
        super.onClose();
        OptionUtil.save();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, mc.textRenderer, new TranslatableText("osmium.options.statuseffectdisplaysettings"), this.width / 2, 15, 0xffffff);
        drawTextWithShadow(matrices, mc.textRenderer, new TranslatableText("osmium.version"), 20, this.height - 20, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
