package com.intro.render.screen;

import com.intro.config.options.BooleanOption;
import com.intro.config.options.EnumOption;
import com.intro.render.Color;
import com.intro.render.widget.BooleanButtonWidget;
import com.intro.render.widget.EnumSelectWidget;
import com.intro.util.OptionUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class OsmiumVideoOptionsScreen extends Screen {

    private Screen parent;
    private MinecraftClient mc = MinecraftClient.getInstance();

    private final Identifier LOGO_TEXTURE = new Identifier("osmium", "icon.png");

    public OsmiumVideoOptionsScreen(Screen parent) {
        super(new TranslatableText("osmium.options.videooptions.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ButtonWidget backButton = new ButtonWidget(this.width / 2 - 100, this.height / 6 + 300, 200, 20, new TranslatableText("osmium.options.videooptions.back"), (buttonWidget) -> {
            mc.openScreen(this.parent);
        });

        ButtonWidget blockOptionScreenButton = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 160, 150, 20, new TranslatableText("osmium.options.blockoptionsettings"), (buttonWidget) -> {
            mc.openScreen(new OsmiumBlockOptionsScreen(this));
        });

        EnumSelectWidget toggleCapeWidget = new EnumSelectWidget(this.width / 2 - 275, this.height / 6 + 120, 150, 20, ((EnumOption) OptionUtil.Options.CustomCapeMode.get()),"osmium.options.videooptions.cape");
        BooleanButtonWidget toggleRainWidget = new BooleanButtonWidget(this.width / 2 - 75, this.height / 6 + 120, 150, 20, (BooleanOption) OptionUtil.Options.NoRainEnabled.get(), "osmium.options.rain");
        BooleanButtonWidget toggleFireworksWidget = new BooleanButtonWidget(this.width / 2 + 125, this.height / 6 + 120, 150, 20, ((BooleanOption) OptionUtil.Options.FireworksDisabled.get()), "osmium.options.fireworks");
        BooleanButtonWidget toggleNetherParticlesWidget = new BooleanButtonWidget(this.width / 2 - 275, this.height / 6 + 160, 150, 20, ((BooleanOption) OptionUtil.Options.DecreaseNetherParticles.get()), "osmium.options.netherparticles");

        ButtonWidget statusEffectScreenButton = new ButtonWidget(this.width / 2 + 125, this.height / 6 + 160, 150, 20, new TranslatableText("osmium.options.statuseffectdisplaysettings"), (buttonWidget) -> {
            mc.openScreen(new OsmiumStatusEffectDisplayOptionsScreen(this));
        });

        this.addDrawableChild(backButton);
        this.addDrawableChild(toggleCapeWidget);
        this.addDrawableChild(toggleRainWidget);
        this.addDrawableChild(toggleFireworksWidget);
        this.addDrawableChild(toggleNetherParticlesWidget);
        this.addDrawableChild(blockOptionScreenButton);
        this.addDrawableChild(statusEffectScreenButton);
    }

    @Override
    public void onClose() {
        OptionUtil.save();
        super.onClose();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, LOGO_TEXTURE);

        Color shaderColor = Color.fromFloatArray(RenderSystem.getShaderColor());

        RenderSystem.enableBlend();
        // renders osmium logo to screen with fade in
        matrices.push();
        // sets the current shader color to itself, but with a modified alpha for fade in effect
        // 0.8901961 because thats the final result for the fade in animation on the main settings page
        RenderSystem.setShaderColor(shaderColor.getFloatR(), shaderColor.getFloatG(), shaderColor.getFloatB(), 0.8901961f);
        // scale image down to a good size
        matrices.scale(0.5f, 0.5f, 0.5f);
        // account for scaling difference
        // its width / 2 - 128 because we are scaling by 0.5, and 128 is the scaled dimensions of the image
        matrices.translate(this.width / 2f - 128, 57,0);
        drawTexture(matrices, this.width / 2, 15, 0, 0, 256, 256);
        matrices.pop();
        matrices.push();
        matrices.translate(0, 57,0);
        drawCenteredText(matrices, mc.textRenderer, new TranslatableText("osmium.version"), this.width / 2, 140, 0xffffff);
        matrices.pop();
        RenderSystem.disableBlend();
        super.render(matrices, mouseX, mouseY, delta);
    }
}
