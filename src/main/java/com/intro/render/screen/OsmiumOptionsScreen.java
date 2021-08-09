package com.intro.render.screen;

import com.intro.Osmium;
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
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;


public class OsmiumOptionsScreen extends Screen {

    private Screen parent;

    private int animationProgress = 0;

    private final Identifier LOGO_TEXTURE = new Identifier("osmium", "icon.png");

    public OsmiumOptionsScreen(Screen parent) {
        super(new TranslatableText("osmium.options.title"));
        this.parent = parent;
    }

    private final MinecraftClient mc = MinecraftClient.getInstance();


    @Override
    protected void init() {
        ButtonWidget fullBrightWidget = new BooleanButtonWidget(this.width / 2 - 275, this.height / 6 + 120, 150, 20, (BooleanOption) OptionUtil.Options.FullbrightEnabled.get(), "osmium.options.fullbright");
        ButtonWidget hurtBobWidget = new BooleanButtonWidget(this.width / 2 + 125, this.height / 6 + 120, 150, 20, ((BooleanOption) OptionUtil.Options.HurtbobbingEnabled.get()), "osmium.options.hurtbobbing");
        ButtonWidget noFireWidget = new BooleanButtonWidget(this.width / 2 - 275, this.height / 6 + 160, 150, 20, ((BooleanOption) OptionUtil.Options.NoFireEnabled.get()), "osmium.options.nofire");
        ButtonWidget fpsWidget = new BooleanButtonWidget(this.width / 2 - 75, this.height / 6 + 200, 150, 20, ((BooleanOption) OptionUtil.Options.FpsEnabled.get()), "osmium.options.fps");
        ButtonWidget smoothSneakWidget = new EnumSelectWidget(this.width / 2 + 125, this.height / 6 + 160, 150, 20, (EnumOption) OptionUtil.Options.SneakMode.get(), "osmium.options.sneak");

        ButtonWidget openVideoOptions = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 160, 150, 20, new TranslatableText("osmium.options.videooptions"), (buttonWidget) -> {
            mc.openScreen(new OsmiumVideoOptionsScreen(this));
        });

        ButtonWidget backButton = new ButtonWidget(this.width / 2 - 100, this.height / 6 + 300, 200, 20, new TranslatableText("osmium.options.videooptions.back"), (buttonWidget) -> {
            mc.openScreen(this.parent);
        });

        ButtonWidget openGuiEditing = new ButtonWidget(this.width / 2 - 275, this.height / 6 + 200, 150, 20, new TranslatableText("osmium.guiedit.title"), (buttonWidget) -> {
            mc.openScreen(new OsmiumGuiEditScreen(this));
        });

        ButtonWidget toggleSneakToggleWidget = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 120, 150, 20, new TranslatableText("osmium.options.togglesneaksettings"), (buttonWidget) -> {
            mc.openScreen(new OsmiumToggleSneakOptionsScreen(this));
        });

        ButtonWidget openGithubWidget = new ButtonWidget(this.width / 2 + 125, this.height / 6 + 200, 150, 20, new TranslatableText("osmium.opencredits"), this::openGithub);


        this.addDrawableChild(fullBrightWidget);
        this.addDrawableChild(toggleSneakToggleWidget);
        this.addDrawableChild(hurtBobWidget);
        this.addDrawableChild(noFireWidget);
        this.addDrawableChild(smoothSneakWidget);
        this.addDrawableChild(openVideoOptions);
        this.addDrawableChild(backButton);
        this.addDrawableChild(openGuiEditing);
        this.addDrawableChild(fpsWidget);
        this.addDrawableChild(openGithubWidget);
    }

    @Override
    public void onClose() {
        super.onClose();
        OptionUtil.save();
    }

    private void openGithub(@Nullable ButtonWidget widget) {
        try {
            Util.getOperatingSystem().open(new URI("https://github.com/Intro-Dev/Osmium"));
        } catch (URISyntaxException exception) {
            Osmium.LOGGER.warn("Failed in opening github link. How did this even happen?");
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        // set proper shaders
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, LOGO_TEXTURE);
        // gets the current shader color
        Color shaderColor = Color.fromFloatArray(RenderSystem.getShaderColor());
        RenderSystem.enableBlend();
        // renders osmium logo to screen with fade in
        matrices.push();
        // sets the current shader color to itself, but with a modified alpha for fade in effect
        RenderSystem.setShaderColor(shaderColor.getFloatR(), shaderColor.getFloatG(), shaderColor.getFloatB(), ((animationProgress * 4) - 1) / 255f);
        // scale image down to a good size
        matrices.scale(0.5f, 0.5f, 0.5f);
        // account for scaling difference
        // its width / 2 - 128 because we are scaling by 0.5, and 128 is the scaled dimensions of the image
        matrices.translate(this.width / 2f - 128, animationProgress,0);
        drawTexture(matrices, this.width / 2, 15, 0, 0, 256, 256);
        matrices.pop();

        matrices.push();
        RenderSystem.setShaderColor(shaderColor.getFloatR(), shaderColor.getFloatG(), shaderColor.getFloatB(), ((animationProgress * 4) - 1) / 255f);
        matrices.translate(0, animationProgress,0);
        drawCenteredText(matrices, mc.textRenderer, new TranslatableText("osmium.version"), this.width / 2, 140, 0xffffff);
        matrices.pop();
        // drawTextWithShadow(matrices, mc.textRenderer, new TranslatableText("osmium.github"), 20, this.height - 40, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
        // 57 is the max because of animation progress looking good at 3
        if(!(animationProgress >= 57))
            animationProgress += 3;
        RenderSystem.disableBlend();

    }



}
