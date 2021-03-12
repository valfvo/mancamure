package com.fvostudio.project.mancamure.gom;

import java.util.Objects;

public abstract class Board extends Owner {
    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game.GameAuth auth, Game game) {
        Objects.requireNonNull(auth);
        this.game = game;
    }

    public abstract Cell getCell(Vector3 coordinate);

    public abstract BoardState getState();

    public abstract void changeState(BoardState state);
}
