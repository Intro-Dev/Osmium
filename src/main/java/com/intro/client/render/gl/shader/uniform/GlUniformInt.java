package com.intro.client.render.gl.shader.uniform;

import com.intro.client.render.gl.shader.Shader;
import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.opengl.GL20;

public class GlUniformInt extends GlUniformVariable<Integer> {

    public GlUniformInt(String name, Shader attachedShader) {
        super(name, attachedShader);
    }

    @Override
    public Integer get() {
        return GL20.glGetUniformi(attachedShader.getProgramId(), location);
    }

    @Override
    public void set(Integer value) {
        GlStateManager._glUniform1i(location, value);
    }
}
