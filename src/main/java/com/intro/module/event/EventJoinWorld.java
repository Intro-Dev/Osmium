package com.intro.module.event;

import net.minecraft.network.protocol.game.ClientboundLoginPacket;

public class EventJoinWorld extends Event{

    public ClientboundLoginPacket packet;

    public EventJoinWorld(ClientboundLoginPacket packet) {
        super(EventDirection.POST, false);
        this.packet = packet;
    }
}
