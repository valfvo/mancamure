package com.fvostudio.project.mancamure.app;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class App extends Application {
    Timer timer;
    WatchService watchService;
    Path path;

    /**
     * Fonction auxiliaire pour ajouter une classe à un Noeud
     * @param node
     * @param className
     * @return
     */
    public static Node component(Node node, String className) {
        node.getStyleClass().add(className);
        return node;
    }

    @Override
    public void start(Stage stage) throws IOException {
        GridPane boardGame = new GridPane();
        boardGame.getStyleClass().add("board-game");

        boardGame.add(component(new Rectangle(100, 220), "granary"), 0, 0, 1, 2);

        for (int row = 0; row < 2; ++row) {
            for (int column = 1; column < 7; ++column) {
                boardGame.add(component(new Rectangle(100, 100), "circle"), column, row);
            }
        }

        boardGame.add(component(new Rectangle(100, 220), "granary"), 7, 0, 1, 2);

        Scene scene = new Scene(boardGame, 1280, 720);
        String cssFolder = System.getProperty("user.dir").replace('\\', '/')
                           + "/src/main/css/";
        String uri = "file:///" + cssFolder + "style.css";
        scene.getStylesheets().add(uri);

        watchService = FileSystems.getDefault().newWatchService();

        path = Paths.get(cssFolder);
        path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        timer = new Timer("StylesheetWatcher", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                WatchKey key = watchService.poll();
                if (key != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path changed = (Path) event.context();
                        if (changed.endsWith("style.css")) {
                            scene.getStylesheets().clear();
                            scene.getStylesheets().add(uri);
                        }
                    }
                    key.reset();
                }
            }
        }, 500, 500);

        stage.setTitle("Mancamure");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
