package com.fvostudio.project.mancamure;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
// import java.io.IOException;
// import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import com.fvostudio.project.mancamure.gom.Element;
import com.fvostudio.project.mancamure.gom.GameHandler;

// import javafx.application.Application;
// import javafx.concurrent.Worker;
// import javafx.scene.Scene;
// import javafx.scene.web.WebEngine;
// import javafx.scene.web.WebView;
// import javafx.stage.Stage;
// import netscape.javascript.JSObject;

// public class App extends Application {
public class App {
    private enum Role {
        OBSERVER,
        PLAYER
    }

    private enum GameType {
        EVE,
        PVE,
        PVP
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

            int depth = input.read();
            boolean isAbPruningEnabled = input.read() != 0;

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
        // launch(args);
        ServerSocket serverSocket = new ServerSocket(3034);
        // ArrayList<Thread> clientThreads = new ArrayList<>();
        // Arbre d'élément de game

        for (;;) {
            // client request
            // gameType | role |
            Socket clientSocket = serverSocket.accept();
            InputStream input = clientSocket.getInputStream();

            GameType gameType = GameType.values()[input.read()];
            Role role = Role.values()[input.read()];

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

            // ArrayList<Socket> observerSockets = new ArrayList<>();
            // observerSockets.add(clientSocket);
            // est-ce que le client est un joueur attendu ?
            // client is a player or an observer ? (role)
            // game type

            // Awale game = new Awale(List.of(args), clientSocket);
            // GameHandler gameHandler = new GameHandler(game);
            // runningGames.appendChild(gameHandler);
            // new Thread(gameHandler).start();

            // clientThreads.add(new Thread(game));
            // clientThreads.get(clientThreads.size() - 1).start();
            // game.run();
        }

        // System.out.println("Press enter to exit...");
        // Scanner scan = new Scanner(System.in);
        // scan.nextLine();
        // scan.close();
    }
}
