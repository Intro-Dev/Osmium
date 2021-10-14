package com.intro.client.render.multirender;

import com.intro.client.render.color.Color;
import com.intro.client.util.TextureUtil;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 <p>Platform for easily making high performance rendering calls</p>
 <p>Unfortunately useless in a lot of situations due to differences with the vanilla rendering system</p>
 <p>For a future optimisation update</p>
 */
public class MultiRenderer {

    private static final ExecutorService renderThreadExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NativeImage atlas;
    private static ResourceLocation atlasLocation;

    private static final AtomicReference<HashMap<ResourceLocation, Texture>> mappedTextures = new AtomicReference<>(new HashMap<>());

    private static int allocatedU = 0;

    private static final BufferBuilder textureDrawBuffer = new BufferBuilder(RenderType.MEDIUM_BUFFER_SIZE);
    private static final BufferBuilder coloredQuadDrawBuffer = new BufferBuilder(RenderType.MEDIUM_BUFFER_SIZE);


    public static Texture allocateTexture(NativeImage image) {
        Texture returnTex;
        if (atlas == null) {
            atlas = image;
        } else {
            atlas = TextureUtil.stitchImagesOnX(atlas, image);
        }
        // subtract 1 because yes
        returnTex = new Texture(allocatedU, 0, image.getWidth() - 1, image.getHeight() - 1);
        allocatedU += image.getWidth();
        Minecraft.getInstance().getTextureManager().release(atlasLocation);
        atlasLocation = Minecraft.getInstance().getTextureManager().register("multi-render-atlas", new DynamicTexture(atlas));
        return returnTex;
    }

    public static Texture allocateTexture(ResourceLocation location) {
        Texture returnTex;
        NativeImage image = TextureUtil.getImageAtLocation(location);
        if (atlas == null) {
            atlas = image;
        } else {
            atlas = TextureUtil.stitchImagesOnX(atlas, image);
        }
        returnTex = new Texture(allocatedU, 0, image.getWidth(), image.getHeight());
        allocatedU += image.getWidth() - 1;
        Minecraft.getInstance().getTextureManager().release(atlasLocation);
        atlasLocation = Minecraft.getInstance().getTextureManager().register("multi-render-atlas", new DynamicTexture(atlas));
        mappedTextures.get().put(location, returnTex);
        System.out.println(atlas.getWidth());
        return returnTex;
    }

    public static void drawTexture(PoseStack stack, int x, int y, int width, int height, Texture texture) {
        Matrix4f matrix4f = stack.last().pose();
        if(!textureDrawBuffer.building())
            textureDrawBuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        // ah yes
        // uv
        addTexturedQuadVertices(matrix4f, textureDrawBuffer, x, x + width, y, y + height, 0, texture.u() / (float) texture.width(), (texture.u() + (float) width) / (float) texture.width(), texture.v() / (float) texture.height(), (texture.v() + (float) height) / (float) texture.height());
    }

    private static void addTexturedQuadVertices(Matrix4f matrices, VertexConsumer consumer, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
        consumer.vertex(matrices, (float) x0, (float) y1, (float) z).uv(u0, v1).endVertex();
        consumer.vertex(matrices, (float) x1, (float) y1, (float) z).uv(u1, v1).endVertex();
        consumer.vertex(matrices, (float) x1, (float) y0, (float) z).uv(u1, v0).endVertex();
        consumer.vertex(matrices, (float) x0, (float) y0, (float) z).uv(u0, v0).endVertex();
    }

    public static void drawTexture(PoseStack stack, int x, int y, int width, int height, ResourceLocation resourceLocation) {
        Matrix4f matrix4f = stack.last().pose();
        Texture texture;
        if (mappedTextures.get().get(resourceLocation) != null) {
            texture = mappedTextures.get().get(resourceLocation);
        } else {
            texture = allocateTexture(resourceLocation);
        }

        if(!textureDrawBuffer.building())
            textureDrawBuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        // ah yes
        // uv
        addTexturedQuadVertices(matrix4f, textureDrawBuffer, x, x + width, y, y + height, 0, texture.u() / (float) texture.width(), (texture.u() + (float) width) / (float) texture.width(), texture.v() / (float) texture.height(), (texture.v() + (float) height) / (float) texture.height());
    }

    public static void fill(PoseStack stack, int x, int y, int width, int height, int color) {
        if(!coloredQuadDrawBuffer.building())
            coloredQuadDrawBuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        int o;

        int x1 = x + width;
        int y1 = y + height;

        if (x < x1) {
            o = x;
            x = x1;
            x1 = o;
        }

        if (y < y1) {
            o = y;
            y = y1;
            y1 = o;
        }

        Color c = new Color(color);

        Matrix4f matrix4f = stack.last().pose();
        coloredQuadDrawBuffer.vertex(matrix4f, (float)x, (float)y, 0.0F).color(c.getFloatR(), c.getFloatG(), c.getFloatB(), c.getFloatA()).endVertex();
        coloredQuadDrawBuffer.vertex(matrix4f, (float)x1, (float)y, 0.0F).color(c.getFloatR(), c.getFloatG(), c.getFloatB(), c.getFloatA()).endVertex();
        coloredQuadDrawBuffer.vertex(matrix4f, (float)x1, (float)y1, 0.0F).color(c.getFloatR(), c.getFloatG(), c.getFloatB(), c.getFloatA()).endVertex();
        coloredQuadDrawBuffer.vertex(matrix4f, (float)x, (float)y, 0.0F).color(c.getFloatR(), c.getFloatG(), c.getFloatB(), c.getFloatA()).endVertex();
    }

    public static void endTextureBatch() {
        if(textureDrawBuffer.building()) {
            textureDrawBuffer.end();
            RenderSystem.setShaderTexture(0, atlasLocation);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
            BufferUploader.end(textureDrawBuffer);
            RenderSystem.disableBlend();
        }
    }

    public static void endColorBatch() {
        if(coloredQuadDrawBuffer.building()) {
            coloredQuadDrawBuffer.end();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            BufferUploader.end(coloredQuadDrawBuffer);
        }
    }
}
