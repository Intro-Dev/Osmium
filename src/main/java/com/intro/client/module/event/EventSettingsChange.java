package com.intro.client.module.event;

public class EventSettingsChange extends Event{

    public EventSettingsChange(EventDirection direction) {
        super(direction, false);
    }
}
