package com.intro.client.module;

import com.intro.client.module.event.Event;

@FunctionalInterface
public interface EventListenerSupplier {

    void run(Event event);

}
