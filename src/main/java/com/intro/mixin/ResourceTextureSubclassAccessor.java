package com.intro.mixin;

import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net/minecraft/client/texture/ResourceTexture$TextureData")
public interface ResourceTextureSubclassAccessor {

    @Accessor
    NativeImage getImage();


}
