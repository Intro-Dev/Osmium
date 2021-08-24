package com.intro.client.render.screen;

import com.intro.client.render.Color;
import com.intro.client.render.widget.BooleanButtonWidget;
import com.intro.client.render.widget.EnumSelectWidget;
import com.intro.client.util.OptionUtil;
import com.intro.common.config.Options;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class OsmiumVideoOptionsScreen extends Screen {

    private final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();

    private final ResourceLocation LOGO_TEXTURE = new ResourceLocation("osmium", "icon.png");

    public OsmiumVideoOptionsScreen(Screen parent) {
        super(new TranslatableComponent("osmium.options.video_options.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button backButton = new Button(this.width / 2 - 100, this.height / 6 + 300, 200, 20, new TranslatableComponent("osmium.options.video_options.back"), (buttonWidget) -> mc.setScreen(this.parent));

        Button blockOptionScreenButton = new Button(this.width / 2 - 75, this.height / 6 + 160, 150, 20, new TranslatableComponent("osmium.options.block_option_settings"), (buttonWidget) -> mc.setScreen(new OsmiumBlockOptionsScreen(this)));

        EnumSelectWidget toggleCapeWidget = new EnumSelectWidget(this.width / 2 - 275, this.height / 6 + 120, 150, 20, Options.CustomCapeMode,"osmium.options.video_options.cape_");
        BooleanButtonWidget toggleRainWidget = new BooleanButtonWidget(this.width / 2 - 75, this.height / 6 + 120, 150, 20, Options.NoRainEnabled, "osmium.options.rain_");
        BooleanButtonWidget toggleFireworksWidget = new BooleanButtonWidget(this.width / 2 + 125, this.height / 6 + 120, 150, 20, Options.FireworksDisabled, "osmium.options.fireworks_");
        BooleanButtonWidget toggleNetherParticlesWidget = new BooleanButtonWidget(this.width / 2 - 275, this.height / 6 + 160, 150, 20, Options.DecreaseNetherParticles, "osmium.options.nether_particles_");

        Button statusEffectScreenButton = new Button(this.width / 2 + 125, this.height / 6 + 160, 150, 20, new TranslatableComponent("osmium.options.status_effect_display_settings"), (buttonWidget) -> mc.setScreen(new OsmiumStatusEffectDisplayOptionsScreen(this)));
        BooleanButtonWidget armorDisplayToggleButton = new BooleanButtonWidget(this.width / 2 - 275, this.height / 6 + 200, 150, 20, Options.ArmorDisplayEnabled, "osmium.options.armor_display_");

        this.addRenderableWidget(backButton);
        this.addRenderableWidget(toggleCapeWidget);
        this.addRenderableWidget(toggleRainWidget);
        this.addRenderableWidget(toggleFireworksWidget);
        this.addRenderableWidget(toggleNetherParticlesWidget);
        this.addRenderableWidget(blockOptionScreenButton);
        this.addRenderableWidget(statusEffectScreenButton);
        this.addRenderableWidget(armorDisplayToggleButton);
    }

    @Override
    public void onClose() {
        OptionUtil.save();
        super.onClose();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, LOGO_TEXTURE);

        Color shaderColor = Color.fromFloatArray(RenderSystem.getShaderColor());

        RenderSystem.enableBlend();
        // renders osmium logo to screen with fade in
        matrices.pushPose();
        // sets the current shader color to itself, but with a modified alpha for fade in effect
        // 0.8901961 because that's the final result for the fade in animation on the main settings page
        RenderSystem.setShaderColor(shaderColor.getFloatR(), shaderColor.getFloatG(), shaderColor.getFloatB(), 0.8901961f);
        // scale image down to a good size
        matrices.scale(0.5f, 0.5f, 0.5f);
        // account for scaling difference
        // its width / 2 - 128 because we are scaling by 0.5, and 128 is the scaled dimensions of the image
        matrices.translate(this.width / 2f - 128, 57,0);
        blit(matrices, this.width / 2, 15, 0, 0, 256, 256);
        matrices.popPose();
        matrices.pushPose();
        matrices.translate(0, 57,0);
        drawCenteredString(matrices, mc.font, new TranslatableComponent("osmium.version"), this.width / 2, 140, 0xffffff);
        matrices.popPose();
        RenderSystem.disableBlend();
        super.render(matrices, mouseX, mouseY, delta);
    }
}
