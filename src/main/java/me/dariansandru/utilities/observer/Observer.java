package me.dariansandru.utilities.observer;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Observer<T> {
    private T reference = null;
    private final Set<Consumer<T>> callbacks = new HashSet<>();

    public void notify(T reference) {
        this.reference = reference;
        for (Consumer<T> callback : callbacks) {
            callback.accept(reference);
        }
    }

    public T getReference() {
        return reference;
    }

    public void addChangeListener(Consumer<T> callback) {
        callbacks.add(callback);
    }
}
