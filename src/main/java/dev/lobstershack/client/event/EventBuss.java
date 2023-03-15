package dev.lobstershack.client.event;

import java.util.ArrayList;
import java.util.HashMap;

public class EventBuss {

    public static final HashMap<Integer, ArrayList<EventListenerSupplier>> mappedListeners = new HashMap<>();

    public static void initListenerMap() {
        for (EventType eventType : EventType.values()) {
            mappedListeners.put(eventType.getIntVal(), new ArrayList<>());
        }
    }

    public static void registerCallback(EventListenerSupplier supplier, EventType event) {
        ArrayList<EventListenerSupplier> list = mappedListeners.get(event.getIntVal());
        list.add(supplier);
        mappedListeners.put(event.getIntVal(), list);
    }

    public static void registerCallback(EventListenerSupplier supplier, EventType[] events) {
        for (EventType event : events) {
            ArrayList<EventListenerSupplier> list = mappedListeners.get(event.getIntVal());
            list.add(supplier);
            mappedListeners.put(event.getIntVal(), list);
        }
    }

    public static void postEvent(Event event, EventType type) {
        for (EventListenerSupplier supplier : mappedListeners.get(type.getIntVal())) {
            supplier.run(event);
        }
    }

}
