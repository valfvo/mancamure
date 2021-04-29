package com.fvostudio.project.mancamure;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

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
    private static LinkedList<AwaleObserver> waitingEveObservers = new LinkedList<>();

    private static Element runningPveGames = new Element();
    // private static LinkedList<AwaleObserver> waitingPvePlayers = new LinkedList<>();
    private static LinkedList<AwaleObserver> waitingPveObservers = new LinkedList<>();

    private static Element runningPvpGames = new Element();
    private static LinkedList<AwaleObserver> waitingPvpPlayers = new LinkedList<>();
    private static LinkedList<AwaleObserver> waitingPvpObservers = new LinkedList<>();

    public static void onEveGameRequest(Socket clientSocket, Role role) throws IOException {
        if (role == Role.OBSERVER) {
            InputStream input = clientSocket.getInputStream();

            boolean isAbPruningEnabledForAIOne = input.read() != 0;
            int depthOfAIOne = input.read();

            boolean isAbPruningEnabledForAITwo = input.read() != 0;
            int depthOfAITwo = input.read();

            AwaleObserver observer =
                new AwaleObserver(clientSocket, AwaleBoard.POV.LOWER_PLAYER);
            waitingEveObservers.offer(observer);

            Awale game = new Awale();
            game.addObservers(waitingEveObservers);

            AwaleBoard board = new AwaleBoard();
            game.setBoard(board);

            AIPlayer playerOne = new AIPlayer();
            game.add(playerOne);
            AwaleMinmax minmaxPlayerOne =
            new AwaleMinmax(board, depthOfAIOne, playerOne);
            minmaxPlayerOne.setAbPruningEnabled(isAbPruningEnabledForAIOne);
            playerOne.setAlgorithm(minmaxPlayerOne);

            AIPlayer playerTwo = new AIPlayer();
            game.add(playerTwo);
            AwaleMinmax minmaxPlayerTwo =
            new AwaleMinmax(board, depthOfAITwo, playerTwo);
            minmaxPlayerTwo.setAbPruningEnabled(isAbPruningEnabledForAITwo);
            playerTwo.setAlgorithm(minmaxPlayerTwo);

            GameHandler gameHandler = new GameHandler(game);
            runningEveGames.appendChild(gameHandler);

            Thread gameThread = new Thread(gameHandler);
            gameThread.start();

            waitingEveObservers.clear();
        }
    }
    
    public static void onPveGameRequest(Socket clientSocket, Role role) throws IOException {
        if (role == Role.PLAYER) {
            InputStream input = clientSocket.getInputStream();

            boolean isAbPruningEnabled = input.read() != 0;
            int depth = input.read();

            AwaleObserver upperPlayer =
                new AwaleObserver(clientSocket, AwaleBoard.POV.UPPER_PLAYER);

            waitingPveObservers.offer(upperPlayer);

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

            // TODO: get observer's pov
            AwaleObserver observer =
                new AwaleObserver(clientSocket, AwaleBoard.POV.UPPER_PLAYER);

            if (lastPveGame == null) {
                waitingPveObservers.offer(observer);
            } else {
                Awale game = (Awale) lastPveGame.getGame();
                game.addObserver(observer);
            }
        }
    }

    public static void onPvpGameRequest(Socket clientSocket, Role role) {
        // TODO: handle external observer
        if (waitingPvpPlayers.size() > 0) {
            AwaleObserver lowerPlayer =
                new AwaleObserver(clientSocket, AwaleBoard.POV.LOWER_PLAYER);

            waitingPvpObservers.offer(lowerPlayer);

            Awale game = new Awale();
            game.addObservers(waitingPvpObservers);

            AwaleBoard board = new AwaleBoard();
            game.setBoard(board);

            Socket upperPlayerSocket = waitingPvpPlayers.get(0).getSocket();
            HumanPlayer playerOne = new HumanPlayer(upperPlayerSocket);
            game.add(playerOne);

            Socket lowerPlayerSocket = lowerPlayer.getSocket();
            HumanPlayer playerTwo = new HumanPlayer(lowerPlayerSocket);
            game.add(playerTwo);

            GameHandler gameHandler = new GameHandler(game);
            runningPvpGames.appendChild(gameHandler);

            Thread gameThread = new Thread(gameHandler);
            gameThread.start();

            waitingPvpObservers.clear();
            waitingPvpPlayers.clear();
        } else {
            AwaleObserver upperPlayer =
                new AwaleObserver(clientSocket, AwaleBoard.POV.UPPER_PLAYER);

            waitingPvpObservers.offer(upperPlayer);
            waitingPvpPlayers.offer(upperPlayer);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3034);

        try {
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
        } finally {
            serverSocket.close();
        }
    }
}
