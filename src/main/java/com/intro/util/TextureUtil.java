package com.intro.util;

import com.intro.mixin.ResourceTextureAccessor;
import com.intro.mixin.ResourceTextureSubclassAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class TextureUtil {

    private static MinecraftClient mc = MinecraftClient.getInstance();


    public static NativeImage convertIdentifierToNativeImage(Identifier identifier) {
        NativeImage TEXTURE;
        mc.getTextureManager().bindTexture(identifier);
        ResourceTexture texture = (ResourceTexture) mc.getTextureManager().getTexture(identifier);
        TEXTURE = getNativeImage(mc.getResourceManager(), texture);
        return TEXTURE;
    }

    private static NativeImage getNativeImage(ResourceManager manager, ResourceTexture texture) {
        ResourceTexture.TextureData textureData = loadTextureData(manager, texture);
        return ((ResourceTextureSubclassAccessor) textureData).getImage();
    }

    private static ResourceTexture.TextureData loadTextureData(ResourceManager resourceManager, ResourceTexture texture) {
        return ResourceTexture.TextureData.load(resourceManager, ((ResourceTextureAccessor) texture).getLocation());
    }

}
