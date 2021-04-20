package com.fvostudio.project.mancamure;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.fvostudio.project.mancamure.gom.Element;
import com.fvostudio.project.mancamure.gom.GameHandler;

public class App {
    private enum GameType {
        EVE,
        PVE,
        PVP;

        private static final GameType[] gameTypeValues = GameType.values();

        public static GameType valueOf(int ordinal) {
            return gameTypeValues[ordinal];
        }
    }

    private enum Role {
        OBSERVER,
        PLAYER;

        private static final Role[] roleValues = Role.values();

        public static Role valueOf(int ordinal) {
            return roleValues[ordinal];
        }
    }

    private static Element runningEveGames = new Element();
    private static LinkedList<Socket> waitingEveObservers = new LinkedList<>();

    private static Element runningPveGames = new Element();
    // private static LinkedList<Socket> waitingPvePlayers = new LinkedList<>();
    private static LinkedList<Socket> waitingPveObservers = new LinkedList<>();

    private static Element runningPvpGames = new Element();
    private static LinkedList<Socket> waitingPvpPlayers = new LinkedList<>();
    private static LinkedList<Socket> waitingPvpObservers = new LinkedList<>();

    public static void onEveGameRequest(Socket clientSocket, Role role) {
        // waitingEveObservers.offer(clientSocket);
        throw new UnsupportedOperationException();
    }

    public static void onPveGameRequest(Socket clientSocket, Role role) throws IOException {
        if (role == Role.PLAYER) {
            InputStream input = clientSocket.getInputStream();

            boolean isAbPruningEnabled = input.read() != 0;
            int depth = input.read();

            waitingPveObservers.offer(clientSocket);

            Awale game = new Awale();
            game.addObservers(waitingPveObservers);

            AwaleBoard board = new AwaleBoard();
            game.setBoard(board);

            HumanPlayer playerOne = new HumanPlayer(clientSocket);
            game.add(playerOne);

            AIPlayer playerTwo = new AIPlayer();
            game.add(playerTwo);
            AwaleMinmax minmax = new AwaleMinmax(board, depth, playerTwo);
            minmax.setAbPruningEnabled(isAbPruningEnabled);
            playerTwo.setAlgorithm(minmax);

            GameHandler gameHandler = new GameHandler(game);
            runningPveGames.appendChild(gameHandler);

            Thread gameThread = new Thread(gameHandler);
            gameThread.start();

            waitingPveObservers.clear();
        } else {
            GameHandler lastPveGame = (GameHandler) runningPveGames.getLastChild();

            if (lastPveGame == null) {
                waitingPveObservers.offer(clientSocket);
            } else {
                Awale game = (Awale) lastPveGame.getGame();
                game.addObserver(clientSocket);
            }
        }
    }

    public static void onPvpGameRequest(Socket clientSocket, Role role) {
        // waitingPvpObservers.offer(clientSocket);
        // if (role == Role.PLAYER) {
        //     waitingPvpPlayers.offer(clientSocket);
        // }
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3034);

        for (;;) {
            // client request
            // gameType | role | algorithm | depth
            Socket clientSocket = serverSocket.accept();
            InputStream input = clientSocket.getInputStream();

            GameType gameType = GameType.valueOf(input.read());
            Role role = Role.valueOf(input.read());

            switch (gameType) {
                case EVE:
                    onEveGameRequest(clientSocket, role);
                    break;
                case PVE:
                    onPveGameRequest(clientSocket, role);
                    break;
                case PVP:
                    onPvpGameRequest(clientSocket, role);
                    break;
                default:
                    break;
            }
        }
    }
}
