package dev.lobstershack.client.event;

@FunctionalInterface
public interface EventListenerSupplier {

    void run(Event event);

}
