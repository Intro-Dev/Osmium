package com.intro.client.module.event;

import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;

public class EventCustomPacket extends Event {

    private final ClientboundCustomPayloadPacket payload;

    public ClientboundCustomPayloadPacket getPayload() {
        return payload;
    }

    public EventCustomPacket(EventDirection direction, ClientboundCustomPayloadPacket payload) {
        super(direction);
        this.payload = payload;
    }
}
