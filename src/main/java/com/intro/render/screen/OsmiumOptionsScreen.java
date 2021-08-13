package com.intro.render.screen;

import com.intro.Osmium;
import com.intro.config.options.BooleanOption;
import com.intro.config.options.EnumOption;
import com.intro.render.Color;
import com.intro.render.widget.BooleanButtonWidget;
import com.intro.render.widget.EnumSelectWidget;
import com.intro.util.OptionUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;


public class OsmiumOptionsScreen extends Screen {

    private Screen parent;

    private int animationProgress = 0;

    private final ResourceLocation LOGO_TEXTURE = new ResourceLocation("osmium", "icon.png");

    public OsmiumOptionsScreen(Screen parent) {
        super(new TranslatableComponent("osmium.options.title"));
        this.parent = parent;
    }

    private final Minecraft mc = Minecraft.getInstance();


    @Override
    protected void init() {
        BooleanButtonWidget fullBrightWidget = new BooleanButtonWidget(this.width / 2 - 275, this.height / 6 + 120, 150, 20, (BooleanOption) OptionUtil.Options.FullbrightEnabled.get(), "osmium.options.fullbright");
        BooleanButtonWidget hurtBobWidget = new BooleanButtonWidget(this.width / 2 + 125, this.height / 6 + 120, 150, 20, ((BooleanOption) OptionUtil.Options.HurtbobbingEnabled.get()), "osmium.options.hurtbobbing");
        BooleanButtonWidget noFireWidget = new BooleanButtonWidget(this.width / 2 - 275, this.height / 6 + 160, 150, 20, ((BooleanOption) OptionUtil.Options.NoFireEnabled.get()), "osmium.options.nofire");
        BooleanButtonWidget fpsWidget = new BooleanButtonWidget(this.width / 2 - 75, this.height / 6 + 200, 150, 20, ((BooleanOption) OptionUtil.Options.FpsEnabled.get()), "osmium.options.fps");
        EnumSelectWidget smoothSneakWidget = new EnumSelectWidget(this.width / 2 + 125, this.height / 6 + 160, 150, 20, (EnumOption) OptionUtil.Options.SneakMode.get(), "osmium.options.sneak");

        Button openVideoOptions = new Button(this.width / 2 - 75, this.height / 6 + 160, 150, 20, new TranslatableComponent("osmium.options.videooptions"), (Button) -> {
            mc.setScreen(new OsmiumVideoOptionsScreen(this));
        });

        Button backButton = new Button(this.width / 2 - 100, this.height / 6 + 300, 200, 20, new TranslatableComponent("osmium.options.videooptions.back"), (Button) -> {
            mc.setScreen(this.parent);
        });

        Button openGuiEditing = new Button(this.width / 2 - 275, this.height / 6 + 200, 150, 20, new TranslatableComponent("osmium.guiedit.title"), (Button) -> {
            mc.setScreen(new OsmiumGuiEditScreen(this));
        });

        Button toggleSneakToggleWidget = new Button(this.width / 2 - 75, this.height / 6 + 120, 150, 20, new TranslatableComponent("osmium.options.togglesneaksettings"), (Button) -> {
            mc.setScreen(new OsmiumToggleSneakOptionsScreen(this));
        });

        Button openGithubWidget = new Button(this.width / 2 + 125, this.height / 6 + 200, 150, 20, new TranslatableComponent("osmium.opencredits"), this::openCredits);


        this.addRenderableWidget(fullBrightWidget);
        this.addRenderableWidget(toggleSneakToggleWidget);
        this.addRenderableWidget(hurtBobWidget);
        this.addRenderableWidget(noFireWidget);
        this.addRenderableWidget(smoothSneakWidget);
        this.addRenderableWidget(openVideoOptions);
        this.addRenderableWidget(backButton);
        this.addRenderableWidget(openGuiEditing);
        this.addRenderableWidget(fpsWidget);
        this.addRenderableWidget(openGithubWidget);
    }

    @Override
    public void onClose() {
        super.onClose();
        OptionUtil.save();
    }

    private void openCredits(@Nullable Button widget) {
        try {
            Util.OS.WINDOWS.openUri(new URI("https://modrinth.com/mod/osmium"));
        } catch (URISyntaxException exception) {
            Osmium.LOGGER.warn("Failed in opening modrinth link. How did this even happen?");
        }
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        // set proper shaders
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, LOGO_TEXTURE);
        // gets the current shader color
        Color shaderColor = Color.fromFloatArray(RenderSystem.getShaderColor());
        RenderSystem.enableBlend();
        // renders osmium logo to screen with fade in
        matrices.pushPose();
        // sets the current shader color to itself, but with a modified alpha for fade in effect
        RenderSystem.setShaderColor(shaderColor.getFloatR(), shaderColor.getFloatG(), shaderColor.getFloatB(), ((animationProgress * 4) - 1) / 255f);
        // scale image down to a good size
        matrices.scale(0.5f, 0.5f, 0.5f);
        // account for scaling difference
        // its width / 2 - 128 because we are scaling by 0.5, and 128 is the scaled dimensions of the image
        matrices.translate(this.width / 2f - 128, animationProgress,0);
        blit(matrices, this.width / 2, 15, 0, 0, 256, 256);
        matrices.popPose();

        matrices.pushPose();
        RenderSystem.setShaderColor(shaderColor.getFloatR(), shaderColor.getFloatG(), shaderColor.getFloatB(), ((animationProgress * 4) - 1) / 255f);
        matrices.translate(0, animationProgress,0);
        drawCenteredString(matrices, mc.font, new TranslatableComponent("osmium.version"), this.width / 2, 140, 0xffffff);
        matrices.popPose();
        // drawTextWithShadow(matrices, mc.textRenderer, new TranslatableText("osmium.github"), 20, this.height - 40, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
        // 57 is the max because of animation progress looking good at 3
        if(!(animationProgress >= 57))
            animationProgress += 3;
        RenderSystem.disableBlend();

    }



}
