package com.intro.client.render.gl.shader.uniform;

import com.intro.client.render.gl.shader.Shader;
import com.mojang.blaze3d.platform.GlStateManager;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class GlUniformVec3f extends GlUniformVariable<Vector3f> {

    public GlUniformVec3f(String name, Shader attachedShader) {
        super(name, attachedShader);
    }

    @Override
    public Vector3f get() {
        float[] params = new float[3];
        GL20.glGetUniformfv(attachedShader.getProgramId(), location, params);
        return new Vector3f(params[0], params[1], params[2]);
    }

    @Override
    public void set(Vector3f value) {
        GlStateManager._glUniform3(location, FloatBuffer.wrap(new float[] {value.x(), value.y(), value.z()}));
    }
}
