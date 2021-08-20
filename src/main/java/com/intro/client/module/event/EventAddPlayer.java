package com.intro.client.module.event;

import net.minecraft.client.player.AbstractClientPlayer;

public class EventAddPlayer extends Event {

    public final AbstractClientPlayer entity;

    public EventAddPlayer(AbstractClientPlayer entity) {
        super(EventDirection.POST);
        this.entity = entity;
    }
}
