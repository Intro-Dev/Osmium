package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.EventReceiveChatMessage;
import com.intro.client.module.event.EventType;
import com.intro.client.render.RenderManager;
import com.intro.common.config.Options;
import com.intro.common.config.options.StatusEffectDisplayMode;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.chat.ChatListener;
import net.minecraft.network.chat.ChatSender;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Gui.class)

public class GuiMixin {
    @Shadow @Final private List<ChatListener> chatListeners;

    @Inject(at = @At("TAIL"), method = "render")
    public void render(PoseStack stack, float f, CallbackInfo ci) {
        RenderManager.renderHud(stack);
    }

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void renderHead(PoseStack stack, float f, CallbackInfo ci) {
        if(!RenderManager.shouldRenderHud) {
            ci.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "renderEffects", cancellable = true)
    public void renderStatusEffectOverlay(PoseStack matrixStack, CallbackInfo ci) {
        if(OsmiumClient.options.getEnumOption(Options.StatusEffectDisplayMode).get() == StatusEffectDisplayMode.CUSTOM)
            ci.cancel();
    }

    @Inject(method = "handlePlayerChat", at = @At(value = "HEAD", target = "Ljava/util/List;iterator()Ljava/util/Iterator"))
    public void onChat(ChatType chatType, Component component, ChatSender sender, CallbackInfo ci) {
        OsmiumClient.EVENT_BUS.postEvent(new EventReceiveChatMessage(chatType, component, sender.uuid()), EventType.EVENT_RECEIVE_CHAT_MESSAGE);
    }
}
