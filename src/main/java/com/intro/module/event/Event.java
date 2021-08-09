package com.intro.module.event;

/**
<p>The core of the event system, all events extend this class</p>
 <p>When an event is posted, the {@link com.intro.Osmium.EVENT_BUS#postEvent(Event, EventType)} is called and passed
 the event object and an EventType. This is then used to post to all registered callbacks for that event type.</p>

 @see com.intro.module.EventListenerSupplier
 @see EventType
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
        throw new UnsupportedOperationException("Event cancellation is currently not implemented!");
    }

    public void setCanceled(boolean canceled) {
        throw new UnsupportedOperationException("Event cancellation is currently not implemented!");
    }

    public boolean isPre() {
        return this.direction == EventDirection.PRE;
    }

    public boolean isPost() {
        return this.direction == EventDirection.POST;
    }

}
