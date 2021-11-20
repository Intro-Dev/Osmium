package com.intro.common.mixin.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleTexture.TextureImage.class)
public interface ResourceTextureSubclassAccessor {

    @Accessor
    NativeImage getImage();

}
