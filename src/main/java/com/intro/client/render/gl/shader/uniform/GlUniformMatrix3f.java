package com.intro.client.render.gl.shader.uniform;

import com.intro.client.render.gl.shader.Shader;
import com.mojang.blaze3d.platform.GlStateManager;
import org.joml.Matrix3f;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class GlUniformMatrix3f extends GlUniformVariable<Matrix3f> {

    public GlUniformMatrix3f(String name, Shader attachedShader) {
        super(name, attachedShader);
    }

    @Override
    public Matrix3f get() {
        float[] params = new float[9];
        GL20.glGetUniformfv(attachedShader.getProgramId(), location, params);
        Matrix3f matrix3f = new Matrix3f();
        matrix3f.get(FloatBuffer.wrap(params));
        return matrix3f;
    }

    @Override
    public void set(Matrix3f value) {
        FloatBuffer buffer = FloatBuffer.allocate(9);
        value.set(buffer);
        GlStateManager._glUniformMatrix3(location, true, buffer);
    }
}
