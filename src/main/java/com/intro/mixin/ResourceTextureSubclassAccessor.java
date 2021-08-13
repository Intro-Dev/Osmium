package com.intro.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net/minecraft/client/renderer/texture/SimpleTexture$TextureImage")
public interface ResourceTextureSubclassAccessor {

    @Accessor
    NativeImage getImage();

}
