package com.intro.common.util;

public class InstanceHolder<T> {

    private T instance;

    public void setInstance(T instance) {
        this.instance = instance;
    }

    public T getInstance() {
        return instance;
    }
}
