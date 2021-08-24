package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.common.config.Options;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    /**
     @author Intro
     @reason Make LowFire work because it now uses BufferBuilder.
     */
    @Overwrite
    private static void renderFire(Minecraft mc, PoseStack stack) {
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableTexture();
        TextureAtlasSprite textureAtlasSprite = ModelBakery.FIRE_1.sprite();
        RenderSystem.setShaderTexture(0, textureAtlasSprite.atlas().location());
        float f = textureAtlasSprite.getU0();
        float g = textureAtlasSprite.getU1();
        float h = (f + g) / 2.0F;
        float i = textureAtlasSprite.getV0();
        float j = textureAtlasSprite.getV1();
        float k = (i + j) / 2.0F;
        float l = textureAtlasSprite.uvShrinkRatio();
        float m = Mth.lerp(l, f, h);
        float n = Mth.lerp(l, g, h);
        float o = Mth.lerp(l, i, k);
        float p = Mth.lerp(l, j, k);
        float q = 1.0F;

        for(int r = 0; r < 2; ++r) {
            stack.pushPose();
            float s = -0.5F;
            float t = 0.5F;
            float u = -0.5F;
            float v = 0.5F;
            float w = -0.5F;
            if(OsmiumClient.options.getBooleanOption(Options.BlockOutlineAlpha).variable) {
                stack.translate((float)(-(r * 2 - 1)) * 0.24F, -0.53000001192092896D, 0.0D);
            } else {
                stack.translate((float)(-(r * 2 - 1)) * 0.24F, -0.30000001192092896D, 0.0D);
            }
            stack.mulPose(Vector3f.YP.rotationDegrees((float)(r * 2 - 1) * 10.0F));
            Matrix4f matrix4f = stack.last().pose();
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
            bufferBuilder.vertex(matrix4f, -0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(n, p).endVertex();
            bufferBuilder.vertex(matrix4f, 0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(m, p).endVertex();
            bufferBuilder.vertex(matrix4f, 0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(m, o).endVertex();
            bufferBuilder.vertex(matrix4f, -0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(n, o).endVertex();
            bufferBuilder.end();
            BufferUploader.end(bufferBuilder);
            stack.popPose();
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);
    }

}
