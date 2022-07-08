package com.intro.client.render.gl.shader.uniform;

import com.intro.client.render.gl.shader.Shader;
import com.intro.client.render.gl.shader.ShaderSystem;
import com.intro.client.util.GlUtil;

public abstract class GlUniformVariable<T> {

    protected final int location;
    protected final String name;
    protected final Shader attachedShader;

    public GlUniformVariable(String name, Shader attachedShader) {
        // GlUtil.checkProgram(attachedShader.getProgramId());
        this.name = name;
        this.attachedShader = attachedShader;
        this.location = ShaderSystem.getUniformLocation(attachedShader.getProgramId(), name);
        GlUtil.checkError();
    }

    public abstract T get();
    public abstract void set(T value);

    protected void checkIfCanAccessValue() {
        if(ShaderSystem.usedShader() != attachedShader.getProgramId()) throw new IllegalStateException("Uniform variables can only be accessed when the target shader is being used!");
    }
}

