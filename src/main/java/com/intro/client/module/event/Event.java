package com.intro.client.module.event;

import com.intro.client.OsmiumClient;

/**
<p>The core of the event system, all events extend this class</p>
 <p>When an event is posted, the {@link OsmiumClient.EVENT_BUS#postEvent(Event, EventType)} is called and passed
 the event object and an EventType. This is then used to post to all registered callbacks for that event type.</p>

 @see com.intro.client.module.EventListenerSupplier
 @see EventType
 @see OsmiumClient.EVENT_BUS
 @since 1.0.0
 @author Intro
 */
public class Event {
    public Event(EventDirection direction) {
        this.direction = direction;
    }

    public final EventDirection direction;


    public boolean isPre() {
        return this.direction == EventDirection.PRE;
    }

    public boolean isPost() {
        return this.direction == EventDirection.POST;
    }

}
