package com.intro.module.event;

import net.minecraft.client.network.AbstractClientPlayerEntity;

public class EventRemovePlayer extends Event {

    public AbstractClientPlayerEntity entity;

    public EventRemovePlayer(AbstractClientPlayerEntity entity) {
        super(EventDirection.POST, false);
        this.entity = entity;
    }
}
