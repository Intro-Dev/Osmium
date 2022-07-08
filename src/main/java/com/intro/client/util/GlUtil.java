package com.intro.client.util;

import com.intro.client.OsmiumClient;
import com.mojang.blaze3d.platform.GlStateManager;
import com.sun.jdi.NativeMethodException;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL20;

public class GlUtil {

    public static void checkError() {
        int err = GlStateManager._getError();
        if(err != GL20.GL_NO_ERROR) {
            OsmiumClient.LOGGER.log(Level.ERROR, "Gl error code: " + err + " ");
            throw new NativeMethodException("OpenGl Error: " + err);
        }
    }

    public static void checkProgram(int program) {
        System.out.println(program);
        if(!GL20.glIsProgram(program)) {
            OsmiumClient.LOGGER.log(Level.ERROR, "Shader Log: " + GL20.glGetShaderInfoLog(program));
            throw new NativeMethodException("Invalid program: " + program);
        }
        GL20.glValidateProgram(program);
        checkError();
        if(GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS) == GL20.GL_FALSE) {
            OsmiumClient.LOGGER.log(Level.ERROR, "Shader Log: " + GL20.glGetShaderInfoLog(program));
            throw new NativeMethodException("Program " + program + " failed validation");
        }
    }

}
