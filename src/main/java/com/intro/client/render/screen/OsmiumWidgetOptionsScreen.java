package com.intro.client.render.screen;

import com.intro.client.render.screen.builder.ScreenBuilder;
import com.intro.client.render.widget.BooleanButtonWidget;
import com.intro.common.config.Options;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class OsmiumWidgetOptionsScreen extends Screen {

    private final Screen parent;

    private final ResourceLocation LOGO_TEXTURE = new ResourceLocation("osmium", "icon.png");

    private final Minecraft mc = Minecraft.getInstance();

    private int globalOffset = 0;
    private int logoOffset = 0;
    private boolean shouldRenderLogo = true;
    private int finalOffset = 0;

    protected OsmiumWidgetOptionsScreen(Screen parent) {
        super(Component.translatable("osmium.options.widget_options"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        // offset because of weird scaling at high gui scales
        if(mc.options.guiScale().get() > 2) {
            logoOffset = -40;
        }
        if(mc.options.guiScale().get() > 4) {
            shouldRenderLogo = false;
            logoOffset = -80;
            globalOffset = -64;

        }
        finalOffset = 57 / mc.options.guiScale().get();

        ScreenBuilder.newInstance()
                .button(Options.PingDisplayEnabled, "osmium.options.ping_display_")
                .button(Options.CpsDisplayEnabled, "osmium.options.cps_")
                .button(Options.FpsEnabled, "osmium.options.fps_")
                .button(Options.ArmorDisplayEnabled, "osmium.options.armor_display_")
                .button(Component.translatable("osmium.options.keystrokes_settings"), (buttonWidget) -> mc.setScreen(new OsmiumKeystrokesScreen(this)))
                .button(Component.translatable("osmium.options.status_effect_display_settings"), (buttonWidget) -> mc.setScreen(new OsmiumStatusEffectDisplayOptionsScreen(this)))
                .addBackButton(this)
                .build(Component.translatable("osmium.options.widgets_screen"));

        BooleanButtonWidget pingDisplayToggle = new BooleanButtonWidget(this.width / 2 -275, this.height / 4 + 80 + globalOffset, 150, 20, Options.PingDisplayEnabled, "osmium.options.ping_display_");
        BooleanButtonWidget cpsDisplayToggle = new BooleanButtonWidget(this.width / 2 -75, this.height / 4 + 80 + globalOffset, 150, 20, Options.CpsDisplayEnabled, "osmium.options.cps_");
        BooleanButtonWidget fpsDisplayToggle = new BooleanButtonWidget(this.width / 2 + 125, this.height / 4 + 80 + globalOffset, 150, 20, Options.FpsEnabled, "osmium.options.fps_");
        BooleanButtonWidget armorDisplayToggle = new BooleanButtonWidget(this.width / 2 - 275, this.height / 4 + 120 + globalOffset, 150, 20, Options.ArmorDisplayEnabled, "osmium.options.armor_display_");


        Button keystrokesOptionsButton = new Button(this.width / 2 - 75, this.height / 4 + 120 + globalOffset, 150, 20, Component.translatable("osmium.options.keystrokes_settings"), (buttonWidget) -> mc.setScreen(new OsmiumKeystrokesScreen(this)));
        Button statusEffectOptionsButton = new Button(this.width / 2 + 125, this.height / 4 + 120 + globalOffset, 150, 20, Component.translatable("osmium.options.status_effect_display_settings"), (buttonWidget) -> mc.setScreen(new OsmiumStatusEffectDisplayOptionsScreen(this)));

        Button backButton = new Button(this.width / 2 - 100, this.height / 4 + 225 + globalOffset, 200, 20, Component.translatable("osmium.options.video_options.back"), (Button) -> mc.setScreen(parent));

        this.addRenderableWidget(fpsDisplayToggle);
        this.addRenderableWidget(cpsDisplayToggle);
        this.addRenderableWidget(pingDisplayToggle);
        this.addRenderableWidget(armorDisplayToggle);
        this.addRenderableWidget(keystrokesOptionsButton);
        this.addRenderableWidget(statusEffectOptionsButton);
        this.addRenderableWidget(backButton);

    }

    public void render(@NotNull PoseStack matrices, int mouseX, int mouseY, float delta){
        this.renderBackground(matrices);
        // set proper shaders
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, LOGO_TEXTURE);
        RenderSystem.enableBlend();
        matrices.pushPose();
        // scale image down to a good size
        matrices.scale(0.5f, 0.5f, 0.5f);
        // account for scaling difference
        // its width / 2 - 128 because we are scaling by 0.5, and 128 is the scaled dimensions of the image
        matrices.translate(this.width / 2f - 128, finalOffset,0);
        if(shouldRenderLogo)
            blit(matrices, this.width / 2, this.height / 8 + globalOffset + logoOffset, 0, 0, 256, 256);
        matrices.popPose();

        matrices.pushPose();
        matrices.translate(0, finalOffset,0);
        drawCenteredString(matrices, mc.font, Component.translatable("osmium.version"), this.width / 2, this.height / 8 + 100 + globalOffset + (logoOffset / 4), 0xffffff);
        matrices.popPose();

        super.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableBlend();

    }
}
