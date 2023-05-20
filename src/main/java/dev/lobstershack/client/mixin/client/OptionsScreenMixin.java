package dev.lobstershack.client.mixin.client;

import dev.lobstershack.client.event.EventBuss;
import dev.lobstershack.client.event.EventDirection;
import dev.lobstershack.client.event.EventSettingsChange;
import dev.lobstershack.client.event.EventType;
import dev.lobstershack.client.render.screen.OsmiumOptionsScreen;
import dev.lobstershack.client.render.widget.AbstractScalableButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    private final Minecraft mc = Minecraft.getInstance();

    protected OptionsScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo info) {
        if(this.mc.options.guiScale().get() > 5) {
            this.addRenderableWidget(new AbstractScalableButton(this.width / 2 - 50, this.height / 6 + 140, 100, 20, Component.translatable("osmium.options.title"), (button) -> mc.setScreen(new OsmiumOptionsScreen(this))));
        } else {
            this.addRenderableWidget(new AbstractScalableButton(this.width / 2 - 100, this.height / 6 + 200, 200, 20, Component.translatable("osmium.options.title"), (buttonWidget) -> mc.setScreen(new OsmiumOptionsScreen(this))));
        }
    }

    @Inject(at = @At("TAIL"), method = "removed")
    private void onClosePost(CallbackInfo info) {
        EventBuss.postEvent(new EventSettingsChange(EventDirection.POST), EventType.EVENT_SETTINGS_CHANGE);
    }

    @Inject(at = @At("HEAD"), method = "removed")
    private void onClosePre(CallbackInfo info) {
        EventBuss.postEvent(new EventSettingsChange(EventDirection.PRE), EventType.EVENT_SETTINGS_CHANGE);
    }
}
