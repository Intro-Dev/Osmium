package com.intro.client.render.renderer;

import com.intro.client.render.Color;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BatchRenderer2d implements Renderer2D {

    private BufferBuilder buffer;
    private boolean drawing;

    private final Minecraft mc = Minecraft.getInstance();

    public void begin(VertexFormat.Mode mode, VertexFormat format) {
        if(drawing)
            throw new IllegalStateException("Already drawing!");
        this.drawing = true;
        this.buffer = new BufferBuilder(4096);
        buffer.begin(mode, format);
    }

    public void end() {
        if(!drawing)
            throw new IllegalStateException("Not drawing!");
        drawing = false;
        buffer.end();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        // im going insane
        // ocasionally rendering certain Drawables to the screen causes batched elements to not rendering
        // only happens half of the time
        // please help
        RenderSystem.depthMask(true);
        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
        RenderSystem.enablePolygonOffset();
        RenderSystem.enableColorLogicOp();
        BufferUploader.end(buffer);
        RenderSystem.disableColorLogicOp();
        RenderSystem.disablePolygonOffset();
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(false);
    }

    @Override
    public void drawTexture(PoseStack stack, int x, int y, int z, int u, int v, int width, int height) {
        drawTexture(stack, x, y, z, u, v, 256, 256, width, height);
    }

    @Override
    public void drawTexture(PoseStack stack, int x, int y, int z, int u, int v, int maxU, int maxV, int width, int height) {
        Matrix4f matrices = stack.last().pose();
        drawTexturedQuad(matrices, x, x + width, y, y + height, z, u, maxU, v, maxV);
    }

    @Override
    public void drawQuad(PoseStack stack, int x, int y, int zOffset, int width, int height, float r, float g, float b, float a) {
        Matrix4f matrices = stack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.vertex(matrices, (float)x, (float)y + height, (float)zOffset).color(r, g, b, a).endVertex();
        buffer.vertex(matrices, (float)x + width, (float)y + height, (float)zOffset).color(r, g, b, a).endVertex();
        buffer.vertex(matrices, (float)x + width, (float)y, (float)zOffset).color(r, g, b, a).endVertex();
        buffer.vertex(matrices, (float)x, (float)y, (float)zOffset).color(r, g, b, a).endVertex();
    }

    @Override
    public void drawQuad(PoseStack stack, int x, int y, int zOffset, int width, int height, int color) {
        Color c = new Color(color);
        drawQuad(stack, x, y, zOffset, width, height, c.getFloatR(), c.getFloatG(), c.getFloatB(), c.getFloatA());
    }

    @Override
    public void drawSprite(PoseStack stack, int x, int y, int zOffset, int width, int height, TextureAtlasSprite sprite) {
        drawTexturedQuad(stack.last().pose(), x, x + width, y, y + height, zOffset, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1());
    }

    @Override
    public void fill(PoseStack stack, int x1, int y1, int x2, int y2, int color) {
        int j;
        Matrix4f matrices = stack.last().pose();
        if (x1 < x2) {
            j = x1;
            x1 = x2;
            x2 = j;
        }

        if (y1 < y2) {
            j = y1;
            y1 = y2;
            y2 = j;
        }

        float f = (float)(color >> 24 & 255) / 255.0F;
        float g = (float)(color >> 16 & 255) / 255.0F;
        float h = (float)(color >> 8 & 255) / 255.0F;
        float k = (float)(color & 255) / 255.0F;
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.vertex(matrices, (float)x1, (float)y2, 0.0F).color(g, h, k, f).endVertex();
        buffer.vertex(matrices, (float)x2, (float)y2, 0.0F).color(g, h, k, f).endVertex();
        buffer.vertex(matrices, (float)x2, (float)y1, 0.0F).color(g, h, k, f).endVertex();
        buffer.vertex(matrices, (float)x1, (float)y1, 0.0F).color(g, h, k, f).endVertex();
    }

    @Override
    public void drawHorizontalLine(PoseStack stack, int x1, int x2, int y, int color) {
        if (x2 < x1) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }

        fill(stack, x1, y, x2 + 1, y + 1, color);
    }

    @Override
    public void drawVerticalLine(PoseStack matrices, int x, int y1, int y2, int color) {
        if (y2 < y1) {
            int i = y1;
            y1 = y2;
            y2 = i;
        }

        fill(matrices, x, y1 + 1, x + 1, y2, color);
    }

    private void drawTexturedQuad(Matrix4f matrices, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        buffer.vertex(matrices, (float)x0, (float)y1, (float)z).uv(u0, v1).endVertex();
        buffer.vertex(matrices, (float)x1, (float)y1, (float)z).uv(u1, v1).endVertex();
        buffer.vertex(matrices, (float)x1, (float)y0, (float)z).uv(u1, v0).endVertex();
        buffer.vertex(matrices, (float)x0, (float)y0, (float)z).uv(u0, v0).endVertex();
    }

}
