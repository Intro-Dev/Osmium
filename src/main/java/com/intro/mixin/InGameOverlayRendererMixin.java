package com.intro.mixin;

import com.intro.config.BooleanOption;
import com.intro.config.OptionUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    /**
     @author Intro
     @reason Make LowFire work because it now uses BufferBuilder.
     */
    @Overwrite
    private static void renderFireOverlay(MinecraftClient mc, MatrixStack stack) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableTexture();
        Sprite sprite = ModelLoader.FIRE_1.getSprite();
        RenderSystem.setShaderTexture(0, sprite.getAtlas().getId());
        float f = sprite.getMinU();
        float g = sprite.getMaxU();
        float h = (f + g) / 2.0F;
        float i = sprite.getMinV();
        float j = sprite.getMaxV();
        float k = (i + j) / 2.0F;
        float l = sprite.getAnimationFrameDelta();
        float m = MathHelper.lerp(l, f, h);
        float n = MathHelper.lerp(l, g, h);
        float o = MathHelper.lerp(l, i, k);
        float p = MathHelper.lerp(l, j, k);
        float q = 1.0F;

        for(int r = 0; r < 2; ++r) {
            stack.push();
            float s = -0.5F;
            float t = 0.5F;
            float u = -0.5F;
            float v = 0.5F;
            float w = -0.5F;
            if(((BooleanOption) OptionUtil.Options.NoFireEnabled.get()).variable) {
                stack.translate((float)(-(r * 2 - 1)) * 0.24F, -0.53000001192092896D, 0.0D);
            } else {
                stack.translate((float)(-(r * 2 - 1)) * 0.24F, -0.30000001192092896D, 0.0D);
            }
            stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float)(r * 2 - 1) * 10.0F));
            Matrix4f matrix4f = stack.peek().getModel();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
            bufferBuilder.vertex(matrix4f, -0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).texture(n, p).next();
            bufferBuilder.vertex(matrix4f, 0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).texture(m, p).next();
            bufferBuilder.vertex(matrix4f, 0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).texture(m, o).next();
            bufferBuilder.vertex(matrix4f, -0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).texture(n, o).next();
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            stack.pop();
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);

    }
}
