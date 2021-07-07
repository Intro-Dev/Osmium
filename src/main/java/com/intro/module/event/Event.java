package com.intro.module.event;

/**
 Thc core of the Event system. All events extend this class.
 All events have a direction property, which tells weather the event is going to happen, or already happened.
 The canceled property is currently not well implemented, but will be in the future.
 To use events, make a Module class, and to use common events like EventTick or EventRender, add a @EventListener to the onEvent method.
 Event method lists for common events are compiled on runtime startup for performance.
 Events are posted to all listeners via the Osmium.EVENT_BU.postEvent method.

 @see com.intro.module.EventListener
 @see com.intro.module.Module
 @see com.intro.Osmium.EVENT_BUS
 @since 1.0.0
 @author Intro
 */
public class Event {
    public Event(EventDirection direction, boolean canceled) {
        this.direction = direction;
        Canceled = canceled;
    }

    public EventDirection direction;
    public boolean Canceled;

    public EventDirection getDirection() {
        return direction;
    }

    public boolean isCanceled() {
        // return Canceled;
        throw new UnsupportedOperationException("Event cancellation is currently not implemented!");
    }

    public void setCanceled(boolean canceled) {
        // Canceled = canceled;
        throw new UnsupportedOperationException("Event cancellation is currently not implemented!");
    }

    public boolean isPre() {
        return this.direction == EventDirection.PRE;
    }

    public boolean isPost() {
        return this.direction == EventDirection.POST;
    }

}
