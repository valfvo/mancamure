package com.fvostudio.project.mancamure;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.fvostudio.project.mancamure.gom.Movement;

public class HumanPlayer extends AwalePlayer {
    private Socket clientSocket;
    private InputStream input;
    // private InputStreamReader reader;

    public HumanPlayer(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.input = this.clientSocket.getInputStream();
        // this.reader = new InputStreamReader(this.input);
    }

    @Override
    public Movement play() {
        try {
            int test = input.read();
            Movement movement = new AwaleMovement(test);            

            return movement;
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

        return null;  // TODO
    }
}
