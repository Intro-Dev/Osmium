package com.intro.client.render.gl.shader.uniform;

import com.intro.client.render.gl.shader.Shader;
import com.intro.client.util.GlUtil;
import com.mojang.math.Matrix4f;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class GlUniformMatrix4f extends GlUniformVariable<Matrix4f> {

    public GlUniformMatrix4f(String name, Shader attachedShader) {
        super(name, attachedShader);
    }

    @Override
    public Matrix4f get() {
        checkIfCanAccessValue();
        float[] params = new float[16];
        GL20.glGetUniformfv(attachedShader.getProgramId(), location, params);
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.load(FloatBuffer.wrap(params));
        GlUtil.checkError();
        return matrix4f;
    }

    @Override
    public void set(Matrix4f value) {
        checkIfCanAccessValue();
        // 16 floats with a byte size of 4
        FloatBuffer buffer = FloatBuffer.allocate(16);
        value.storeTransposed(buffer);
        GL20.glUniformMatrix4fv(location, true, buffer);
        GlUtil.checkError();
    }
}
