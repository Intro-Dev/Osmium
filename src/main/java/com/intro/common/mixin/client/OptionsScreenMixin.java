package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.EventDirection;
import com.intro.client.module.event.EventSettingsChange;
import com.intro.client.module.event.EventType;
import com.intro.client.render.screen.OsmiumOptionsScreen;
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

    private final Minecraft mc = Minecraft.getInstance();

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
        OsmiumClient.EVENT_BUS.postEvent(new EventSettingsChange(EventDirection.POST), EventType.EVENT_SETTINGS_CHANGE);
    }

    @Inject(at = @At("HEAD"), method = "removed")
    private void onClosePre(CallbackInfo info) {
        OsmiumClient.EVENT_BUS.postEvent(new EventSettingsChange(EventDirection.PRE), EventType.EVENT_SETTINGS_CHANGE);
    }
}
