package com.intro.module;

import com.intro.module.event.EventType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Tells the event system to compile this into the optimised system at startup.</p>
 * @see com.intro.module.event.Event
 * @see Module
 * @since 1.0.6
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface EventListener {
    /**
     * The events that the method listens to.
     * This is for optimizations at runtime init
     */
    EventType[] ListenedEvents();
}
