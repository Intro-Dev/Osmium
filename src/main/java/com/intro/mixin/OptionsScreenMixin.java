package com.intro.mixin;

import com.intro.Osmium;
import com.intro.module.event.EventDirection;
import com.intro.module.event.EventSettingsChange;
import com.intro.module.event.EventType;
import com.intro.render.screen.OsmiumOptionsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    private Minecraft mc = Minecraft.getInstance();

    protected OptionsScreenMixin(TranslatableComponent title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo info) {
        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableComponent("osmium.options.title"), (buttonWidget) -> {
            mc.setScreen(new OsmiumOptionsScreen(this));
        }));
    }

    @Inject(at = @At("TAIL"), method = "removed")
    private void onClosePost(CallbackInfo info) {
        Osmium.EVENT_BUS.postEvent(new EventSettingsChange(EventDirection.POST), EventType.EVENT_SETTINGS_CHANGE);
    }

    @Inject(at = @At("HEAD"), method = "removed")
    private void onClosePre(CallbackInfo info) {
        Osmium.EVENT_BUS.postEvent(new EventSettingsChange(EventDirection.PRE), EventType.EVENT_SETTINGS_CHANGE);
    }
}
