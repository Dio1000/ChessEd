package me.dariansandru.utilities.observer;

import java.util.HashSet;
import java.util.Set;

public class Observable<T>{
    private final Set<Observer<T>> observers = new HashSet<>();

    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    public void notifyObservers(T data) {
        for(Observer<T> observer : observers) {
            observer.notify(data);
        }
    }
}
