package com.intro.client.module.event;

import net.minecraft.client.player.AbstractClientPlayer;

public class EventRemovePlayer extends Event {

    public AbstractClientPlayer entity;

    public EventRemovePlayer(AbstractClientPlayer entity) {
        super(EventDirection.POST, false);
        this.entity = entity;
    }
}
