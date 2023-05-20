package dev.lobstershack.client.render.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.render.color.Colors;
import dev.lobstershack.client.render.cosmetic.Cape;
import dev.lobstershack.client.render.widget.AbstractScalableButton;
import dev.lobstershack.client.render.widget.BackAndForwardWidget;
import dev.lobstershack.client.render.widget.BooleanButtonWidget;
import dev.lobstershack.client.render.widget.EnumSelectWidget;
import dev.lobstershack.client.util.MathUtil;
import dev.lobstershack.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// I absolutely despise this class
// But I don't want to spend the time writing a new options screen for the new cape system
// So the code is just adapted
public class OsmiumCapeOptionsScreen extends Screen {

    private final Minecraft mc = Minecraft.getInstance();

    private final Screen parent;

    private float playerXRot = 0;
    private float zoomInScale = 0f;

    private final AtomicInteger currentPage = new AtomicInteger(0);
    private List<List<Cape>> capePages;

    private int bgStartHeight = 0;
    private BackAndForwardWidget forwardWidget;

    private double guiScale;


    public OsmiumCapeOptionsScreen(Screen parent) {
        super(Component.translatable("osmium.cape_options"));
        this.parent = parent;
    }


    @Override
    protected void init() {

        guiScale = 2f / mc.options.guiScale().get();

        bgStartHeight = (int) (this.height / 2 - (256 * guiScale));

        List<Cape> rawCapes = OsmiumClient.cosmeticManager.getLocalCapes().stream().toList();
        capePages = new ArrayList<>();

        int numOps = rawCapes.size() / 6;
        int extraOps = rawCapes.size() % 6;

        int i = 0;

        while (i < numOps) {
            capePages.add(rawCapes.subList(i * 6, i * 6 + 6));
            i++;
        }

        // account for remainder
        capePages.add(rawCapes.subList(i * 6, i * 6 + extraOps));

        // standard width and heights have to be clamped or the button uv cords are messed up for the whole one person who plays on 1 gui scale
        int standardButtonWidth = (int) (200 * guiScale);
        standardButtonWidth = Mth.clamp(standardButtonWidth, 0, 200);

        int standardButtonHeight = (int) (20 * guiScale);
        standardButtonHeight = Mth.clamp(standardButtonHeight, 0, 20);

        int buttonStartHeight = (int) (bgStartHeight + (350 * guiScale));
        int buttonYIncrement = (int) (standardButtonHeight * 1.25);


        // why are widgets so annoying to scale properly
        AbstractScalableButton refreshButton = new AbstractScalableButton((int) (this.width / 2 - (250 * guiScale)), buttonStartHeight, standardButtonWidth, standardButtonHeight, Component.translatable("osmium.refresh_capes"), this::refresh, guiScale);
        EnumSelectWidget toggleCapeWidget = new EnumSelectWidget((int) (this.width / 2 - (250 * guiScale)), buttonStartHeight + buttonYIncrement, standardButtonWidth, standardButtonHeight, Options.CustomCapeMode,"osmium.options.video_options.cape_", (float) guiScale);
        BooleanButtonWidget toggleAnimationWidget = new BooleanButtonWidget((int) (this.width / 2 - (250 * guiScale)), buttonStartHeight + buttonYIncrement * 2, standardButtonWidth, standardButtonHeight, Options.AnimateCapes, "osmium.options.animate_capes_", (float) guiScale);
        BooleanButtonWidget toggleShowOtherCapesWidget = new BooleanButtonWidget((int) (this.width / 2 - (250 * guiScale)), buttonStartHeight + buttonYIncrement * 3 , standardButtonWidth, standardButtonHeight, Options.ShowOtherPlayersCapes, "osmium.options.show_other_capes_", (float) guiScale);


        AbstractScalableButton backButton = new AbstractScalableButton((int) (this.width / 2 - (250 * guiScale)), buttonStartHeight + buttonYIncrement * 5, standardButtonWidth, standardButtonHeight, Component.translatable("osmium.options.video_options.back"), button -> mc.setScreen(this.parent), guiScale);

        // this widget is 1 pixel off center
        // :)
        forwardWidget = new BackAndForwardWidget((int) (this.width / 2 + (192 * guiScale)), (int) (bgStartHeight + (475 * guiScale)), (int) (30 * guiScale), currentPage, 0, capePages.size() - 1, (float) guiScale);

        this.addRenderableWidget(backButton);
        this.addRenderableWidget(toggleShowOtherCapesWidget);
        this.addRenderableWidget(toggleAnimationWidget);
        this.addRenderableWidget(forwardWidget);
        this.addRenderableWidget(toggleCapeWidget);
        this.addRenderableWidget(refreshButton);
        super.init();
    }

