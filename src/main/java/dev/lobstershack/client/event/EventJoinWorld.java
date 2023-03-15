package dev.lobstershack.client.event;

import net.minecraft.network.protocol.game.ClientboundLoginPacket;

public class EventJoinWorld extends Event{

    public final ClientboundLoginPacket packet;

    public EventJoinWorld(ClientboundLoginPacket packet) {
        super(EventDirection.POST);
        this.packet = packet;
    }
}
