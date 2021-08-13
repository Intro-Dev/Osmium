package com.intro.render.screen;

import com.intro.Osmium;
import com.intro.config.options.BlockOutlineMode;
import com.intro.config.options.EnumOption;
import com.intro.render.widget.ColorOptionWidget;
import com.intro.render.widget.DoubleSliderWidget;
import com.intro.util.OptionUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class OsmiumBlockOptionsScreen extends Screen {

    private final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();

    public OsmiumBlockOptionsScreen(Screen parent) {
        super(new TranslatableComponent("osmium.options.blockoptionsettings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button backButton = new Button(this.width / 2 - 100, this.height / 6 + 300, 200, 20, new TranslatableComponent("osmium.options.videooptions.back"), (buttonWidget) -> {
            mc.setScreen(this.parent);
        });

        ColorOptionWidget colorSelectWidget = new ColorOptionWidget(this.width / 2 + 25, this.height / 6 - 40, Osmium.options.getColorOption(Osmium.options.BlockOutlineColor.identifier));

        Button toggleOverlayButton = new Button(this.width / 2 - 175, this.height / 6 + 70, 150, 20, new TranslatableComponent("osmium.options.overlay" + ((EnumOption) OptionUtil.Options.BlockOutlineMode.get()).variable.toString().toLowerCase()), (buttonWidget) -> {
            ((EnumOption) OptionUtil.Options.BlockOutlineMode.get()).variable = ((BlockOutlineMode) ((EnumOption) OptionUtil.Options.BlockOutlineMode.get()).variable).next();
            buttonWidget.setMessage(new TranslatableComponent("osmium.options.overlay" + ((EnumOption) OptionUtil.Options.BlockOutlineMode.get()).variable.toString().toLowerCase()));
        });

        DoubleSliderWidget alphaSelectWidget = new DoubleSliderWidget(mc, this.width / 2 - 175, this.height / 6 + 110, 150, 20, Osmium.options.getDoubleOption(Osmium.options.BlockOutlineAlpha.identifier), "osmium.options.blockoverlayalpha", 0, 1, 10);

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
        drawCenteredString(matrices, mc.font, new TranslatableComponent("osmium.options.blockoptionsettings"), this.width / 2, 15, 0xffffff);
        drawString(matrices, mc.font, new TranslatableComponent("osmium.version"), 20, this.height - 20, 0xffffff);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
