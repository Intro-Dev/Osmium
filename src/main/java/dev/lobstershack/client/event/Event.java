package dev.lobstershack.client.event;

/**
<p>The core of the event system, all events extend this class</p>
 <p>When an event is posted, the {@link EventBuss#postEvent(Event, EventType)} is called and passed
 the event object and an EventType. This is then used to post to all registered callbacks for that event type.</p>

 @see EventListenerSupplier
 @see EventType
 @see EventBuss
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
