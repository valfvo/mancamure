package com.fvostudio.project.mancamure;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.fvostudio.project.mancamure.gom.Movement;
import com.fvostudio.project.mancamure.gom.util.Vector3;

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
            // TODO: -1 = abandonment
            System.out.println("waiting player move...");

            int x = input.read();
            int y = input.read();

            if (x == -1 || y == -1) {
                throw new IllegalStateException("player disconnected unexpectedly");
            }

            System.out.println("received player move");

            AwaleBoardState currentState =
                (AwaleBoardState) getGame().getBoard().getState();

            int pitIndex = currentState.getPitIndex(new Vector3(x, y, 0.0));

            return new AwaleMovement(pitIndex);
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

        return null;  // TODO
    }
}
