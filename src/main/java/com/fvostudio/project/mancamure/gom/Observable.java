package com.fvostudio.project.mancamure.gom;

import java.util.List;

public interface Observable {
    List<? extends Observer> getObservers();

    void addObserver(Observer observer);

    void addObservers(List<? extends Observer> observers);

    default void notifyObservers(byte[] message) {
        for (Observer observer : getObservers()) {
            observer.update(message);
        }
    }
}
