package dev.lobstershack.client.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.lobstershack.client.event.EventBuss;
import dev.lobstershack.client.event.EventReceiveChatMessage;
import dev.lobstershack.client.event.EventType;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;

@Mixin(ChatListener.class)
public class ChatListenerMixin {

    @Inject(method = "showMessageToPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V"))
    public void onChat(ChatType.Bound bound, PlayerChatMessage chatMessage, Component component, GameProfile gameProfile, boolean bl, Instant instant, CallbackInfoReturnable<Boolean> cir) {
        EventBuss.postEvent(new EventReceiveChatMessage(bound.chatType(), chatMessage.decoratedContent(), gameProfile.getId()), EventType.EVENT_RECEIVE_CHAT_MESSAGE);
    }
}
