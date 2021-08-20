package com.intro.client.util;

import com.intro.common.mixin.client.ResourceTextureAccessor;
import com.intro.common.mixin.client.ResourceTextureSubclassAccessor;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class TextureUtil {

    private static final Minecraft mc = Minecraft.getInstance();


    public static NativeImage convertIdentifierToNativeImage(ResourceLocation location) {
        NativeImage TEXTURE;
        mc.getTextureManager().bindForSetup(location);
        SimpleTexture texture = (SimpleTexture) mc.getTextureManager().getTexture(location);
        TEXTURE = getNativeImage(mc.getResourceManager(), texture);
        return TEXTURE;
    }

    private static NativeImage getNativeImage(ResourceManager manager, SimpleTexture texture) {
        SimpleTexture.TextureImage textureImage = loadTextureData(manager, texture);
        return ((ResourceTextureSubclassAccessor) textureImage).getImage();
    }

    private static SimpleTexture.TextureImage loadTextureData(ResourceManager resourceManager, SimpleTexture texture) {
        return SimpleTexture.TextureImage.load(resourceManager, ((ResourceTextureAccessor) texture).getLocation());
    }

}
