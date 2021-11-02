package com.intro.client.module.event;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class EventReceiveChatMessage extends Event {

    private final ChatType type;
    private final Component component;
    private final UUID uuid;

    public EventReceiveChatMessage(ChatType type, Component component, UUID uuid) {
        super(EventDirection.POST);
        this.component = component;
        this.type = type;
        this.uuid = uuid;
    }

    public ChatType getType() {
        return type;
    }

    public Component getComponent() {
        return component;
    }

    public UUID getUuid() {
        return uuid;
    }
}
