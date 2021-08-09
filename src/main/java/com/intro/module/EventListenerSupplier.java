package com.intro.module;

import com.intro.module.event.Event;

@FunctionalInterface
public interface EventListenerSupplier {

    void run(Event event);

}
