package com.intro.client.render.screen;

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


public class OsmiumGeneralUtilityOptionsScreen extends Screen {

    private final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();


    private int globalOffset = 0;
    private int logoOffset = 0;
    private boolean shouldRenderLogo = true;
    private int finalOffset = 0;

    private final ResourceLocation LOGO_TEXTURE = new ResourceLocation("osmium", "icon.png");

    public OsmiumGeneralUtilityOptionsScreen(Screen parent) {
        super(new TranslatableComponent("osmium.options.general_mods"));
        this.parent = parent;
    }


    @Override
    protected void init() {

        // offset because of weird scaling at high gui scales
        if(mc.options.guiScale > 2) {
            logoOffset = -40;
        }
        if(mc.options.guiScale > 4) {
            shouldRenderLogo = false;
            logoOffset = -80;
            globalOffset = -64;
        }

        finalOffset = 57 / mc.options.guiScale;

        BooleanButtonWidget fullBrightWidget = new BooleanButtonWidget(this.width / 2 - 275, this.height / 4 + 80 + globalOffset, 150, 20, Options.FullbrightEnabled, "osmium.options.full_bright_");
        BooleanButtonWidget hurtBobWidget = new BooleanButtonWidget(this.width / 2 - 75, this.height / 4 + 80 + globalOffset, 150, 20, Options.HurtbobbingEnabled, "osmium.options.hurt_bobbing_");
        BooleanButtonWidget noFireWidget = new BooleanButtonWidget(this.width / 2 + 125, this.height / 4 + 80 + globalOffset, 150, 20, Options.NoFireEnabled, "osmium.options.no_fire_");
        EnumSelectWidget smoothSneakWidget = new EnumSelectWidget(this.width / 2 - 275, this.height / 4 + 120 + globalOffset, 150, 20, Options.SneakMode, "osmium.options.sneak_");
        Button backButton = new Button(this.width / 2 - 100, this.height / 4 + 225 + globalOffset, 200, 20, new TranslatableComponent("osmium.options.video_options.back"), (Button) -> mc.setScreen(parent));
        Button toggleSneakToggleWidget = new Button(this.width / 2 - 75, this.height / 4 + 120 + globalOffset, 150, 20, new TranslatableComponent("osmium.options.toggle_sneak_settings"), (Button) -> mc.setScreen(new OsmiumToggleSneakOptionsScreen(this)));


        this.addRenderableWidget(toggleSneakToggleWidget);
        this.addRenderableWidget(fullBrightWidget);
        this.addRenderableWidget(hurtBobWidget);
        this.addRenderableWidget(noFireWidget);
        this.addRenderableWidget(smoothSneakWidget);
        this.addRenderableWidget(backButton);
    }

    @Override
    public void onClose() {
        super.onClose();
        OptionUtil.save();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
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