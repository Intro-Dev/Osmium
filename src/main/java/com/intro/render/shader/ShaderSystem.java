package com.intro.render.shader;

import net.minecraft.util.Identifier;

import java.util.HashMap;

public class ShaderSystem {

    private static int largestId;

    private static HashMap<Integer, Shader> shaders = new HashMap<>();

    public static void loadShader(Identifier identifier) {
        largestId++;
        Shader loadedShader = new Shader(identifier);
        shaders.put(largestId, loadedShader);
    }

    public static Shader getShader(int id) {
        return shaders.get(id);
    }

    public static HashMap<Integer, Shader> getShaders() {
        return shaders;
    }


}