    // the gore from all the multiplications for gui scaling
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        PoseStack stack = graphics.pose();
        PoseStack entityRenderStack = RenderSystem.getModelViewStack();
        entityRenderStack.pushPose();
        stack.pushPose();

        zoomInScale += 0.25 * delta;
        zoomInScale = Mth.clamp(zoomInScale, 0, 1);
        entityRenderStack.scale(zoomInScale, zoomInScale, zoomInScale);
        stack.scale(zoomInScale, zoomInScale, 0);

        graphics.fill((int) (this.width / 2 - (312 * guiScale)), bgStartHeight, (int) (this.width / 2 + (312 * guiScale)), (int) (this.height / 2 + (256 * guiScale)), Colors.BACKGROUND_GRAY.getColor().getInt());
        RenderUtil.renderScaledText(graphics, mc.font, Component.translatable("osmium.cape_select"), (int) (this.width / 2 + (180 * guiScale)), bgStartHeight + (10 * guiScale), Colors.WHITE.getColor().getInt(), (float) guiScale, true);

        List<Cape> pageCapes = capePages.get(currentPage.get());

        for(int i = 0; i < pageCapes.size(); i++) {
            Cape cape = pageCapes.get(i);

            if(OsmiumClient.cosmeticManager.getPlayerCape(Minecraft.getInstance().user.getUuid().toLowerCase()) != null && OsmiumClient.cosmeticManager.getPlayerCape(Minecraft.getInstance().user.getUuid().toLowerCase()) == cape) {
                graphics.fill((int) (this.width / 2 + (100 * guiScale)), (int) (bgStartHeight + (40 + (i * 70)) * guiScale), (int) (this.width / 2 + (300 * guiScale)), (int) (bgStartHeight + (100 + (i * 70)) * guiScale), Colors.DARK_GRAY.getColor().getInt());
            }

            drawOutlinedBox(graphics, (int) (this.width / 2 + (100 * guiScale)), (int) (bgStartHeight + (40 + (i * 70)) * guiScale), (int) (200 * guiScale), (int) (60 * guiScale), Colors.WHITE.getColor().getInt());
            stack.pushPose();
            RenderUtil.positionAccurateScale(stack, 0.5f, (int) (this.width / 2 + (70 * guiScale)), (int) (bgStartHeight + (40 + (i * 70) - 10) * guiScale), (int) (160 * guiScale), (int) (80 * guiScale));
            // something wacky going on here
            // view stack scaling is slightly off, so we have to change pos a bit
            graphics.blit(cape.getFrameTexture(), (int) (this.width / 2 + (70 * guiScale)), (int) (bgStartHeight + (40 + (i * 70) - 10) * guiScale), 0, 0 , (int) (160 * guiScale), (int) (80 * guiScale), (int) (160 * guiScale), (int) (80 * guiScale));
            stack.popPose();

            //  these coordinates are just insane
            RenderUtil.renderScaledText(graphics, mc.font,"Source: " + cape.source, (int) (this.width / 2 + (200 * guiScale)), (int) (bgStartHeight + (40 + (i * 70) + 15) * guiScale), Colors.WHITE.getColor().getInt(), (float) guiScale, true);
            RenderUtil.renderScaledText(graphics, mc.font, "Animated: " + cape.animated, (int) (this.width / 2 + (200 * guiScale)), (int) (bgStartHeight + (40 + (i * 70) + 25) * guiScale), Colors.WHITE.getColor().getInt(), (float) guiScale, true);
            RenderUtil.renderScaledText(graphics, mc.font, "Creator: " + cape.creator, (int) (this.width / 2 + (200 * guiScale)), (int) (bgStartHeight + (40 + (i * 70) + 35) * guiScale)  , Colors.WHITE.getColor().getInt(), (float) guiScale, true);
        }

        RenderUtil.renderScaledText(graphics, mc.font, currentPage.get() + 1 + "/" +  capePages.size(), (int) (this.width / 2 + (200 * guiScale)), (int) (bgStartHeight + (475 * guiScale)), Colors.WHITE.getColor().getInt(), (float) guiScale, true);

