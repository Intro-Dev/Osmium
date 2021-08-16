package com.intro.client.module.event;

public class EventTick extends Event{

    public EventTick(EventDirection eventDirection) {
        super(eventDirection, false);
    }
}
