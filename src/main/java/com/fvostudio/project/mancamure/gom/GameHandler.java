package com.fvostudio.project.mancamure.gom;

public class GameHandler extends Element implements Runnable {
    private Game game;

    public GameHandler(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        game.start();

        synchronized (getParent()) {
            remove();
        }
    }

    public Game getGame() {
        return game;
    }
}