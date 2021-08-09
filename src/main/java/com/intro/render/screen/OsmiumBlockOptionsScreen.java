package com.intro.render.screen;

import com.intro.Osmium;
import com.intro.config.options.BlockOutlineMode;
import com.intro.config.options.EnumOption;
import com.intro.render.widget.ColorOptionWidget;
import com.intro.render.widget.DoubleSliderWidget;
import com.intro.util.OptionUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class OsmiumBlockOptionsScreen extends Screen {

    private final Screen parent;
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public OsmiumBlockOptionsScreen(Screen parent) {
        super(new TranslatableText("osmium.options.blockoptionsettings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ButtonWidget backButton = new ButtonWidget(this.width / 2 - 100, this.height / 6 + 300, 200, 20, new TranslatableText("osmium.options.videooptions.back"), (buttonWidget) -> {
            mc.openScreen(this.parent);
        });

        ColorOptionWidget colorSelectWidget = new ColorOptionWidget(this.width / 2 + 25, this.height / 6 - 40, Osmium.options.getColorOption(Osmium.options.BlockOutlineColor.identifier));

        ButtonWidget toggleOverlayButton = new ButtonWidget(this.width / 2 - 175, this.height / 6 + 70, 150, 20, new TranslatableText("osmium.options.overlay" + ((EnumOption) OptionUtil.Options.BlockOutlineMode.get()).variable.toString().toLowerCase()), (buttonWidget) -> {
            ((EnumOption) OptionUtil.Options.BlockOutlineMode.get()).variable = ((BlockOutlineMode) ((EnumOption) OptionUtil.Options.BlockOutlineMode.get()).variable).next();
            buttonWidget.setMessage(new TranslatableText("osmium.options.overlay" + ((EnumOption) OptionUtil.Options.BlockOutlineMode.get()).variable.toString().toLowerCase()));
        });

        DoubleSliderWidget alphaSelectWidget = new DoubleSliderWidget(mc, this.width / 2 - 175, this.height / 6 + 110, 150, 20, Osmium.options.getDoubleOption(Osmium.options.BlockOutlineAlpha.identifier), "osmium.options.blockoverlayalpha", 0, 1, 10);

        this.addDrawableChild(backButton);
        this.addDrawableChild(toggleOverlayButton);
        this.addDrawableChild(colorSelectWidget);
        this.addDrawableChild(alphaSelectWidget);
    }

    @Override
    public void onClose() {
        super.onClose();
        OptionUtil.save();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, mc.textRenderer, new TranslatableText("osmium.options.blockoptionsettings"), this.width / 2, 15, 0xffffff);
        drawTextWithShadow(matrices, mc.textRenderer, new TranslatableText("osmium.version"), 20, this.height - 20, 0xffffff);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
