package com.intro.client.render.screen;

import com.intro.client.OsmiumClient;
import com.intro.client.render.Color;
import com.intro.client.render.widget.BooleanButtonWidget;
import com.intro.client.render.widget.EnumSelectWidget;
import com.intro.client.util.OptionUtil;
import com.intro.common.config.Options;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;


public class OsmiumOptionsScreen extends Screen {

    private static boolean shownUpdateScreen = false;

    private final Screen parent;

    private int animationProgress = 0;

    private int globalOffset = 0;
    private int logoOffset = 0;
    private boolean shouldRenderLogo = true;


    private final ResourceLocation LOGO_TEXTURE = new ResourceLocation("osmium", "icon.png");

    public OsmiumOptionsScreen(Screen parent) {
        super(new TranslatableComponent("osmium.options.title"));
        this.parent = parent;
    }

    private final Minecraft mc = Minecraft.getInstance();


    @Override
    protected void init() {

        if(!OsmiumClient.runningLatestVersion && !shownUpdateScreen) {
             shownUpdateScreen = true;
             mc.setScreen(new OsmiumUpdateScreen(this));
        }

        // offset because of weird scaling at high gui scales
        if(mc.options.guiScale > 4) {
            globalOffset = -64;
        }
        if(mc.options.guiScale > 2) {
            logoOffset = -40;
        }
        if(mc.options.guiScale > 4) {
            shouldRenderLogo = false;
        }

        BooleanButtonWidget fullBrightWidget = new BooleanButtonWidget(this.width / 2 - 275, this.height / 4 + 80 + globalOffset, 150, 20, Options.FullbrightEnabled, "osmium.options.full_bright_");
        BooleanButtonWidget hurtBobWidget = new BooleanButtonWidget(this.width / 2 + 125, this.height / 4 + 80 + globalOffset, 150, 20, Options.HurtbobbingEnabled, "osmium.options.hurt_bobbing_");
        BooleanButtonWidget noFireWidget = new BooleanButtonWidget(this.width / 2 - 275, this.height / 4 + 120 + globalOffset, 150, 20, Options.NoFireEnabled, "osmium.options.no_fire_");
        BooleanButtonWidget fpsWidget = new BooleanButtonWidget(this.width / 2 - 75, this.height / 4 + 160 + globalOffset, 150, 20, Options.FpsEnabled, "osmium.options.fps_");
        EnumSelectWidget smoothSneakWidget = new EnumSelectWidget(this.width / 2 + 125, this.height / 4 + 120 + globalOffset, 150, 20, Options.SneakMode, "osmium.options.sneak_");

        Button openVideoOptions = new Button(this.width / 2 - 75, this.height / 4 + 120 + globalOffset, 150, 20, new TranslatableComponent("osmium.options.video_options"), (Button) -> mc.setScreen(new OsmiumVideoOptionsScreen(this)));

        Button backButton = new Button(this.width / 2 - 100, this.height / 4 + 225 + globalOffset, 200, 20, new TranslatableComponent("osmium.options.video_options.back"), (Button) -> mc.setScreen(parent));

        Button openGuiEditing = new Button(this.width / 2 - 275, this.height / 4 + 160 + globalOffset, 150, 20, new TranslatableComponent("osmium.gui_edit.title"), (Button) -> mc.setScreen(new OsmiumGuiEditScreen()));

        if(mc.level == null) {
            openGuiEditing.active = false;
        }

        Button toggleSneakToggleWidget = new Button(this.width / 2 - 75, this.height / 4 + 80 + globalOffset, 150, 20, new TranslatableComponent("osmium.options.toggle_sneak_settings"), (Button) -> mc.setScreen(new OsmiumToggleSneakOptionsScreen(this)));

        Button openGithubWidget = new Button(this.width / 2 + 125, this.height / 4 + 160 + globalOffset, 150, 20, new TranslatableComponent("osmium.open_credits"), this::openCredits);


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
            OsmiumClient.LOGGER.warn("Failed in opening modrinth link. How did this even happen?");
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
        float floatColor = Mth.clamp((animationProgress * 4) - 1 / 255f, 0, 1);
        RenderSystem.setShaderColor(shaderColor.getFloatR(), shaderColor.getFloatG(), shaderColor.getFloatB(), floatColor);
        // scale image down to a good size
        matrices.scale(0.5f, 0.5f, 0.5f);
        // account for scaling difference
        // its width / 2 - 128 because we are scaling by 0.5, and 128 is the scaled dimensions of the image
        matrices.translate(this.width / 2f - 128, animationProgress,0);
        if(shouldRenderLogo)
            blit(matrices, this.width / 2, this.height / 8 + globalOffset + logoOffset, 0, 0, 256, 256);
        matrices.popPose();

        matrices.pushPose();
        RenderSystem.setShaderColor(shaderColor.getFloatR(), shaderColor.getFloatG(), shaderColor.getFloatB(), ((animationProgress * 4) - 1) / 255f);
        matrices.translate(0, animationProgress,0);
        drawCenteredString(matrices, mc.font, new TranslatableComponent("osmium.version"), this.width / 2, this.height / 8 + 100 + globalOffset + (logoOffset / 2), 0xffffff);
        matrices.popPose();
        super.render(matrices, mouseX, mouseY, delta);
        // 57 is the max because of animation progress looking good at 3
        if(!(animationProgress >= 57 / mc.options.guiScale)) {
            animationProgress = Mth.clamp(animationProgress, 0, 57);
            animationProgress += 3;
        }
        RenderSystem.disableBlend();
        System.out.println(animationProgress);
    }



}
