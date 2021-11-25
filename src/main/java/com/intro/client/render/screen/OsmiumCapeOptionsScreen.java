package com.intro.client.render.screen;

import com.intro.client.OsmiumClient;
import com.intro.client.network.ClientNetworkHandler;
import com.intro.client.render.cape.Cape;
import com.intro.client.render.cape.CosmeticManager;
import com.intro.client.render.color.Colors;
import com.intro.client.render.widget.AbstractScalableButton;
import com.intro.client.render.widget.BackAndForwardWidget;
import com.intro.client.render.widget.BooleanButtonWidget;
import com.intro.client.render.widget.EnumSelectWidget;
import com.intro.client.util.MathUtil;
import com.intro.client.util.RenderUtil;
import com.intro.common.config.Options;
import com.intro.common.config.options.Option;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        super(new TranslatableComponent("osmium.cape_options"));
        this.parent = parent;
    }


    @Override
    protected void init() {

        guiScale = 2f / mc.options.guiScale;

        bgStartHeight = (int) (this.height / 2 - (256 * guiScale));

        List<Cape> rawCapes = CosmeticManager.getAllCapes().stream().toList();
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
        AbstractScalableButton refreshButton = new AbstractScalableButton((int) (this.width / 2 - (250 * guiScale)), buttonStartHeight, standardButtonWidth, standardButtonHeight, new TranslatableComponent("osmium.refresh_capes"), this::refresh, guiScale);
        EnumSelectWidget toggleCapeWidget = new EnumSelectWidget((int) (this.width / 2 - (250 * guiScale)), buttonStartHeight + buttonYIncrement, standardButtonWidth, standardButtonHeight, Options.CustomCapeMode,"osmium.options.video_options.cape_", (float) guiScale);
        BooleanButtonWidget toggleAnimationWidget = new BooleanButtonWidget((int) (this.width / 2 - (250 * guiScale)), buttonStartHeight + buttonYIncrement * 2, standardButtonWidth, standardButtonHeight, Options.AnimateCapes, "osmium.options.animate_capes_", (float) guiScale);
        BooleanButtonWidget toggleShowOtherCapesWidget = new BooleanButtonWidget((int) (this.width / 2 - (250 * guiScale)), buttonStartHeight + buttonYIncrement * 3 , standardButtonWidth, standardButtonHeight, Options.ShowOtherPlayersCapes, "osmium.options.show_other_capes_", (float) guiScale);


        AbstractScalableButton backButton = new AbstractScalableButton((int) (this.width / 2 - (250 * guiScale)), buttonStartHeight + buttonYIncrement * 5, standardButtonWidth, standardButtonHeight   , new TranslatableComponent("osmium.options.video_options.back"), button -> mc.setScreen(this.parent), guiScale);

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
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        PoseStack entityRenderStack = RenderSystem.getModelViewStack();
        entityRenderStack.pushPose();
        stack.pushPose();

        zoomInScale += 0.25 * delta;
        zoomInScale = Mth.clamp(zoomInScale, 0, 1);
        entityRenderStack.scale(zoomInScale, zoomInScale, zoomInScale);
        stack.scale(zoomInScale, zoomInScale, 0);

        fill(stack, (int) (this.width / 2 - (312 * guiScale)), bgStartHeight, (int) (this.width / 2 + (312 * guiScale)), (int) (this.height / 2 + (256 * guiScale)), Colors.BACKGROUND_GRAY.getColor().getInt());
        RenderUtil.renderScaledText(stack, mc.font, new TranslatableComponent("osmium.cape_select"), (int) (this.width / 2 + (180 * guiScale)), bgStartHeight + (10 * guiScale), Colors.WHITE.getColor().getInt(), (float) guiScale, true);

        List<Cape> pageCapes = capePages.get(currentPage.get());

        for(int i = 0; i < pageCapes.size(); i++) {
            Cape cape = pageCapes.get(i);

            if(CosmeticManager.playerCapes.get(mc.player.getStringUUID()) != null && CosmeticManager.playerCapes.get(mc.player.getStringUUID()).registryName.equals(cape.registryName)) {
                fill(stack, (int) (this.width / 2 + (100 * guiScale)), (int) (bgStartHeight + (40 + (i * 70)) * guiScale), (int) (this.width / 2 + (300 * guiScale)), (int) (bgStartHeight + (100 + (i * 70)) * guiScale), Colors.DARK_GRAY.getColor().getInt());
            }

            drawOutlinedBox(stack, (int) (this.width / 2 + (100 * guiScale)), (int) (bgStartHeight + (40 + (i * 70)) * guiScale), (int) (200 * guiScale), (int) (60 * guiScale), Colors.WHITE.getColor().getInt());
            stack.pushPose();
            RenderUtil.positionAccurateScale(stack, 0.5f, (int) (this.width / 2 + (70 * guiScale)), (int) (bgStartHeight + (40 + (i * 70) - 10) * guiScale), (int) (160 * guiScale), (int) (80 * guiScale));
            // something wacky going on here
            // view stack scaling is slightly off, so we have to change pos a bit
            RenderSystem.setShaderTexture(0, cape.getFrameTexture());
            blit(stack, (int) (this.width / 2 + (70 * guiScale)), (int) (bgStartHeight + (40 + (i * 70) - 10) * guiScale), 0, 0 , (int) (160 * guiScale), (int) (80 * guiScale), (int) (160 * guiScale), (int) (80 * guiScale));
            stack.popPose();

            //  these coordinates are just insane
            RenderUtil.renderScaledText(stack, mc.font,"Source: " + cape.source, (int) (this.width / 2 + (200 * guiScale)), (int) (bgStartHeight + (40 + (i * 70) + 15) * guiScale), Colors.WHITE.getColor().getInt(), (float) guiScale, true);
            RenderUtil.renderScaledText(stack, mc.font, "Animated: " + cape.isAnimated, (int) (this.width / 2 + (200 * guiScale)), (int) (bgStartHeight + (40 + (i * 70) + 25) * guiScale), Colors.WHITE.getColor().getInt(), (float) guiScale, true);
            RenderUtil.renderScaledText(stack, mc.font, "Creator: " + cape.creator, (int) (this.width / 2 + (200 * guiScale)), (int) (bgStartHeight + (40 + (i * 70) + 35) * guiScale)  , Colors.WHITE.getColor().getInt(), (float) guiScale, true);
        }

        RenderUtil.renderScaledText(stack, mc.font, currentPage.get() + 1 + "/" +  capePages.size(), (int) (this.width / 2 + (200 * guiScale)), (int) (bgStartHeight + (475 * guiScale)), Colors.WHITE.getColor().getInt(), (float) guiScale, true);

        playerXRot -= 0.15 * delta;
        if(playerXRot <= -179.85) {
            playerXRot = 180;
        }

        // spent 2 hours fixing depth testing issues because I forgot to scale the ModelViewStack properly
        // please help
        renderEntityInInventory((int) (this.width / 2 - (150 * guiScale)), (int) (bgStartHeight + (300 * guiScale)), (int) (140 * guiScale), playerXRot, 0, mc.player);
        stack.popPose();
        entityRenderStack.popPose();
        super.render(stack, mouseX, mouseY, delta);
    }

    public void drawOutlinedBox(PoseStack stack, int x, int y, int width, int height, int color) {
        vLine(stack, x, y, y + height, color);
        vLine(stack, x + width, y, y + height, color);
        hLine(stack, x, x + width, y, color);
        hLine(stack, x, x + width, y + height, color);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseCode) {
        List<Cape> pageCapes = capePages.get(currentPage.get());

        // yes I know this is the worst method of 2d hitbox detection
        // but its only 6 checks, and I can't be bothered over a micro optimization
        for(int i = 0; i < pageCapes.size(); i++) {
            if(MathUtil.isPositionWithinBounds((int) mouseX, (int) mouseY, (int) (this.width / 2 + (100 * guiScale)), (int) (bgStartHeight + (40 + (i * 70)) * guiScale), (int) (200 * guiScale), (int) (60 * guiScale))) {
                CosmeticManager.playerCapes.put(mc.player.getStringUUID(), pageCapes.get(i));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                OsmiumClient.options.put(Options.SetCape, new Option<>(Options.SetCape, pageCapes.get(i).registryName));
                try {
                    ClientNetworkHandler.sendCapeSetPacket(pageCapes.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return super.mouseClicked(mouseX, mouseY, mouseCode);
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseCode);
    }

    protected void refresh(Button button) {
        CosmeticManager.refresh();

        List<Cape> rawCapes = CosmeticManager.getAllCapes().stream().toList();
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
        Quaternion zRotationQuaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion yRotationQuaternion = Vector3f.XP.rotationDegrees(yRotClamped * 20.0F);
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
        yRotationQuaternion.conj();
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
