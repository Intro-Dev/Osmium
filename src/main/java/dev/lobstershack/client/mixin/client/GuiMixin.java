package dev.lobstershack.client.mixin.client;

import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.config.options.StatusEffectDisplayMode;
import dev.lobstershack.client.render.widget.drawable.DrawableRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)

public class GuiMixin {

    @Inject(at = @At("TAIL"), method = "render")
    public void render(GuiGraphics graphics, float f, CallbackInfo ci) {
        DrawableRenderer.renderHud(graphics);
    }

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void renderHead(GuiGraphics graphics, float f, CallbackInfo ci) {
        if(!DrawableRenderer.shouldRenderHud) {
            ci.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "renderEffects", cancellable = true)
    public void renderStatusEffectOverlay(GuiGraphics graphics, CallbackInfo ci) {
        if(Options.StatusEffectDisplayMode.get() == StatusEffectDisplayMode.CUSTOM)
            ci.cancel();
    }

}
