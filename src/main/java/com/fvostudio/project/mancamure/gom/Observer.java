package com.fvostudio.project.mancamure.gom;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Observer {
    private Socket socket;
    private OutputStream output;

    public Observer(Socket socket) {
        setSocket(socket);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;

        try {
            this.output = socket.getOutputStream();
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    public void update(byte[] message) {
        try {
            output.write(message);
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }
}
