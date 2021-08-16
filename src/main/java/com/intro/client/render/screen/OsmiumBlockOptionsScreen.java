package com.intro.client.render.screen;

import com.intro.client.OsmiumClient;
import com.intro.client.render.widget.ColorOptionWidget;
import com.intro.client.render.widget.DoubleSliderWidget;
import com.intro.client.util.OptionUtil;
import com.intro.common.config.options.BlockOutlineMode;
import com.intro.common.config.options.EnumOption;
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

        ColorOptionWidget colorSelectWidget = new ColorOptionWidget(this.width / 2 + 25, this.height / 6 - 40, OsmiumClient.options.getColorOption(OsmiumClient.options.BlockOutlineColor.identifier));

        Button toggleOverlayButton = new Button(this.width / 2 - 175, this.height / 6 + 70, 150, 20, new TranslatableComponent("osmium.options.overlay" + ((EnumOption) OptionUtil.Options.BlockOutlineMode.get()).variable.toString().toLowerCase()), (buttonWidget) -> {
            ((EnumOption) OptionUtil.Options.BlockOutlineMode.get()).variable = ((BlockOutlineMode) ((EnumOption) OptionUtil.Options.BlockOutlineMode.get()).variable).next();
            buttonWidget.setMessage(new TranslatableComponent("osmium.options.overlay" + ((EnumOption) OptionUtil.Options.BlockOutlineMode.get()).variable.toString().toLowerCase()));
        });

        DoubleSliderWidget alphaSelectWidget = new DoubleSliderWidget(mc, this.width / 2 - 175, this.height / 6 + 110, 150, 20, OsmiumClient.options.getDoubleOption(OsmiumClient.options.BlockOutlineAlpha.identifier), "osmium.options.blockoverlayalpha", 0, 1, 10);

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