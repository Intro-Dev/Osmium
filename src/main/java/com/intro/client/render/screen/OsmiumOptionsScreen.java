package com.intro.client.render.screen;

import com.intro.client.OsmiumClient;
import com.intro.client.render.color.Color;
import com.intro.client.render.screen.builder.ScreenBuilder;
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

    private float animationProgress = 0;

    private final Minecraft mc = Minecraft.getInstance();

    private int globalOffset = 0;
    private int logoOffset = 0;
    private boolean shouldRenderLogo = true;
    private int bakedMaxAnim = 0;

    private final ResourceLocation LOGO_TEXTURE = new ResourceLocation("osmium", "icon.png");

    public OsmiumOptionsScreen(Screen parent) {
        super(new TranslatableComponent("osmium.options.title"));
        this.parent = parent;
    }


    @Override
    protected void init() {

        if(!OsmiumClient.runningLatestVersion && !shownUpdateScreen) {
             shownUpdateScreen = true;
             mc.setScreen(new OsmiumUpdateScreen(this));
        }

        // offset because of weird scaling at high gui scales
        if(mc.options.guiScale > 2) {
            logoOffset = -40;
        }
        if(mc.options.guiScale > 4) {
            shouldRenderLogo = false;
            logoOffset = -80;
            globalOffset = -64;

        }
        bakedMaxAnim = 57 / mc.options.guiScale;

        Button openGeneralUtilScreen = new Button(this.width / 2 - 275, this.height / 4 + 80 + globalOffset, 150, 20, new TranslatableComponent("osmium.options.general_mods"), button -> mc.setScreen(ScreenBuilder.newInstance()
                        .button(Options.FullbrightEnabled, "osmium.options.full_bright_")
                        .button(Options.HurtbobbingEnabled, "osmium.options.hurt_bobbing_")
                        .button(Options.NoFireEnabled, "osmium.options.no_fire_")
                        .button(Options.SneakMode, "osmium.options.sneak_")
                        .button(new TranslatableComponent("osmium.options.toggle_sneak_settings"), (Button) -> mc.setScreen(ScreenBuilder.newInstance()
                                .button(Options.ToggleSprintEnabled, "osmium.options.toggle_sprint_")
                                .button(Options.ToggleSneakEnabled, "osmium.options.toggle_sneak_")
                                .button(Options.FlyBoostEnabled, "osmium.options.fly_boost_")
                                .slider(Options.FlyBoostAmount, "osmium.options.fly_boost_amount", 0, 10, 10)
                                .addBackButton(this)
                                .build(new TranslatableComponent("osmium.options.toggle_sneak_settings"))
                        ))
                        .addBackButton(this)
                        .build(new TranslatableComponent("osmium.options.general_mods")))
                );
        Button openWidgetScreen = new Button(this.width / 2 - 75, this.height / 4 + 80 + globalOffset, 150, 20, new TranslatableComponent("osmium.options.widgets_screen"), button -> mc.setScreen(new OsmiumWidgetOptionsScreen(this)));

        Button openVideoOptions = new Button(this.width / 2 + 125, this.height / 4 + 80 + globalOffset, 150, 20, new TranslatableComponent("osmium.options.video_options"), (Button) -> mc.setScreen(new OsmiumVideoOptionsScreen(this)));

        Button openGuiEditing = new Button(this.width / 2 - 275, this.height / 4 + 120 + globalOffset, 150, 20, new TranslatableComponent("osmium.gui_edit.title"), (Button) -> mc.setScreen(new OsmiumGuiEditScreen(this)));

        Button openHypixelScreen = new Button(this.width / 2+ 125, this.height / 4 + 120 + globalOffset, 150, 20, new TranslatableComponent("osmium.options.hypixel_mods"), (Button) -> mc.setScreen(new OsmiumHypixelModsScreen(this)));

        Button backButton = new Button(this.width / 2 - 100, this.height / 4 + 225 + globalOffset, 200, 20, new TranslatableComponent("osmium.options.video_options.back"), (Button) -> mc.setScreen(parent));
        Button openModrinthWidget = new Button(this.width / 2 - 75, this.height / 4 + 120 + globalOffset, 150, 20, new TranslatableComponent("osmium.open_credits"), this::openCredits);

        if(mc.level == null) {
            openGuiEditing.active = false;
        }


        this.addRenderableWidget(openHypixelScreen);
        this.addRenderableWidget(openGeneralUtilScreen);
        this.addRenderableWidget(openVideoOptions);
        this.addRenderableWidget(backButton);
        this.addRenderableWidget(openGuiEditing);
        this.addRenderableWidget(openWidgetScreen);
        this.addRenderableWidget(openModrinthWidget);
    }

    @Override
    public void onClose() {
        super.onClose();
        OptionUtil.save();
    }

    private void openCredits(@Nullable Button widget) {
        try {
            Util.getPlatform().openUri(new URI("https://modrinth.com/mod/osmium"));
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
        drawCenteredString(matrices, mc.font, new TranslatableComponent("osmium.version"), this.width / 2, this.height / 8 + 100 + globalOffset + (logoOffset / 4), 0xffffff);
        matrices.popPose();
        super.render(matrices, mouseX, mouseY, delta);
        // 57 is the max because of animation progress looking good at 3
        animationProgress += 10 * delta;
        animationProgress = Mth.clamp(animationProgress, 0, bakedMaxAnim);
        RenderSystem.disableBlend();
    }



}
