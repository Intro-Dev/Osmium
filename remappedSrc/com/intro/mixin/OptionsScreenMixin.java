package com.intro.mixin;

import com.intro.Osmium;
import com.intro.module.event.EventDirection;
import com.intro.module.event.EventSettingsChange;
import com.intro.render.screen.OsmiumOptionsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    public int tsX = 10, tsY = 10;


    MinecraftClient mc = MinecraftClient.getInstance();
    private final GameOptions settings;

    protected OptionsScreenMixin(Text title, GameOptions gameOptions, GameOptions settings) {
        super(title);
        this.settings = settings;
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo info) {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableText("osmium.options.title"), (buttonWidget) -> {
            this.mc.openScreen(new OsmiumOptionsScreen(this));
        }));
    }

    @Inject(at = @At("TAIL"), method = "removed")
    private void onClosePost(CallbackInfo info) {
        Osmium.EVENT_BUS.postEvent(new EventSettingsChange(EventDirection.POST));
    }

    @Inject(at = @At("HEAD"), method = "removed")
    private void onClosePre(CallbackInfo info) {
        Osmium.EVENT_BUS.postEvent(new EventSettingsChange(EventDirection.POST));
    }
}
