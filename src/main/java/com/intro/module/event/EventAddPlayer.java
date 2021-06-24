package com.intro.module.event;

import net.minecraft.client.network.AbstractClientPlayerEntity;

public class EventAddPlayer extends Event {
    public AbstractClientPlayerEntity entity;
    public EventAddPlayer(AbstractClientPlayerEntity entity) {
        super(EventDirection.POST, false);
        this.entity = entity;
    }
}
