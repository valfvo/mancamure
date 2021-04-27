package com.fvostudio.project.mancamure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.fvostudio.project.mancamure.gom.Movement;
import com.fvostudio.project.mancamure.gom.util.Pair;
import com.fvostudio.project.mancamure.gom.util.Vector3;

public class HumanPlayer extends AwalePlayer {
    private Socket clientSocket;
    private InputStream input;
    private OutputStream output;
    // private InputStreamReader reader;

    public HumanPlayer(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.input = this.clientSocket.getInputStream();
            this.output = this.clientSocket.getOutputStream();
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        // this.reader = new InputStreamReader(this.input);
    }

    @Override
    public Movement play() {
        try {
            // TODO: -1 = abandonment
            System.out.println("waiting player move...");
            sendPlayRequest();

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

    private void sendPlayRequest() throws IOException {
        // 0: not possible, 1: not legal, 2: legal
        // 3 | x y legality | x y legality | ...
        // size => 19
        byte[] playRequest = new byte[19];
        playRequest[0] = 3;
        int offset = 1;

        AwaleBoardState currentState = (AwaleBoardState) getGame().getBoard().getState();

        for(Pair<Integer, Integer> pit : getPlayableObjects(currentState)) {
            Vector3 pitPosition = currentState.getPitPosition(pit.second);
            playRequest[offset++] = (byte) (int) pitPosition.getX();
            playRequest[offset++] = (byte) (int) pitPosition.getY();

            ArrayList<Movement> movements = getGame().getPossibleMovementsFrom(pit);
            if (!movements.isEmpty()) {
                if (getGame().isLegal(movements.get(0))) {
                    playRequest[offset++] = 2;
                } else {
                    playRequest[offset++] = 1;
                }
            } else {
                playRequest[offset++] = 0;
            }
        }

        output.write(playRequest);
    } 
}
