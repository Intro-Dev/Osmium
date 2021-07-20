package com.intro.mixin;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ShaderEffect.class)
public interface ShaderEffectInvoker {

    @Invoker
    public Framebuffer invokeGetTarget(String name);

}
