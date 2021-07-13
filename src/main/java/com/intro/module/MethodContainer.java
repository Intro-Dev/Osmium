package com.intro.module;

import com.intro.module.event.EventTick;
import com.intro.module.event.EventType;

import java.lang.reflect.Method;

/**
 * <p>Contains a method with its accompanying {@link Module} and {@link com.intro.module.event.Event} type.
 *  * Used by the event system to call methods.</p>
 * @see com.intro.module.event.Event
 * @see Module
 * @see EventListener
 * @since 1.0.6
 */
public class MethodContainer {

    public Module module;
    public Method method;
    public EventType[] type;

    public MethodContainer(Module module, Method method, EventType[] type) {
        this.method = method;
        this.module =  module;
        this.type = type;
    }

}