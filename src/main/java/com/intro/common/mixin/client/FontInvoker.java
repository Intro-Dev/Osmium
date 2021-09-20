package com.intro.common.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Font.class)
public interface FontInvoker {

    @Invoker
    public FontSet invokeGetFontSet(ResourceLocation resourceLocation);

    @Invoker
    public void invokeRenderChar(BakedGlyph bakedGlyph, boolean bl, boolean bl2, float f, float g, float h, Matrix4f matrix4f, VertexConsumer vertexConsumer, float i, float j, float k, float l, int m);

}
