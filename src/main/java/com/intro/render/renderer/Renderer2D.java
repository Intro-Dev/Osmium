package com.intro.render.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface Renderer2D {

    public void drawTexture(PoseStack stack, int x, int y, int z, int u, int v, int width, int height);

    public void drawTexture(PoseStack stack, int x, int y, int z, int u, int v, int maxU, int maxV, int width, int height);

    public void drawQuad(PoseStack stack, int x, int y, int zOffset, int width, int height, float r, float g, float b, float a);

    public void drawQuad(PoseStack stack, int x, int y, int zOffset, int width, int height, int color);

    public void drawSprite(PoseStack stack, int x, int y, int zOffset, int width, int height, TextureAtlasSprite sprite);

    public void fill(PoseStack stack, int x1, int y1, int x2, int y2, int color);

    public void drawHorizontalLine(PoseStack stack, int x1, int x2, int y, int color);

    public void drawVerticalLine(PoseStack stack, int x, int y1, int y2, int color);

    public void begin(VertexFormat.Mode mode, VertexFormat format);

    public void end();

}
