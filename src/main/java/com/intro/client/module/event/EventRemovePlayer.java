package com.intro.client.module.event;

import net.minecraft.client.player.AbstractClientPlayer;

public class EventRemovePlayer extends Event {

    public final AbstractClientPlayer entity;

    public EventRemovePlayer(AbstractClientPlayer entity) {
        super(EventDirection.POST);
        this.entity = entity;
    }
}