        playerXRot -= 0.15 * delta;
        if(playerXRot <= -179.85) {
            playerXRot = 180;
        }

        // spent 2 hours fixing depth testing issues because I forgot to scale the ModelViewStack properly
        // please help
        renderEntityInInventory((int) (this.width / 2 - (150 * guiScale)), (int) (bgStartHeight + (300 * guiScale)), (int) (140 * guiScale), playerXRot, 0, mc.player);
        stack.popPose();
        entityRenderStack.popPose();
        super.render(graphics, mouseX, mouseY, delta);
    }

    public void drawOutlinedBox(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        graphics.vLine(x, y, y + height, color);
        graphics.vLine(x + width, y, y + height, color);
        graphics.hLine(x, x + width, y, color);
        graphics.hLine(x, x + width, y + height, color);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseCode) {
        List<Cape> pageCapes = capePages.get(currentPage.get());

        // yes I know this is the worst method of 2d hitbox detection
        // but its only 6 checks, and I can't be bothered over a micro optimization
        for(int i = 0; i < pageCapes.size(); i++) {
            if(MathUtil.isPositionWithinBounds((int) mouseX, (int) mouseY, (int) (this.width / 2 + (100 * guiScale)), (int) (bgStartHeight + (40 + (i * 70)) * guiScale), (int) (200 * guiScale), (int) (60 * guiScale))) {
                OsmiumClient.cosmeticManager.setLocalPlayerCape(pageCapes.get(i));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                Options.SetCape.set(pageCapes.get(i).name);
                return super.mouseClicked(mouseX, mouseY, mouseCode);
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseCode);
    }

    protected void refresh(Button button) {
        OsmiumClient.cosmeticManager.loadLocalCapes();

        List<Cape> rawCapes = OsmiumClient.cosmeticManager.getLocalCapes().stream().toList();
        capePages = new ArrayList<>();

        int numOps = rawCapes.size() / 6;
        int extraOps = rawCapes.size() % 6;

        int i = 0;

        while (i < numOps) {
            capePages.add(rawCapes.subList(i * 6, i * 6 + 6));
            i++;
        }

        // account for remainder
        capePages.add(rawCapes.subList(i * 6, i * 6 + extraOps));

        forwardWidget = new BackAndForwardWidget((int) (this.width / 2 + (184 * guiScale)), (int) (bgStartHeight + (475 * guiScale)), (int) (30 * guiScale),  currentPage, 0, capePages.size() - 1, (float) guiScale);

    }


    public static void renderEntityInInventory(int x, int y, int scale, float xRot, float yRot, LivingEntity livingEntity) {
        float xRotClamped = Mth.clamp(xRot, -180, 180);
        float yRotClamped = Mth.clamp(yRot, -180, 180);
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        // translate far from screen
        poseStack.translate(x, y, 1050.0D);
        poseStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack poseStack2 = new PoseStack();
        // translate far from screen
        poseStack2.translate(0.0D, 0.0D, 1000.0D);
        poseStack2.scale((float)scale, (float)scale, (float)scale);
        // flip to right side
        Quaternionf zRotationQuaternion = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf yRotationQuaternion = (new Quaternionf()).rotateX(yRotClamped * 20.0F * 0.017453292F);
        zRotationQuaternion.mul(yRotationQuaternion);
        poseStack2.mulPose(zRotationQuaternion);
        // set entity rotations
        float m = livingEntity.yBodyRot;
        float n = livingEntity.getYRot();
        float o = livingEntity.getXRot();
        float p = livingEntity.yHeadRotO;
        float q = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 180.0F + xRotClamped * 20.0F;
        livingEntity.setYRot(180.0F + xRotClamped * 20.0f);
        livingEntity.setXRot(-yRotClamped * 20.0F);
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();
        // setup for rendering
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        yRotationQuaternion.conjugate();
        entityRenderDispatcher.overrideCameraOrientation(yRotationQuaternion);
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, poseStack2, bufferSource, 15728880));
        bufferSource.endBatch();
        // undo changes
        entityRenderDispatcher.setRenderShadow(true);
        livingEntity.yBodyRot = m;
        livingEntity.setYRot(n);
        livingEntity.setXRot(o);
        livingEntity.yHeadRotO = p;
        livingEntity.yHeadRot = q;
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
