package com.intro.render.screen;

import com.intro.config.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class OsmiumVideoOptionsScreen extends Screen {

    private Screen parent;
    private MinecraftClient mc = MinecraftClient.getInstance();

    private ButtonWidget BackButton;
    private ButtonWidget ToggleCapeWidget;
    private ButtonWidget ToggleRainWidget;

    public OsmiumVideoOptionsScreen(Screen parent) {
        super(new TranslatableText("osmium.options.videooptions.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        BackButton = new ButtonWidget(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableText("osmium.options.videooptions.back"), (buttonWidget) -> {
            mc.openScreen(this.parent);
        });

        ToggleCapeWidget = new ButtonWidget(this.width / 2 - 275, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.videooptions.cape" + ((EnumOption) OptionUtil.Options.CustomCapeMode.get()).variable.toString().toLowerCase()), (buttonWidget) -> {

            ((EnumOption) OptionUtil.Options.CustomCapeMode.get()).variable = ((CapeRenderingMode) ((EnumOption) OptionUtil.Options.CustomCapeMode.get()).variable).next();
            mc.worldRenderer.reload();
            if(((EnumOption) OptionUtil.Options.CustomCapeMode.get()).variable == CapeRenderingMode.DISABLED) {
                buttonWidget.setMessage(new TranslatableText("osmium.options.videooptions.capedisabled"));
            } else if(((EnumOption) OptionUtil.Options.CustomCapeMode.get()).variable == CapeRenderingMode.OPTIFINE) {
                buttonWidget.setMessage(new TranslatableText("osmium.options.videooptions.capeoptifine"));
            } else if(((EnumOption) OptionUtil.Options.CustomCapeMode.get()).variable == CapeRenderingMode.ALL) {
                buttonWidget.setMessage(new TranslatableText("osmium.options.videooptions.capeall"));
            }


        });

        if(((BooleanOption) OptionUtil.Options.NoRainEnabled.get()).variable) {
            ToggleRainWidget = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.rainenabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.NoRainEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.NoRainEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.NoRainEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.rainenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.raindisabled"));
                }
            });
        } else {
            ToggleRainWidget = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.raindisabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.NoRainEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.NoRainEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.NoRainEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.rainenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("o smium.options.raindisabled"));
                }
            });
        }

        this.addDrawableChild(BackButton);
        this.addDrawableChild(ToggleCapeWidget);
        this.addDrawableChild(ToggleRainWidget);
    }

    @Override
    public void onClose() {
        super.onClose();
        OptionUtil.save();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, mc.textRenderer, new TranslatableText("osmium.options.videooptions.title"), this.width / 2, 15, 0xffffff);
        drawTextWithShadow(matrices, mc.textRenderer, new TranslatableText("osmium.version"), 20, this.height - 20, 0xffffff);

        super.render(matrices, mouseX, mouseY, delta);
    }
}