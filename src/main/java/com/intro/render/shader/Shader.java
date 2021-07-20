package com.intro.render.shader;

import com.google.gson.JsonSyntaxException;
import com.intro.mixin.ShaderEffectInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Shader {

    private final Identifier identifier;
    private final MinecraftClient mc = MinecraftClient.getInstance();

    private Framebuffer framebuffer;

    private static final Logger LOGGER = LogManager.getLogger();

    private ShaderEffect shader;

    public Shader(Identifier identifier) {
        this.identifier = identifier;
    }

    public void load() {
        if (this.shader != null) {
            this.shader.close();
        }

        try {
            this.shader = new ShaderEffect(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), identifier);
            this.shader.setupDimensions(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
            this.framebuffer = ((ShaderEffectInvoker) this.shader).invokeGetTarget("minecraft:main");
        } catch (IOException e) {
            LOGGER.warn("Failed to load shader: {}", identifier, e);
            e.printStackTrace();
            this.shader = null;
            this.framebuffer = null;
        } catch (JsonSyntaxException e) {
            LOGGER.warn("Failed to parse shader: {}", identifier, e);
            e.printStackTrace();
            this.shader = null;
            this.framebuffer = null;
        }
    }

    public void draw() {
        if(this.framebuffer != null) {
            // RenderSystem.enableBlend();
            // RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
            this.framebuffer.draw(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), false);
            // RenderSystem.disableBlend();
        }
    }

    public String getPath() {
        return identifier.getPath();
    }


}
