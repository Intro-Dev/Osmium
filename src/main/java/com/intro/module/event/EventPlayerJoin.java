package com.intro.module.event;

import net.minecraft.client.network.AbstractClientPlayerEntity;

public class EventPlayerJoin extends Event {
    public AbstractClientPlayerEntity entity;
    public EventPlayerJoin(AbstractClientPlayerEntity entity) {
        super(EventDirection.POST, false);
        this.entity = entity;
    }
}
