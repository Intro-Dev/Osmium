package dev.lobstershack.client.render.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.render.color.Color;
import dev.lobstershack.client.render.screen.builder.ScreenBuilder;
import dev.lobstershack.client.render.widget.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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

    // :)
    // happy Easter
    private int easterEggStage = 0;
    private boolean hasDoneEasterEgg = false;
    private final Component easterEggText = dev.lobstershack.client.util.Util.generateRandomEasterEggMessage();

    private final ResourceLocation LOGO_TEXTURE = new ResourceLocation("osmium", "icon.png");

    public OsmiumOptionsScreen(Screen parent) {
        super(Component.translatable("osmium.options.title"));
        this.parent = parent;
    }


    @Override
    protected void init() {

        if(!OsmiumClient.runningLatestVersion && !shownUpdateScreen) {
             shownUpdateScreen = true;
             mc.setScreen(new OsmiumUpdateScreen(this));
        }

        // offset because of weird scaling at high gui scales
        if(mc.options.guiScale().get() > 2) {
            logoOffset = -40;
        }
        if(mc.options.guiScale().get() > 4) {
            shouldRenderLogo = false;
            logoOffset = -80;
            globalOffset = -64;

        }
        bakedMaxAnim = 57 / mc.options.guiScale().get();

        Button openGeneralUtilScreen = new AbstractScalableButton(this.width / 2 - 275, this.height / 4 + 80 + globalOffset, 150, 20, Component.translatable("osmium.options.general_mods"), button -> mc.setScreen(ScreenBuilder.newInstance()
                        .button(Options.FulbrightEnabled, "osmium.options.full_bright_")
                        .button(Options.HurtBobbingEnabled, "osmium.options.hurt_bobbing_")
                        .button(Options.NoFireEnabled, "osmium.options.no_fire_")
                        .button(Options.SneakMode, "osmium.options.sneak_")
                        .button(Options.FreeLookEnabled, "osmium.options.free_look_")
                        .button(Component.translatable("osmium.options.toggle_sneak_settings"), (bT) -> mc.setScreen(ScreenBuilder.newInstance()
                                .button(Options.ToggleSprintEnabled, "osmium.options.toggle_sprint_")
                                .button(Options.ToggleSneakEnabled, "osmium.options.toggle_sneak_", (widget) -> {
                                    // compatibility patch for sneaktweak
                                    if( FabricLoader.getInstance().isModLoaded("sneaktweak")) {
                                        ((Button) widget).active = false;
                                        ((AbstractScalableButton) widget).setTooltip(Component.translatable("osmium.compatibility.sneak_tweak_disable"));
                                    }

                                })
                                .button(Options.FlyBoostEnabled, "osmium.options.fly_boost_")
                                .slider(Options.FlyBoostAmount, "osmium.options.fly_boost_amount", 0, 10, 10)
                                .addBackButton(this)
                                .build(Component.translatable("osmium.options.toggle_sneak_settings"))
                        ))
                        .addBackButton(this)
                        .build(Component.translatable("osmium.options.general_mods")))
                );


        Button openWidgetScreen = new AbstractScalableButton(this.width / 2 - 75, this.height / 4 + 80 + globalOffset, 150, 20, Component.translatable("osmium.options.widgets_screen"), button -> mc.setScreen(ScreenBuilder.newInstance()
                .button(Options.PingDisplayEnabled, "osmium.options.ping_display_")
                .button(Options.CpsDisplayEnabled, "osmium.options.cps_")
                .button(Options.FpsEnabled, "osmium.options.fps_")
                .button(Options.ArmorDisplayEnabled, "osmium.options.armor_display_")
                .button(Component.translatable("osmium.options.keystrokes_settings"), (buttonWidget) -> mc.setScreen(ScreenBuilder.newInstance()
                        .widget(new ColorOptionWidget(mc.getWindow().getGuiScaledWidth() / 2 + 50, mc.getWindow().getScreenHeight() / 4 - 100, 150, Options.KeystrokesColor))
                        .widget(new AbstractScalableButton(mc.getWindow().getGuiScaledWidth() / 2 - 175, mc.getWindow().getScreenHeight() / 4 - 100, 150, 20, Component.translatable("osmium.options.reset_color"), (bW) -> Options.KeystrokesColor.set(new Color(0.1f, 0.1f, 0.1f, 0.2f))))
                        .widget(new BooleanButtonWidget(mc.getWindow().getGuiScaledWidth() / 2 - 175, mc.getWindow().getScreenHeight() / 4 + 20, 150, 20, Options.KeystrokesEnabled, "osmium.options.keystrokes_"))
                        .widget(new BooleanButtonWidget(mc.getWindow().getGuiScaledWidth() / 2 - 175, mc.getWindow().getScreenHeight() / 4 - 20, 150, 20, Options.KeystrokesRgb, "osmium.options.rgb_"))
                        .widget(new DoubleSliderWidget(mc, mc.getWindow().getGuiScaledWidth() / 2 - 175, mc.getWindow().getScreenHeight() / 4 - 60, 150, 20, Options.KeystrokesAlpha, "osmium.options.transparency", 0, 1, 10))
                        .widget(new AbstractScalableButton(mc.getWindow().getGuiScaledWidth() / 2 - 100, mc.getWindow().getScreenHeight() / 4 + 225, 200, 20, Component.translatable("osmium.options.video_options.back"), (bW) -> mc.setScreen(this.parent)))
                        .build(Component.translatable("osmium.options.keystrokes_settings"))
                ))
                .button(Component.translatable("osmium.options.status_effect_display_settings"), (buttonWidget) -> mc.setScreen(ScreenBuilder.newInstance()
                                .button(Options.StatusEffectDisplayMode, "osmium.options.status_effect_display_")
                                .slider(Options.MaxStatusEffectsDisplayed, "osmium.options.max_status_display", 0, 10, 1)
                                .addBackButton(this)
                                .build(Component.translatable("osmium.options.status_effect_display_settings"))))
                .addBackButton(this)
                .build(Component.translatable("osmium.options.widgets_screen"))
        ));
        Button openVideoOptions = new AbstractScalableButton(this.width / 2 + 125, this.height / 4 + 80 + globalOffset, 150, 20, Component.translatable("osmium.options.video_options"), (Button) -> mc.setScreen(ScreenBuilder.newInstance()
                .button(Component.translatable("osmium.cape_options"), button -> mc.setScreen(new OsmiumCapeOptionsScreen(this)), btn -> {
                    if(mc.level == null) {
                        ((AbstractScalableButton) btn).active = false;
                        ((AbstractScalableButton) btn).setTooltip(Component.translatable("osmium.options.cape_screen_level_only"));
                    }
                })
                .button(Options.NoRainEnabled, "osmium.options.rain_")
                .button(Options.FireworksDisabled, "osmium.options.fireworks_")
                .button(Options.DecreaseNetherParticles, "osmium.options.nether_particles_")
                .button(Component.translatable("osmium.options.block_option_settings"), (buttonWidget) -> mc.setScreen(ScreenBuilder.newInstance()
                        .widget(new AbstractScalableButton(mc.getWindow().getGuiScaledWidth() / 2 - 100, mc.getWindow().getScreenHeight() / 4 + 225, 200, 20, Component.translatable("osmium.options.video_options.back"), (bW) -> mc.setScreen(this.parent)))
                        .widget(new ColorOptionWidget(mc.getWindow().getGuiScaledWidth() / 2 + 50, mc.getWindow().getScreenHeight() / 4 - 100, 150, Options.BlockOutlineColor))
                        .widget(new EnumSelectWidget(mc.getWindow().getGuiScaledWidth() / 2 - 175, mc.getWindow().getScreenHeight() / 4 - 100, 150, 20, Options.BlockOutlineMode, "osmium.options.overlay_"))
                        .widget(new DoubleSliderWidget(mc, mc.getWindow().getGuiScaledWidth() / 2 - 175, mc.getWindow().getScreenHeight() / 4 - 60, 150, 20, Options.BlockOutlineAlpha, "osmium.options.block_overlay_alpha", 0, 1, 10))
                        .build(Component.translatable("osmium.options.block_option_settings"))
                ))
                .addBackButton(this)
                .build(Component.translatable("osmium.options.video_options.title"))
        ));



        AbstractScalableButton openGuiEditing = new AbstractScalableButton(this.width / 2 - 275, this.height / 4 + 120 + globalOffset, 150, 20, Component.translatable("osmium.gui_edit.title"), (Button) -> mc.setScreen(new OsmiumGuiEditScreen(this)), 1f);

        Button openHypixelScreen = new AbstractScalableButton(this.width / 2+ 125, this.height / 4 + 120 + globalOffset, 150, 20, Component.translatable("osmium.options.hypixel_mods"), (Button) -> mc.setScreen(new OsmiumHypixelModsScreen(this)));

        Button backButton = new AbstractScalableButton(this.width / 2 - 100, this.height / 4 + 225 + globalOffset, 200, 20, Component.translatable("osmium.options.video_options.back"), (Button) -> {
            mc.setScreen(parent);
            Options.save();
        });
        Button openModrinthWidget = new AbstractScalableButton(this.width / 2 - 75, this.height / 4 + 120 + globalOffset, 150, 20, Component.translatable("osmium.open_credits"), this::openCredits);

        if(mc.level == null) {
            openGuiEditing.active = false;
            openGuiEditing.setTooltip(Component.translatable("osmium.options.gui_screen_level_only"));
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
        Options.save();
    }

    private void openCredits(@Nullable Button widget) {
        try {
            Util.getPlatform().openUri(new URI("https://modrinth.com/mod/osmium"));
        } catch (URISyntaxException exception) {
            OsmiumClient.LOGGER.warn("Failed in opening modrinth link. How did this even happen?");
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        PoseStack stack = graphics.pose();
        this.renderBackground(graphics);
        // set proper shaders
        // gets the current shader color
        Color shaderColor = Color.fromFloatArray(RenderSystem.getShaderColor());
        RenderSystem.enableBlend();
        // renders osmium logo to screen with fade in
        stack.pushPose();
        // sets the current shader color to itself, but with a modified alpha for fade in effect
        float floatColor = Mth.clamp((animationProgress * 4) - 1 / 255f, 0, 1);
        RenderSystem.setShaderColor(shaderColor.getFloatR(), shaderColor.getFloatG(), shaderColor.getFloatB(), floatColor);
        // scale image down to a good size
        stack.scale(0.5f, 0.5f, 0.5f);
        // account for scaling difference
        // its width / 2 - 128 because we are scaling by 0.5, and 128 is the scaled dimensions of the image
        stack.translate(this.width / 2f - 128, animationProgress,0);
        if(shouldRenderLogo)
            graphics.blit(LOGO_TEXTURE, this.width / 2, this.height / 8 + globalOffset + logoOffset, 0, 0, 256, 256);
        stack.popPose();

        stack.pushPose();
        // RenderSystem.setShaderColor(shaderColor.getFloatR(), shaderColor.getFloatG(), shaderColor.getFloatB(), ((animationProgress * 4) - 1) / 255f);
        stack.translate(0, animationProgress,0);

        graphics.drawCenteredString(mc.font, hasDoneEasterEgg ? easterEggText : Component.translatable("osmium.version"), this.width / 2, this.height / 8 + 100 + globalOffset + (logoOffset / 4), 0xffffff);
        stack.popPose();
        super.render(graphics, mouseX, mouseY, delta);
        // 57 is the max because of animation progress looking good at 3
        animationProgress += 10 * delta;
        animationProgress = Mth.clamp(animationProgress, 0, bakedMaxAnim);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == InputConstants.KEY_UP && easterEggStage == 0 || easterEggStage == 1) {
            easterEggStage++;
        } else if(keyCode == InputConstants.KEY_DOWN && (easterEggStage == 2 || easterEggStage == 3)) {
            easterEggStage++;
        } else if(keyCode == InputConstants.KEY_LEFT && (easterEggStage == 4 || easterEggStage == 6)) {
            easterEggStage++;
        } else if(keyCode == InputConstants.KEY_RIGHT && (easterEggStage == 5 || easterEggStage == 7)) {
            easterEggStage++;
        } else if(keyCode == InputConstants.KEY_B && easterEggStage == 8) {
            easterEggStage++;
        } else if(keyCode == InputConstants.KEY_A && easterEggStage == 9) {
            easterEggStage++;
        } else if(keyCode == InputConstants.KEY_RETURN && easterEggStage == 10) {
            easterEggStage++;
        } else {
            easterEggStage = 0;
        }

        if(easterEggStage == 11) {
            hasDoneEasterEgg = true;
        }


        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
