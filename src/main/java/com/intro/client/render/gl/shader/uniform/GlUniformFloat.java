package com.intro.client.render.gl.shader.uniform;

import com.intro.client.render.gl.shader.Shader;
import org.lwjgl.opengl.GL20;

public class GlUniformFloat extends GlUniformVariable<Float> {

    public GlUniformFloat(String name, Shader attachedShader) {
        super(name, attachedShader);
    }

    @Override
    public Float get() {
        return GL20.glGetUniformf(attachedShader.getProgramId(), location);
    }

    @Override
    public void set(Float value) {
        GL20.glUniform1f(location, value);
    }
}
