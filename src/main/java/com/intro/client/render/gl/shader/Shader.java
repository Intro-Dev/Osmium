package com.intro.client.render.gl.shader;

import com.intro.client.OsmiumClient;
import com.intro.client.util.GlUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.util.Optional;

public class Shader implements AutoCloseable {

    private final int programId;

    protected Shader(ResourceLocation vertexShaderPath, ResourceLocation fragmentShaderPath) throws IOException {
        Optional<Resource> vertexShaderResource = Minecraft.getInstance().getResourceManager().getResource(vertexShaderPath);
        Optional<Resource> fragmentShaderResource = Minecraft.getInstance().getResourceManager().getResource(fragmentShaderPath);
        String vertexShaderCode;
        String fragmentShaderCode;

        if(vertexShaderResource.isPresent() && fragmentShaderResource.isPresent()) {
            vertexShaderCode = new String(vertexShaderResource.get().open().readAllBytes());
            fragmentShaderCode = new String(fragmentShaderResource.get().open().readAllBytes());
        } else {
            throw new IllegalArgumentException("Shader not found at path!");
        }

        int vertexShaderId = createShader(vertexShaderCode, GL20.GL_VERTEX_SHADER);
        int fragmentShaderId = createShader(fragmentShaderCode, GL20.GL_FRAGMENT_SHADER);
        programId = GlStateManager.glCreateProgram();
        GlStateManager.glAttachShader(programId, vertexShaderId);
        GlStateManager.glAttachShader(programId, fragmentShaderId);
        GlStateManager.glLinkProgram(programId);
        GlUtil.checkProgram(programId);
    }

    private static int createShader(String shaderSource, int shaderType) {
        int shaderId = GlStateManager.glCreateShader(shaderType);
        checkShaderError(shaderId);
        GL20.glShaderSource(shaderId, shaderSource);
        checkShaderError(shaderId);
        GlStateManager.glCompileShader(shaderId);
        checkShaderError(shaderId);
        if(GlStateManager.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            OsmiumClient.LOGGER.log(Level.ERROR, "Failed shader compilation: " + GlStateManager.glGetShaderInfoLog(shaderId, 500));
            OsmiumClient.LOGGER.log(Level.ERROR, "Shader source: " + shaderSource);
        }
        return shaderId;
    }

    private static void checkShaderError(int id) {
        int error = GlStateManager._getError();
        if(error != GL20.GL_NO_ERROR) {
            OsmiumClient.LOGGER.log(Level.ERROR, "Error in shader creation: " + error);
            OsmiumClient.LOGGER.log(Level.ERROR, "Shader log: " + GlStateManager.glGetShaderInfoLog(id, 500));
        }
    }


    protected void useProgram() {
        GlStateManager._glUseProgram(programId);
    }

    @Override
    public void close() {
        GlStateManager.glDeleteProgram(programId);
    }

    public int getProgramId() {
        return programId;
    }
}
