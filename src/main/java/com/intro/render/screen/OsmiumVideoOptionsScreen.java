package com.intro.render.screen;

import com.intro.config.BooleanOption;
import com.intro.config.CapeRenderingMode;
import com.intro.config.EnumOption;
import com.intro.config.OptionUtil;
import com.intro.render.widget.BooleanButtonWidget;
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
    private BooleanButtonWidget ToggleRainWidget;
    private BooleanButtonWidget ToggleFireworksWidget;
    private BooleanButtonWidget ToggleNetherParticlesWidget;
    private ButtonWidget BlockOptionScreenButton;

    public OsmiumVideoOptionsScreen(Screen parent) {
        super(new TranslatableText("osmium.options.videooptions.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        BackButton = new ButtonWidget(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableText("osmium.options.videooptions.back"), (buttonWidget) -> {
            mc.openScreen(this.parent);
        });

        BlockOptionScreenButton = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 60, 150, 20, new TranslatableText("osmium.options.blockoptionsettings"), (buttonWidget) -> {
            mc.openScreen(new OsmiumBlockOptionsScreen(this));
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

        ToggleRainWidget = new BooleanButtonWidget(this.width / 2 - 75, this.height / 6 + 20, 150, 20, (BooleanOption) OptionUtil.Options.NoRainEnabled.get(), "osmium.options.rain");
        ToggleFireworksWidget = new BooleanButtonWidget(this.width / 2 + 125, this.height / 6 + 20, 150, 20, ((BooleanOption) OptionUtil.Options.FireworksDisabled.get()), "osmium.options.fireworks");
        ToggleNetherParticlesWidget = new BooleanButtonWidget(this.width / 2 - 275, this.height / 6 + 60, 150, 20, ((BooleanOption) OptionUtil.Options.DecreaseNetherParticles.get()), "osmium.options.netherparticles");

        this.addDrawableChild(BackButton);
        this.addDrawableChild(ToggleCapeWidget);
        this.addDrawableChild(ToggleRainWidget);
        this.addDrawableChild(ToggleFireworksWidget);
        this.addDrawableChild(ToggleNetherParticlesWidget);
        this.addDrawableChild(BlockOptionScreenButton);
    }

    @Override
    public void onClose() {
        OptionUtil.save();
        super.onClose();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, mc.textRenderer, new TranslatableText("osmium.options.videooptions.title"), this.width / 2, 15, 0xffffff);
        drawTextWithShadow(matrices, mc.textRenderer, new TranslatableText("osmium.version"), 20, this.height - 20, 0xffffff);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
