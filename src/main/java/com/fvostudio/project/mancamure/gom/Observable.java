package com.fvostudio.project.mancamure.gom;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public interface Observable {
    List<Socket> getObservers();

    void addObserver(Socket observerSocket);

    void addObservers(List<Socket> observerSockets);

    default void notifyObservers(byte[] message) {
        for (Socket observerSocket : getObservers()) {
            try {
                OutputStream output = observerSocket.getOutputStream();
                output.write(message);
            } catch (IOException e) {
                // TODO: handle exception
                System.out.println(e.getMessage());
            }
        }
    }
}
