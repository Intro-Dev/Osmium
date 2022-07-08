package com.intro.client.render.gl.shader.uniform;

import com.intro.client.render.gl.shader.Shader;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.math.Vector4f;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class GlUniformVec4f extends GlUniformVariable<Vector4f> {

    public GlUniformVec4f(String name, Shader attachedShader) {
        super(name, attachedShader);
    }

    @Override
    public Vector4f get() {
        float[] params = new float[4];
        GL20.glGetUniformfv(attachedShader.getProgramId(), location, params);
        return new Vector4f(params[0], params[1], params[2], params[3]);
    }

    @Override
    public void set(Vector4f value) {
        GlStateManager._glUniform4(location, FloatBuffer.wrap(new float[] {value.x(), value.y(), value.z(), value.w()}));
    }
}
