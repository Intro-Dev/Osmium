package dev.lobstershack.client.render.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.render.widget.AbstractScalableButton;
import dev.lobstershack.client.render.widget.BooleanButtonWidget;
import dev.lobstershack.client.render.widget.EnumSelectWidget;
import dev.lobstershack.client.util.HypixelAbstractionLayer;
import dev.lobstershack.client.config.Options;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
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
        super(Component.translatable("osmium.options.general_mods"));
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


        BooleanButtonWidget levelHeadToggle = new BooleanButtonWidget(this.width / 2 - 275, this.height / 4 + 80 + globalOffset, 150, 20, Options.LevelHeadEnabled, "osmium.options.level_head_");
        EnumSelectWidget levelHeadModeToggle = new EnumSelectWidget(this.width/2 - 275, this.height/4 + 105 + globalOffset, 150, 20, Options.LevelHeadMode, "osmium.options.level_head_mode_");

        BooleanButtonWidget autoGGToggle = new BooleanButtonWidget(this.width / 2 - 75, this.height / 4 + 80 + globalOffset, 150, 20, Options.AutoGGEnabled, "osmium.options.auto_gg_");

        // mojang really has produced trash here: the suggestion in EditBoxes only goes away when the MaxLength is reached
        EditBox autoggEnterBox = new EditBox(mc.font, this.width / 2 - 2, this.height / 4 + 105 + globalOffset, 75, 20, Component.translatable("osmium.options.auto_gg_string"));
        // Prohibit log/chat spam with this feature
        autoggEnterBox.setMaxLength(24);
        autoggEnterBox.setValue(Options.AutoGGString.get());
        // as below with the API key, it isn't provided to the user directly
        autoggEnterBox.setResponder((string) -> {
            Options.AutoGGString.set(autoggEnterBox.getValue());
        });


        EditBox apiEnterBox = new EditBox(mc.font, this.width / 2 - 100, this.height / 4 + 175 + globalOffset, 200, 20, Component.translatable("osmium.options.api_key"));
        // max uuid length is 36
        apiEnterBox.setMaxLength(36);
        apiEnterBox.setSuggestion("Enter Hypixel Api Key");
        apiEnterBox.setValue(Options.HypixelApiKey.get());
        // doesn't actually provide the string to the consumer
        // thanks mojang
        apiEnterBox.setResponder((string) -> {
            Options.HypixelApiKey.set(apiEnterBox.getValue());
            HypixelAbstractionLayer.loadApiKey();
        });
        Button backButton = new AbstractScalableButton(this.width / 2 - 100, this.height / 4 + 225 + globalOffset, 200, 20, Component.translatable("osmium.options.video_options.back"), (Button) -> mc.setScreen(parent));


        this.addRenderableWidget(levelHeadToggle);
        this.addRenderableWidget(levelHeadModeToggle);
        this.addRenderableWidget(autoGGToggle);
        this.addRenderableWidget(autoggEnterBox);
        this.addRenderableWidget(apiEnterBox);
        this.addRenderableWidget(backButton);
    }

    @Override
    public void onClose() {
        super.onClose();
        Options.save();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        PoseStack stack = graphics.pose();
        this.renderBackground(graphics);
        // set proper shaders
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, LOGO_TEXTURE);
        RenderSystem.enableBlend();
        stack.pushPose();
        // scale image down to a good size
        stack.scale(0.5f, 0.5f, 0.5f);
        // account for scaling difference
        // its width / 2 - 128 because we are scaling by 0.5, and 128 is the scaled dimensions of the image
        stack.translate(this.width / 2f - 128, finalOffset,0);
        if(shouldRenderLogo)
            graphics.blit(LOGO_TEXTURE, this.width / 2, this.height / 8 + globalOffset + logoOffset, 0, 0, 256, 256);
        stack.popPose();

        stack.pushPose();
        stack.translate(0, finalOffset,0);
        graphics.drawCenteredString(mc.font, Component.translatable("osmium.version"), this.width / 2, this.height / 8 + 100 + globalOffset + (logoOffset / 4), 0xffffff);
        stack.popPose();

        stack.pushPose();
        stack.translate(0, finalOffset,0);
        graphics.drawString(mc.font, "GG String: ", this.width / 2 - 73, this.height / 4 + 85 + globalOffset + (logoOffset / 4), 0xffffff);
        stack.popPose();
        RenderSystem.disableBlend();
        super.render(graphics, mouseX, mouseY, delta);
    }

}
