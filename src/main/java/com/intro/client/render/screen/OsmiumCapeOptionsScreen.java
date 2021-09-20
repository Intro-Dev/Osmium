package com.intro.client.render.screen;

import com.intro.client.network.ClientNetworkHandler;
import com.intro.client.render.cape.Cape;
import com.intro.client.render.cape.CapeHandler;
import com.intro.client.render.cape.CosmeticManager;
import com.intro.client.render.color.Colors;
import com.intro.client.render.widget.BackAndForwardWidget;
import com.intro.client.render.widget.BooleanButtonWidget;
import com.intro.client.render.widget.EnumSelectWidget;
import com.intro.client.util.MathUtil;
import com.intro.client.util.RenderUtil;
import com.intro.common.config.Options;
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
import net.minecraft.network.chat.TranslatableComponent;
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

    public OsmiumCapeOptionsScreen(Screen parent) {
        super(new TranslatableComponent("osmium.cape_options"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        bgStartHeight = this.height / 2 - 256;

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

        Button refreshButton = new Button(this.width / 2 - 250, bgStartHeight + 350, 200, 20, new TranslatableComponent("osmium.refresh"), this::refresh);
        EnumSelectWidget toggleCapeWidget = new EnumSelectWidget(this.width / 2 - 250, bgStartHeight + 375 , 200, 20, Options.CustomCapeMode,"osmium.options.video_options.cape_");
        BooleanButtonWidget toggleAnimationWidget = new BooleanButtonWidget(this.width / 2 - 250, bgStartHeight + 400 , 200, 20, Options.AnimateCapes, "osmium.options.animate_capes_");
        BooleanButtonWidget toggleShowOtherCapesWidget = new BooleanButtonWidget(this.width / 2 - 250, bgStartHeight + 425 , 200, 20, Options.ShowOtherPlayersCapes, "osmium.options.show_other_capes_");


        forwardWidget = new BackAndForwardWidget(this.width / 2 + 200, bgStartHeight + 475, 20,  currentPage, 0, capePages.size() - 1);

        this.addRenderableWidget(toggleShowOtherCapesWidget);
        this.addRenderableWidget(toggleAnimationWidget);
        this.addRenderableWidget(forwardWidget);
        this.addRenderableWidget(toggleCapeWidget);
        this.addRenderableWidget(refreshButton);
        super.init();
    }

    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        PoseStack entityRenderStack = RenderSystem.getModelViewStack();
        entityRenderStack.pushPose();
        stack.pushPose();

        zoomInScale += 0.25 * delta;
        zoomInScale = Mth.clamp(zoomInScale, 0, 1);
        entityRenderStack.scale(zoomInScale, zoomInScale, zoomInScale);
        stack.scale(zoomInScale, zoomInScale, 0);

        fill(stack, this.width / 2 - 312, bgStartHeight,this.width / 2 + 312, this.height   / 2 + 256, Colors.BACKGROUND_GRAY.getColor().getInt());
        drawCenteredString(stack, mc.font, new TranslatableComponent("osmium.cape_select"), this.width / 2 + 200, bgStartHeight + 10, Colors.WHITE.getColor().getInt());

        List<Cape> pageCapes = capePages.get(currentPage.get());

        for(int i = 0; i < pageCapes.size(); i++) {
            Cape cape = pageCapes.get(i);

            if(CapeHandler.playerCapes.get(mc.player.getStringUUID()) != null && CapeHandler.playerCapes.get(mc.player.getStringUUID()).registryName.equals(cape.registryName)) {
                fill(stack, this.width / 2 + 100, bgStartHeight + 40 + (i * 70), this.width / 2 + 300, bgStartHeight + 100 + (i * 70), Colors.DARK_GRAY.getColor().getInt());
            }

            drawOutlinedBox(stack, this.width / 2 + 100, bgStartHeight + 40 + (i * 70), 200, 60, Colors.WHITE.getColor().getInt());
            stack.pushPose();
            RenderUtil.positionAccurateScale(stack, 0.5f, this.width / 2 + 70, bgStartHeight + 40 + (i * 70) - 10, 160, 80);
            // something wacky going on here
            // view stack scaling is slightly off, so we have to change pos a bit
            RenderSystem.setShaderTexture(0, cape.getFrameTexture());
            blit(stack, this.width / 2 + 70, bgStartHeight + 40 + (i * 70) - 10, 0, 0 , 160, 80, 160, 80);
            stack.popPose();

            //  these coordinates are just insane
            drawString(stack, mc.font,"Source: " + cape.source, this.width / 2 + 200, bgStartHeight + 40 + (i * 70) + 15, Colors.WHITE.getColor().getInt());
            drawString(stack, mc.font, "Animated: " + cape.isAnimated, this.width / 2 + 200, bgStartHeight + 40 + (i * 70) + 25, Colors.WHITE.getColor().getInt());
            drawString(stack, mc.font, "Creator: " + cape.creator, this.width / 2 + 200, bgStartHeight + 40 + (i * 70) + 35, Colors.WHITE.getColor().getInt());
        }

        drawCenteredString(stack, mc.font, currentPage.get() + 1 + "/" +  capePages.size(), this.width / 2 + 200, bgStartHeight + 475, Colors.WHITE.getColor().getInt());

        playerXRot -= 0.15 * delta;
        if(playerXRot <= -179.85) {
            playerXRot = 180;
        }

        // spent 2 hours fixing depth testing issues because I forgot to scale the ModelViewStack properly
        // please help
        renderEntityInInventory( this.width / 2 - 150, bgStartHeight + 300, 140, playerXRot, 0, mc.player);
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
        // but its only 7 checks, and I can't be bothered over a micro optimization
        for(int i = 0; i < pageCapes.size(); i++) {
            if(MathUtil.isPositionWithinBounds((int) mouseX, (int) mouseY, this.width / 2 + 100, bgStartHeight + 40 + (i * 70), 200, 60)) {
                CapeHandler.playerCapes.put(mc.player.getStringUUID(), pageCapes.get(i));
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

        forwardWidget = new BackAndForwardWidget(this.width / 2 + 200, bgStartHeight + 475, 20,  currentPage, 0, capePages.size() - 1);
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

}
