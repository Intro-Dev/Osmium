package com.intro.client.render.screen;

import com.intro.client.render.widget.BooleanButtonWidget;
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
import net.minecraft.util.Mth;

public class OsmiumWidgetsScreen extends Screen {

    private final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();

    private int globalOffset = 0;
    private int logoOffset = 0;
    private boolean shouldRenderLogo = true;
    private int finalOffset = 0;

    private final ResourceLocation LOGO_TEXTURE = new ResourceLocation("osmium", "icon.png");


    public OsmiumWidgetsScreen(Screen parent) {
        super(new TranslatableComponent("osmium.options.widgets_screen"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button backButton = new Button(this.width / 2 - 100, this.height / 4 + 220, 200, 20, new TranslatableComponent("osmium.options.video_options.back"), (buttonWidget) -> mc.setScreen(this.parent));


        // offset because of weird scaling at high gui scales
        if(mc.options.guiScale > 2) {
            logoOffset = -40;
        }
        if(mc.options.guiScale > 4) {
            shouldRenderLogo = false;
            logoOffset = -80;
            globalOffset = -64;
        }

        // this wonderful bit of code is very inefficient
        // but, it's only called on init, and it makes a very annoying rendering offset bug go away
        // so who's laughing
        while(!(finalOffset > (57 / mc.options.guiScale))) {
            finalOffset = Mth.clamp(finalOffset, 0, 57 / mc.options.guiScale);
            finalOffset += 3;
        }

        BooleanButtonWidget pingDisplayEnabled = new BooleanButtonWidget(this.width / 2 - 275, this.height / 4 + 80 + globalOffset, 150, 20, Options.PingDisplayEnabled, "osmium.options.ping_display_");
        BooleanButtonWidget cpsDisplayEnabled = new BooleanButtonWidget(this.width / 2 - 75, this.height / 4 + 80 + globalOffset, 150, 20, Options.CpsDisplayEnabled, "osmium.options.cps_");
        BooleanButtonWidget fpsDisplayEnabled = new BooleanButtonWidget(this.width / 2 + 125, this.height / 4 + 80 + globalOffset, 150, 20, Options.FpsEnabled, "osmium.options.fps_");
        BooleanButtonWidget armorDisplayEnabled = new BooleanButtonWidget(this.width / 2 - 275, this.height / 4 + 120 + globalOffset, 150, 20, Options.ArmorDisplayEnabled, "osmium.options.armor_display_");
        Button openKeystrokesWidget = new Button(this.width / 2 - 75, this.height / 4 + 120 + globalOffset, 150, 20, new TranslatableComponent("osmium.options.keystrokes_settings"), button -> mc.setScreen(new OsmiumKeystrokesScreen(this)));
        Button statusEffectScreenButton = new Button(this.width / 2 + 125, this.height / 4 + 120 + globalOffset, 150, 20, new TranslatableComponent("osmium.options.status_effect_display_settings"), (buttonWidget) -> mc.setScreen(new OsmiumStatusEffectDisplayOptionsScreen(this)));

        this.addRenderableWidget(statusEffectScreenButton);
        this.addRenderableWidget(openKeystrokesWidget);
        this.addRenderableWidget(pingDisplayEnabled);
        this.addRenderableWidget(cpsDisplayEnabled);
        this.addRenderableWidget(fpsDisplayEnabled);
        this.addRenderableWidget(armorDisplayEnabled);
        this.addRenderableWidget(backButton);


    }

    @Override
    public void onClose() {
        super.onClose();
        OptionUtil.save();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        // haha magic number go brrrr
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
        drawCenteredString(matrices, mc.font, new TranslatableComponent("osmium.version"), this.width / 2, this.height / 8 + 100 + globalOffset + (logoOffset / 4), 0xffffff);
        matrices.popPose();
        super.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableBlend();
    }
}
