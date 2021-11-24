package com.intro.client.render.screen;

import com.intro.client.OsmiumClient;
import com.intro.client.render.widget.BooleanButtonWidget;
import com.intro.client.util.HypixelAbstractionLayer;
import com.intro.client.util.OptionUtil;
import com.intro.common.config.Options;
import com.intro.common.config.options.StringOption;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;


public class OsmiumHypixelModsScreen extends Screen {

    private final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();


    private int globalOffset = 0;
    private int logoOffset = 0;
    private boolean shouldRenderLogo = true;
    private int finalOffset = 0;

    private final ResourceLocation LOGO_TEXTURE = new ResourceLocation("osmium", "icon.png");

    public OsmiumHypixelModsScreen(Screen parent) {
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


        BooleanButtonWidget levelHeadToggle = new BooleanButtonWidget(this.width / 2 - 275, this.height / 4 + 80 + globalOffset, 150, 20, Options.LevelHeadEnabled, "osmium.options.level_head_");
        BooleanButtonWidget autoGGToggle = new BooleanButtonWidget(this.width / 2 - 75, this.height / 4 + 80 + globalOffset, 150, 20, Options.AutoGGEnabled, "osmium.options.auto_gg_");

        // mojang really has produced trash here: the suggestion in EditBoxes only goes away when the MaxLength is reached
        EditBox autoggEnterBox = new EditBox(mc.font, this.width / 2 - 2, this.height / 4 + 105 + globalOffset, 75, 20, new TranslatableComponent("osmium.options.auto_gg_string"));
        // Prohibit log/chat spam with this feature
        autoggEnterBox.setMaxLength(24);
        autoggEnterBox.setValue(OsmiumClient.options.getStringOption(Options.AutoGGString).variable);
        // as below with the API key, it isn't provided to the user directly
        autoggEnterBox.setResponder((string) -> {
            OsmiumClient.options.put(Options.AutoGGString, new StringOption(Options.AutoGGString, autoggEnterBox.getValue()));
        });

        // Here this isn't as important as Hypixel's API keys are always 36 characters long
        EditBox apiEnterBox = new EditBox(mc.font, this.width / 2 - 100, this.height / 4 + 175 + globalOffset, 200, 20, new TranslatableComponent("osmium.options.api_key"));
        // max uuid length is 36
        apiEnterBox.setMaxLength(36);
        apiEnterBox.setSuggestion("Enter Hypixel Api Key");
        apiEnterBox.setValue(OsmiumClient.options.getStringOption(Options.HypixelApiKey).variable);
        // doesn't actually provide the string to the consumer
        // thanks mojang
        apiEnterBox.setResponder((string) -> {
            OsmiumClient.options.put(Options.HypixelApiKey, new StringOption(Options.HypixelApiKey, apiEnterBox.getValue()));
            try {
                HypixelAbstractionLayer.loadApiKey();
            } catch (IllegalArgumentException ignored) {

            }
        });
        Button backButton = new Button(this.width / 2 - 100, this.height / 4 + 225 + globalOffset, 200, 20, new TranslatableComponent("osmium.options.video_options.back"), (Button) -> mc.setScreen(parent));


        this.addRenderableWidget(levelHeadToggle);
        this.addRenderableWidget(autoGGToggle);
        this.addRenderableWidget(autoggEnterBox);
        this.addRenderableWidget(apiEnterBox);
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

        matrices.pushPose();
        matrices.translate(0, finalOffset,0);
        drawString(matrices, mc.font, "GG String: ", this.width / 2 - 73, this.height / 4 + 85 + globalOffset + (logoOffset / 4), 0xffffff);
        matrices.popPose();
        super.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableBlend();
    }

}
