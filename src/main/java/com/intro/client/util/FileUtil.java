package com.intro.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    private static final Minecraft mc = Minecraft.getInstance();

    public static InputStream getResourceLocationStream(ResourceLocation location) throws IOException {

        return mc.getResourceManager().getResource(location).getInputStream();
    }


}
