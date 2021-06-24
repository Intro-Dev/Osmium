package com.intro.module.event;

import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;

public class EventJoinWorld extends Event{

    public GameJoinS2CPacket packet;

    public EventJoinWorld(GameJoinS2CPacket packet) {
        super(EventDirection.POST, false);
        this.packet = packet;
    }
}
