package dev.lobstershack.client.mixin.client;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChatComponent.class)
public interface ChatComponentInvoker {

    @Invoker
    void invokeAddMessage(Component chatComponent, @Nullable MessageSignature headerSignature, int addedTime, @Nullable GuiMessageTag tag, boolean onlyTrim);

}
