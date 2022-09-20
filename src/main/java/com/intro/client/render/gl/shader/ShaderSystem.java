package com.intro.client.render.gl.shader;

import com.intro.client.OsmiumClient;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Level;
import oshi.util.tuples.Triplet;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ShaderSystem {

    private static final ConcurrentHashMap<Triplet<ResourceLocation, ResourceLocation, VertexFormat>, Shader> compiledShaders = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Pair<Integer, String>, Integer> uniformLocations = new ConcurrentHashMap<>();

    private static Shader currentlyUsedShader;


    /**
     * see DefaultVertexFormats for how to name input attributes
     */
    public static Shader getOrCreateShader(ResourceLocation vertexShaderPath, ResourceLocation fragmentShaderPath, VertexFormat format) {
        Triplet<ResourceLocation, ResourceLocation, VertexFormat> triplet = new Triplet<>(vertexShaderPath, fragmentShaderPath, format);
        compiledShaders.computeIfAbsent(triplet, (element) -> {
            try {
                return new Shader(vertexShaderPath, fragmentShaderPath, format);
            } catch (IOException e) {
                OsmiumClient.LOGGER.log(Level.ERROR, "Failed compilation of shader: " + element.getA() + ", " + element.getB() + ", format: " + element.getC().toString());
            }
            return null;
        });
        return compiledShaders.get(triplet);
    }

    public static void reloadShaders() {
        for(Triplet<ResourceLocation, ResourceLocation, VertexFormat> triplet : compiledShaders.keySet()) {
            Shader originalShader = compiledShaders.get(triplet);
            originalShader.close();
            try {
                compiledShaders.put(triplet, new Shader(triplet.getA(), triplet.getB(), triplet.getC()));
            } catch (IOException e) {
                OsmiumClient.LOGGER.log(Level.ERROR, "Failed reload of shader: " + triplet.getA() + ", " + triplet.getB() + ", format: " + triplet.getC().toString());

            }
        }
    }

    public static void drawRenderTargetWithFrameBuffer(Shader shader, RenderTarget in, RenderTarget out) {
        in.bindRead();
        out.bindWrite(false);
        shader.useProgram();
        RenderSystem.disableDepthTest();
        BufferBuilder builder = new BufferBuilder(RenderType.MEDIUM_BUFFER_SIZE);
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        builder.vertex(0.0D, 0.0D, 500.0D).endVertex();
        builder.vertex(out.width, 0.0D, 500.0D).endVertex();
        builder.vertex(out.width, out.height, 500.0D).endVertex();
        builder.vertex(0.0D, out.height, 500.0D).endVertex();
        BufferUploader.draw(builder.end());
    }

    public static void useShader(Shader shader) {
        currentlyUsedShader = shader;
        shader.useProgram();
    }

    public static int usedShader() {
        return currentlyUsedShader.getProgramId();
    }

    public static int getUniformLocation(int id, String name) {
        Pair<Integer, String> uniformPair = new Pair<>(id, name);
        uniformLocations.computeIfAbsent(uniformPair, (key) -> GlStateManager._glGetUniformLocation(id, name));
        return uniformLocations.get(uniformPair);
    }

    public static void drawBuffer(BufferBuilder.RenderedBuffer buffer) {
        BufferUploader.draw(buffer);
    }


}
