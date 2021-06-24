package com.intro.module.event;

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
        return Canceled;
    }

    public void setCanceled(boolean canceled) {
        Canceled = canceled;
    }

    public boolean isPre() {
        if(this.direction == EventDirection.PRE)
            return true;
        return false;
    }
}
