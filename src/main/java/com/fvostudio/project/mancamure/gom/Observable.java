package com.fvostudio.project.mancamure.gom;

import java.net.Socket;
import java.util.List;

public interface Observable {
    List<Socket> getObservers();

    void addObserver(Socket observerSocket);

    void addObservers(List<Socket> observerSockets);
}
