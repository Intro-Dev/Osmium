package com.intro.common.config.options;

public class Option<T> {

    private T value;
    private final String identifier;

    public Option(String identifier, T value) {
        this.value = value;
        this.identifier = identifier;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
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
