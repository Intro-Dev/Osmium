package dev.lobstershack.client.config.options;

import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.util.DebugUtil;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Option<T> {

    private T value;
    private final String identifier;
    private final ArrayList<Consumer<T>> valueChangeListeners = new ArrayList<>();

    public Option(String identifier, T def) {
        this.set(def);
        this.identifier = identifier;
        Options.put(identifier, this);
    }

    private Option(String identifier) {
        this.identifier = identifier;
    }

    public static <T> Option<T> unboundOptionOf(String identifier, T value) {
        Option<T> option = new Option<>(identifier);
        option.set(value);
        return option;
    }

    public void setUnsafe(Object o) {
        if(!value.getClass().isNestmateOf(o.getClass())) throw new ClassCastException("Object " + o + " cannot be assigned to option " + identifier + " with option type " + value.getClass());
        value = (T) o;
        this.broadcastUpdate();
        DebugUtil.logIfDebug("Setting option " + this.identifier + " to value " + o, Level.INFO);
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
        this.broadcastUpdate();
        DebugUtil.logIfDebug("Setting option " + this.identifier + " to value " + value, Level.INFO);
    }

    public void broadcastUpdate() {
        valueChangeListeners.forEach(tConsumer -> tConsumer.accept(this.value));
    }

    public void addChangeListener(Consumer<T> listener) {
        valueChangeListeners.add(listener);
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "Option{" +
                "value=" + value +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
